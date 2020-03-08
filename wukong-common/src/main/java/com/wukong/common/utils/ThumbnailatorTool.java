package com.wukong.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.lang3.StringUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class ThumbnailatorTool {


    public static void scalePicture(String path, float scale) throws IOException {

        /**
         * scale(比例)
         * Thumbnails.of(fromPic).scale(1f).outputQuality(0.25f).toFile(toPic);
         */
        Thumbnails.of(path)
                .scale(1f)
                .outputQuality(scale)
                .toFile(path);
    }


    /**
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String base64 = getBase64("D:/workspace/ctm01tjcollect/20190712093632_d1aacfd6-cfd9-4b37-aeda-e258d075ccf2.png");
        System.out.println(StringUtils.replace(base64, " ", ""));
//        thumbnailatorTest.test1();
//        ThumbnailatorTool.test2();
//        thumbnailatorTest.test3();
//        thumbnailatorTest.test4();
//        thumbnailatorTest.test5();
//        thumbnailatorTest.test6();
//        thumbnailatorTest.test7();
//        thumbnailatorTest.test8();
//        thumbnailatorTest.test9();

    }

    public static String getBase64(String filePath){
        byte[] data = null;
        try(InputStream inputStream = new FileInputStream(filePath)){
            data = new byte[inputStream.available()];
            inputStream.read(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.getEncoder().encode(data));
    }

    /**
     * 指定大小进行缩放
     * 
     * @throws IOException
     */
    public static void test1() throws IOException {
        /*
         * size(width,height) 若图片横比200小，高比300小，不变
         * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
         * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
         */
        Thumbnails.of("D:/workspace/tjcollect/20190712091648_58afa5fb-eddf-477e-9524-cc44faa9120d.jpg").size(200, 300).toFile("D:/workspace/tjcollect/20190712091648_58afa5fb-eddf-477e-9524-cc44faa9120d.jpg");
//        Thumbnails.of("D:/workspace/tjcollect/20190712091648_58afa5fb-eddf-477e-9524-cc44faa9120d.jpg").size(2560, 2048).toFile("C:/image_2560x2048.jpg");
    }

    /**
     * 按照比例进行缩放
     * 
     * @throws IOException
     */
    public static void test2() throws IOException {
        /**
         * scale(比例)
         */
        Thumbnails.of("D:\\workspace\\tjcollect\\微信图片_20190726115144.jpg")
                .scale(1f)
                .outputQuality(0.5f)
                .toFile("D:\\workspace\\tjcollect\\微信图片_20190726115144.jpg");
//        Thumbnails.of("images/test.jpg").scale(1.10f).toFile("C:/image_110%.jpg");
    }

    /**
     * 不按照比例，指定大小进行缩放
     * 
     * @throws IOException
     */
    public static void test3() throws IOException {
        /**
         * keepAspectRatio(false) 默认是按照比例缩放的
         */
        Thumbnails.of("images/test.jpg").size(120, 120).keepAspectRatio(false).toFile("C:/image_120x120.jpg");
    }

    /**
     * 旋转
     * 
     * @throws IOException
     */
    public static void test4() throws IOException {
        /**
         * rotate(角度),正数：顺时针 负数：逆时针
         */
        Thumbnails.of("images/test.jpg").size(1280, 1024).rotate(90).toFile("C:/image+90.jpg");
        Thumbnails.of("images/test.jpg").size(1280, 1024).rotate(-90).toFile("C:/iamge-90.jpg");
    }

    /**
     * 水印
     * 
     * @throws IOException
     */
    public static void test5() throws IOException {
        /**
         * watermark(位置，水印图，透明度)
         */
        Thumbnails.of("images/test.jpg").size(1280, 1024).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File("images/watermark.png")), 0.5f)
                .outputQuality(0.8f).toFile("C:/image_watermark_bottom_right.jpg");
        Thumbnails.of("images/test.jpg").size(1280, 1024).watermark(Positions.CENTER, ImageIO.read(new File("images/watermark.png")), 0.5f)
                .outputQuality(0.8f).toFile("C:/image_watermark_center.jpg");
    }

    /**
     * 裁剪
     * 
     * @throws IOException
     */
    public static void test6() throws IOException {
        /**
         * 图片中心400*400的区域
         */
        Thumbnails.of("images/test.jpg").sourceRegion(Positions.CENTER, 400, 400).size(200, 200).keepAspectRatio(false)
                .toFile("C:/image_region_center.jpg");
        /**
         * 图片右下400*400的区域
         */
        Thumbnails.of("images/test.jpg").sourceRegion(Positions.BOTTOM_RIGHT, 400, 400).size(200, 200).keepAspectRatio(false)
                .toFile("C:/image_region_bootom_right.jpg");
        /**
         * 指定坐标
         */
        Thumbnails.of("images/test.jpg").sourceRegion(600, 500, 400, 400).size(200, 200).keepAspectRatio(false).toFile("C:/image_region_coord.jpg");
    }

    /**
     * 转化图像格式
     * 
     * @throws IOException
     */
    public static void test7() throws IOException {
        /**
         * outputFormat(图像格式)
         */
        Thumbnails.of("images/test.jpg").size(1280, 1024).outputFormat("png").toFile("C:/image_1280x1024.png");
        Thumbnails.of("images/test.jpg").size(1280, 1024).outputFormat("gif").toFile("C:/image_1280x1024.gif");
    }

    /**
     * 输出到OutputStream
     * 
     * @throws IOException
     */
    public static void test8() throws IOException {
        /**
         * toOutputStream(流对象)
         */
        OutputStream os = new FileOutputStream("C:/image_1280x1024_OutputStream.png");
        Thumbnails.of("images/test.jpg").size(1280, 1024).toOutputStream(os);
    }

    /**
     * 输出到BufferedImage
     * 
     * @throws IOException
     */
    public static void test9() throws IOException {
        /**
         * asBufferedImage() 返回BufferedImage
         */
        BufferedImage thumbnail = Thumbnails.of("images/test.jpg").size(1280, 1024).asBufferedImage();
        ImageIO.write(thumbnail, "jpg", new File("C:/image_1280x1024_BufferedImage.jpg"));
    }
}