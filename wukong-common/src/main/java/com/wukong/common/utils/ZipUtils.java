package com.wukong.common.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具类
 */
public class ZipUtils {

    private ZipUtils() {

    }

    /**
     * 压缩文件
     *
     * @param source 待压缩的文件
     * @return 压缩后的文件
     */
    public static File zip(File source) throws IOException {
        String zipName = source.getName() + ".zip";
        return zip(source, zipName);
    }

    /**
     * 压缩文件
     *
     * @param source  待压缩的文件
     * @param zipName 压缩文件名
     * @return 压缩后的文件
     */
    public static File zip(File source, String zipName) throws IOException {
        File target = null;
        if (!zipName.endsWith(".zip"))
            zipName = zipName + ".zip";
        if (source.exists()) {
            target = new File(source.getParent(), zipName);
            if (target.exists()) {
                FileUtils.deleteQuietly(target);
            }

            try (FileOutputStream fos = new FileOutputStream(target);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                addEntry("/", source, zos);

            } catch (IOException e) {
                throw e;
            }
        }
        return target;
    }

    /**
     * 扫描添加文件Entry
     *
     * @param base   基路径
     * @param source 原文件
     * @param zos    Zip文件输出流
     * @throws IOException
     */
    private static void addEntry(String base, File source, ZipOutputStream zos) throws IOException {

        //按目录分级，形如/aaa/bbb.txt
        String entry = base + source.getName();
        if (source.isDirectory()) {
            File[] files = source.listFiles();
            if (files != null) {
                for (File file : files) {
                    addEntry(entry + "/", file, zos);
                }
            }
        } else {
            try (FileInputStream fis = new FileInputStream(source);
                 BufferedInputStream bis = new BufferedInputStream(fis, 1024 * 10)) {
                byte[] buffer = new byte[1024 * 10];
                int read = 0;
                zos.putNextEntry(new ZipEntry(entry));
                while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                    zos.write(buffer, 0, read);
                }
                zos.closeEntry();
            }
        }
    }

    /**
     * 解压文件
     *
     * @param source 待解压文件
     */
    public static void unzip(File source) throws IOException {
        if (source.exists()) {
            BufferedOutputStream bos = null;
            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source))) {
                ZipEntry entry = null;
                while ((entry = zis.getNextEntry()) != null && !entry.isDirectory()) {
                    File target = new File(source.getParent(), entry.getName());
                    if (!target.getParentFile().exists()) {
                        target.getParentFile().mkdirs();
                    }

                    bos = new BufferedOutputStream(new FileOutputStream(target));
                    int read = 0;
                    byte[] buffer = new byte[1024 * 10];
                    while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.flush();
                }
                zis.closeEntry();
            } catch (IOException e) {
                throw e;
            } finally {
                IOUtils.closeQuietly(bos);
            }
        }
    }
}
