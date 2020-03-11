package com.wukong.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.Base64Utils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

/**
 * excel导入导出工具类
 * 约定：
 * 1、前三行为标题行，第四行开始为数据行
 * created by wangbao6
 * created in 2019-07-04
 */
public final class ExcelTool<T> {

    //数据行第一行在表格的第四行，前面三行留做标题和表头使用
    public static final int ROW_TITLE_INDEX=0;
    public static final int ROW_VARIABLE_HEADER_CELL_SIZE_INDEX=1;
    public static final int ROW_COMMON_HEADER_INDEX=2;
    public static final int ROW_DATA_INDEX=3;

    /**
     * 获取客户端浏览器类型、编码下载文件名
     * @param request
     * @param fileName
     * @return String
     * @author kf6567
     * @date 2016-8-17
     */
    public static String encodeFileName(HttpServletRequest request, String fileName) {
        String userAgent = request.getHeader("User-Agent");
        String rtn = "";
        try {
            String new_filename = URLEncoder.encode(fileName, "UTF8");

            // 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
            rtn = "filename=\"" + new_filename + "\"";
            if (userAgent != null) {
                userAgent = userAgent.toLowerCase();
                // IE浏览器，只能采用URLEncoder编码
                if (userAgent.contains("msie")) {
                    rtn = "filename=\"" + new_filename + "\"";
                }
                // Opera浏览器只能采用filename*
                else if (userAgent.contains("opera")) {
                    rtn = "filename*=UTF-8''" + new_filename;
                }
                // Safari浏览器，只能采用ISO编码的中文输出
                else if (userAgent.contains("safari")) {
                    rtn = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
                }
                // Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
                else if (userAgent.contains("applewebkit")) {
                    rtn = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
                }
                // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
                else if (userAgent.contains("mozilla")) {
                    rtn = "filename*=UTF-8''" + new_filename;
                }
            }
        } catch (UnsupportedEncodingException e) {

        }
        return rtn;
    }

    /**
     * 下载到客户端浏览器
     *
     * @param workbook 要下载的Excel表格文件
     * @param response 响应
     * @param fileName 文件名称
     * @throws IOException io流异常
     */
    public static void downloadBrowser(Workbook workbook, HttpServletResponse response, String fileName, HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");

        // 清空response
        response.reset();

        // 设置response的响应头Header，控制浏览器以下载的形式打开文件
        response.setHeader("Content-disposition",
                "attachment;" + encodeFileName(request, fileName));
        //获得输出流，包装成缓冲流可以提高输入、输出效率。但需要写flush方法才能清空缓冲区
        OutputStream toClient = new BufferedOutputStream(
                response.getOutputStream());

        //设置文件类型和编码格式
        response.setContentType("application/vnd.ms-excel;charset=utf-8");

        //将表格数据直接写入到输出流
        workbook.write(toClient);

        //flush方法迫使缓冲的输出数据被写出到底层输出流中，其实其内部也是调用write方法
        toClient.flush();
        //关闭流
        toClient.close();
    }

    /**
     * 根据文件后缀名判断该文件是否是Excel表格，如果是则返回true，否则返回FALSE
     *
     * @param fileName 表示文件类型的后缀
     * @return 布尔值
     */
    public static boolean isExcelFile(String fileName) {

        String suffix = StringUtils.substringAfterLast(fileName, ".");

        if (suffix.equalsIgnoreCase("xls")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据文件输入流创建表格对象
     * @param fileName
     * @param intStream
     * @return
     * @throws IOException
     */
    public static Workbook createWorkbook(String fileName, InputStream intStream) throws IOException {
        if(isExcelFile(fileName))
            return new HSSFWorkbook(intStream);
        else return null;
    }

    /**
     * 生成默认表格样式
     * @param workbook 表格
     * @return 样式
     */
    public static CellStyle getDefaultCellStyle(Workbook workbook){
        //创建样式
        CellStyle cellStyle = workbook.createCellStyle();
        //垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //自动换行
        cellStyle.setWrapText(true);
        //边框为黑色细实线
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        return cellStyle;
    }

    /**
     * 生成默认表格样式
     * @param workbook 表格
     * @return 样式
     */
    public static CellStyle getLinkCellStyle(Workbook workbook){

        CellStyle cellStyle = workbook.createCellStyle();
        Font cellFont = workbook.createFont();
        cellFont.setUnderline(HSSFFont.U_SINGLE);
        cellFont.setFontHeightInPoints((short) 11);
        cellStyle.setFont(cellFont);
        return cellStyle;
    }

    /**
     * 生成默认字体，10号加粗
     * @param workbook 表格
     * @return 字体
     */
    public static Font getDefaultFont(Workbook workbook){
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);
        return font;
    }

    /**
     * 获取单元格样式
     *
     * @param workbook excel文件（创建样式用）
     * @return 第单元格样式
     */
    public static CellStyle getCommonCellStyle(Workbook workbook) {
        //样式
        CellStyle cellStyle = ExcelTool.getDefaultCellStyle(workbook);
        //左对齐
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        //字体
        cellStyle.setFont(ExcelTool.getCommonCellFont(workbook));
        return cellStyle;
    }

    /**
     * 普通文本的字体
     * @param workbook excel
     * @return 字体
     */
    public static Font getCommonCellFont(Workbook workbook){
        Font font = ExcelTool.getDefaultFont(workbook);
        font.setColor(Font.COLOR_NORMAL);
        return font;
    }

    /**
     * 获取表格模板单元格样式
     *
     * @param workbook excel文件（创建样式用）
     * @return 第单元格样式
     */
    public static CellStyle getMouldCellStyle(Workbook workbook) {
        //样式
        CellStyle cellStyle = ExcelTool.getDefaultCellStyle(workbook);
        //左对齐
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setFont(ExcelTool.getMouldCellFont(workbook));
        return cellStyle;
    }

    /**
     * 获取excel模板提示信息的字体
     * @param workbook excel
     * @return 字体
     */
    public static Font getMouldCellFont(Workbook workbook){
        Font font = ExcelTool.getDefaultFont(workbook);
        font.setColor(Font.COLOR_RED);
        return font;
    }

    /**
     * 获取第一列样式
     *
     * @param workbook excel文件（创建样式用）
     * @return 第一列样式
     */
    public static CellStyle getHeadCellStyle(Workbook workbook) {
        CellStyle firstCellStyle = ExcelTool.getDefaultCellStyle(workbook);
        //居中
        firstCellStyle.setAlignment(HorizontalAlignment.CENTER);
        //字体
        firstCellStyle.setFont(ExcelTool.getHeadFont(workbook));
        return firstCellStyle;
    }

    /**
     * 获取标题行的字体
     * @param workbook excel
     * @return 字体
     */
    public static Font getHeadFont(Workbook workbook){
        Font font = ExcelTool.getDefaultFont(workbook);
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        return font;
    }

    /**
     * 根据excel文件生成一个名字为sheetName的sheet，在根据headers生成表头， 普通文字
     *
     * @param workbook  excl文件
     * @param sheetName sheet的名字
     * @param headers   表头集合
     * @return 表
     */
    public static Sheet createSheetWithCommonHeader(Workbook workbook, String sheetName, List<String> headers) {

        Sheet sheet = workbook.createSheet(sheetName);

        sheet.setDefaultColumnWidth((short) 45);
        // 生成一个样式
        CellStyle style = ExcelTool.getHeadCellStyle(workbook);

        // 产生表格标题行
        Row row = sheet.createRow(ROW_COMMON_HEADER_INDEX);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers.get(i));
        }
        return sheet;
    }

    /**
     * 根据excel文件生成一个名字为sheetName的sheet，在根据headers生成表头，富文本
     *
     * @param workbook  excl文件
     * @param sheetName sheet的名字
     * @param headers   表头集合
     * @return 表
     */
    public static Sheet createSheetWithRichTextHeader(Workbook workbook, String sheetName, List<RichTextString> headers) {

        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        CellStyle style = ExcelTool.getHeadCellStyle(workbook);

        // 产生表格表头行
        Row row = sheet.createRow(ROW_COMMON_HEADER_INDEX);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers.get(i));
        }
        return sheet;
    }

    /**
     * 通用的创建表格方法
     * @param dataList 需要导出的数据集合
     * @param fieldNames 属性名集合
     * @param headers 表头集合
     * @param sheetName 数据表名称
     * @return WorkBook对象
     * @throws NoSuchFieldException 找不到属性
     * @throws IllegalAccessException
     */
    public static Workbook createCommonWorkbookWithCommonHeader(List dataList, List<String> fieldNames, List<String> headers, String sheetName) throws NoSuchFieldException, IllegalAccessException {

        // 声明一个工作薄，xls格式，新版excel也能打开
        HSSFWorkbook workbook = new HSSFWorkbook();
        return createCommonWorkbookWithCommonHeader(workbook, dataList, fieldNames, headers, sheetName);
    }

    /**
     * 导出对象的所有一级属性
     * @param dataList
     * @param headers
     * @param sheetName
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Workbook createCommonWorkbookWithCommonHeader(List dataList, List<String> headers, String sheetName) throws NoSuchFieldException, IllegalAccessException {

        // 声明一个工作薄，xls格式，新版excel也能打开
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet = createSheetWithCommonHeader(workbook,sheetName,headers);
        return createCommonWorkbook(workbook, sheet, dataList);
    }

    public static Workbook createCommonWorkbookWithCommonHeader(Workbook workbook, List dataList, List<String> fieldNames, List<String> headers, String sheetName) throws NoSuchFieldException, IllegalAccessException {
        // 创建一个数据表(sheet)并且生成表头
        Sheet sheet = createSheetWithCommonHeader(workbook,sheetName,headers);
        return createCommonWorkbook(workbook, sheet, dataList, fieldNames);
    }
    public static Workbook createCommonWorkbookWithRichTextHeader(Workbook workbook, List dataList, List<String> fieldNames, List<RichTextString> headers, String sheetName) throws NoSuchFieldException, IllegalAccessException {
        // 创建一个数据表(sheet)并且生成表头
        Sheet sheet = createSheetWithRichTextHeader(workbook,sheetName,headers);
        return createCommonWorkbook(workbook, sheet, dataList, fieldNames);
    }

    /**
     * 通用导出方法，可导出对象属性中的属性，层级数量不限
     * @param workbook
     * @param sheet
     * @param dataList 要导出的数据
     * @param fieldNames 属性，如果是属性套属性，则多级属性之间用点号分隔 如“attribute1.attribute2”;
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Workbook createCommonWorkbook(Workbook workbook, Sheet sheet, List dataList, List<String> fieldNames) throws NoSuchFieldException, IllegalAccessException {
        CellStyle defaultCellStyle = ExcelTool.getCommonCellStyle(workbook);

        // 遍历集合数据，产生数据行
        Row row;
        int index = ROW_DATA_INDEX;
        for (Object data : dataList) {
            index++;
            row = sheet.createRow(index);
            row.setHeightInPoints(25);
            int cellIndex = 0;
            //获取集合元素的类类型，也就是要下载的类的类类型
            Class c = data.getClass();

            //遍历属性名集合，获取每一个要导出的属性的名字
            for (String fieldName : fieldNames) {

                //如果是属性中有好几级属性（该字段是引用类型），获取每一级属性的名字
                String[] fields = fieldName.split("\\.");

                //定义要插入到数据库中的数据对象
                Object insertToCell = null;
                for (int i = 0; i < fields.length; i++) {

                    //i== 0说明是第一级属性
                    if (i == 0) {
                        //从data中根据属性名获取属性对象
                        Field nameField = c.getDeclaredField(fields[i]);
                        //设置属性对象可读
                        nameField.setAccessible(true);
                        //获取data中该属性的值
                        insertToCell = nameField.get(data);
                        //如果第一级属性为null，则不解析下一级属性
                        if (insertToCell == null) {
                            break;
                        }
                    } else {
                        //从上一级属性值中获取属性对象
                        Field nameField = insertToCell.getClass().getDeclaredField(fields[i]);
                        //设置可读
                        nameField.setAccessible(true);
                        //获取上一级属性对象中该属性的值
                        insertToCell = nameField.get(insertToCell);
                    }
                }

                //创建单元格
                Cell cell = row.createCell(cellIndex++);
                //设置单元格类型为字符串
                cell.setCellType(CellType.STRING);
                cell.setCellStyle(defaultCellStyle);
                //插入数据
                if (insertToCell == null) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue("" + insertToCell);
                }
            }
        }

        return workbook;
    }

    /**
     * 创建标题行
     * @param sheetName
     * @param workbook
     * @param title
     * @param cellSize 这个标题占用几行
     */
    public static void createTitleRow( Workbook workbook,String sheetName, String title, int cellSize) {
        Sheet sheet = workbook.getSheet(sheetName);
        //标题行
        Row rowTitle = sheet.createRow(ROW_TITLE_INDEX);
        rowTitle.setHeight((short) (15 * 35));
        Cell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellStyle(ExcelTool.getHeadCellStyle(workbook));
        cellTitle.setCellValue(title);
        sheet.addMergedRegion(new CellRangeAddress(ROW_TITLE_INDEX, ROW_TITLE_INDEX, 0, cellSize - 1));
    }

    /**
     * 生成合并单元格的表头
     * @param workbook
     * @param sheetName
     * @param headers
     * @param cellSize 每个表头占用杰单元格
     * @return
     */
    public static Sheet createVariableCellSizeHeaderWithRichText(Workbook workbook, String sheetName, List<RichTextString> headers, int cellSize) {

        Sheet sheet = workbook.getSheet(sheetName);
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        CellStyle style = ExcelTool.getHeadCellStyle(workbook);

        int cellIndex = -1;
        // 产生表格表头行
        Row row = sheet.createRow(ROW_VARIABLE_HEADER_CELL_SIZE_INDEX);
        IntStream.rangeClosed(0, headers.size() * cellSize - 1).boxed().map(c -> row.createCell(c)).forEach(cell -> cell.setCellStyle(style));

        for (int i = 0; i < headers.size(); i++) {
            sheet.addMergedRegion(new CellRangeAddress(ROW_VARIABLE_HEADER_CELL_SIZE_INDEX,ROW_VARIABLE_HEADER_CELL_SIZE_INDEX, cellIndex + 1, cellIndex + cellSize ));
            Cell cell = row.getCell(cellIndex + 1);
            cell.setCellValue(headers.get(i));
            cellIndex += cellSize;
        }
        return sheet;
    }

    /**
     * 生成合并单元格的表头
     * @param workbook
     * @param sheetName
     * @param headers
    //     * @param cellSize 每个表头占用杰单元格
     * @return
     */
    public static Sheet createVariableCellSizeHeaderWithRichText(Workbook workbook,String sheetName, List<MergeHeader> headers) {

        Sheet sheet = workbook.getSheet(sheetName);
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        CellStyle style = ExcelTool.getHeadCellStyle(workbook);

        int cellIndex = -1;
        // 产生表格表头行
        Row row = sheet.createRow(ROW_VARIABLE_HEADER_CELL_SIZE_INDEX);
        int totalSize = headers.stream().mapToInt(MergeHeader::getSize).sum();
        IntStream.rangeClosed(0, totalSize -1).boxed().map(row::createCell).forEach(cell -> cell.setCellStyle(style));

        for (int i = 0; i < headers.size(); i++) {
            sheet.addMergedRegion(new CellRangeAddress(ROW_VARIABLE_HEADER_CELL_SIZE_INDEX,ROW_VARIABLE_HEADER_CELL_SIZE_INDEX, cellIndex + 1, cellIndex + headers.get(i).getSize() ));
            Cell cell = row.getCell(cellIndex + 1);
            cell.setCellValue(headers.get(i).getRichTextString());
            cellIndex += cellIndex + headers.get(i).getSize();
        }
        return sheet;
    }


    /**
     * 创建富文本表头
     * @param workbook 表格实体
     * @param headers 表头，使用"|"作为分隔符，分隔符前面的是黑色字体，分隔符后边将会变成红色字体，例如“属性名称|（属性填写要求）”
     * @return 富文本
     */
    public static List<RichTextString> convertToRichText(Workbook workbook, List<String> headers){

        List<RichTextString> richTextStrings = new ArrayList<>();

        for(String head : headers){
            String[] headSplit = StringUtils.split(head, "|");
            String wholeHead = StringUtils.remove(head, "|");
            RichTextString richTextString = new HSSFRichTextString(wholeHead);
            richTextString.applyFont(0, headSplit[0].length(), getHeadFont(workbook));
            if(headSplit.length > 1){
                richTextString.applyFont(headSplit[0].length() , wholeHead.length(), getMouldCellFont(workbook));
            }
            richTextStrings.add(richTextString);
        }
        return richTextStrings;
    }

    public static RichTextString convertToRichText(Workbook workbook, String header) {
        String[] headSplit = StringUtils.split(header, "|");
        String wholeHead = StringUtils.remove(header, "|");
        RichTextString richTextString = new HSSFRichTextString(wholeHead);
        richTextString.applyFont(0, headSplit[0].length(), getHeadFont(workbook));
        if (headSplit.length > 1) {
            richTextString.applyFont(headSplit[0].length(), wholeHead.length(), getMouldCellFont(workbook));
        }
        return richTextString;
    }

    /**
     * 从表格中解析数据,注意表格中各个属性的顺序要和java类中一致，本a
     * @param workbook
     * @param clazz
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public List<T> getDataFromExcel(Workbook workbook, Class<T> clazz) throws IllegalAccessException, InstantiationException {

        Field[] fields = clazz.getDeclaredFields();

        List<T> dataList = new ArrayList<>();
        // 解析sheet
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            int readRowCount = sheet.getPhysicalNumberOfRows();
            // 解析sheet 的行
            for (int j = ROW_DATA_INDEX; j < readRowCount; j++) {
                Row row = sheet.getRow(j);
                if (row == null) {
                    continue;
                }
                if (row.getFirstCellNum() < 0) {
                    continue;
                }
                // 解析sheet 的列
                T t = clazz.newInstance();
                for (int k = 0; k < fields.length; k++) {
                    Cell cell = row.getCell(k);
                    Field field = fields[k];
                    field.setAccessible(true);
                    if(cell == null) {
                        continue;
                    }
                    cell.setCellType(CellType.STRING);
                    if(field.getType() == String.class){
                        field.set(t, cell.getStringCellValue());
                    } else if (field.getType() == Integer.class){
                        field.set(t, Integer.parseInt(cell.getStringCellValue()));
                    } else if(field.getType() == Double.class) {
                        field.set(t, Double.valueOf(cell.getStringCellValue()));
                    } else if(field.getType() == Float.class) {
                        field.set(t, Float.valueOf(cell.getStringCellValue()));
                    } else if(field.getType() == Long.class) {
                        field.set(t, Long.valueOf(cell.getStringCellValue()));
                    }
                    // todo 添加更多类型
                }
                dataList.add(t);
            }
            return dataList;
        }
        return Collections.emptyList();
    }

    /**
     * 导出对象中的所有一级属性
     * @param workbook
     * @param sheet
     * @param dataList
     * @return
     * @throws IllegalAccessException
     */
    public static Workbook createCommonWorkbook(Workbook workbook, Sheet sheet, List dataList) throws IllegalAccessException {
        CellStyle defaultCellStyle = ExcelTool.getCommonCellStyle(workbook);

        // 遍历集合数据，产生数据行
        Row row;
        int index = ROW_DATA_INDEX;
        for (Object data : dataList) {
            row = sheet.createRow(index);
            row.setHeightInPoints(25);
            int cellIndex = 0;
            //获取集合元素的类类型，也就是要下载的类的类类型
            Class c = data.getClass();

            Field[] fields = c.getDeclaredFields();
            //遍历属性名集合，获取每一个要导出的属性的名字
            for (Field field : fields) {

                //设置属性对象可读
                field.setAccessible(true);
                //获取data中该属性的值
                Object insertToCell = field.get(data);
                //创建单元格
                Cell cell = row.createCell(cellIndex++);
                //设置单元格类型为字符串
                cell.setCellType(CellType.STRING);
                cell.setCellStyle(defaultCellStyle);
                //插入数据
                if (insertToCell == null) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue("" + insertToCell);
                }
            }
            index++;
        }
        return workbook;
    }



    /**
     * 导出对象的特定一级属性
     * @param dataList
     * @param headers
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Workbook createSelectedWorkbookWithCommonHeader(List dataList, List<String> fields, List<String> headers, boolean pic) throws IllegalAccessException, NoSuchFieldException {

        // 声明一个工作薄，xls格式，新版excel也能打开
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet = createSheetWithCommonHeader(workbook,"sheet",headers);
        return createSelectedWorkbookWithPicture(workbook, sheet, dataList, fields, pic);
    }

    /**
     * 导出对象中的所有一级属性
     * @param workbook
     * @param sheet
     * @param dataList
     * @return
     * @throws IllegalAccessException
     */
    private static Workbook createSelectedWorkbookWithPicture(Workbook workbook, Sheet sheet, List dataList, List<String> fieldList, boolean pic) throws IllegalAccessException, NoSuchFieldException {
        CellStyle defaultCellStyle = ExcelTool.getCommonCellStyle(workbook);
        // 遍历集合数据，产生数据行
        Row row;
        int index = ROW_DATA_INDEX;
        for (Object data : dataList) {
            row = sheet.createRow(index);
            int cellIndex = 0;
            //获取集合元素的类类型，也就是要下载的类的类类型
            Class c = data.getClass();

            //遍历属性名集合，获取每一个要导出的属性的名字
            for (String fieldName : fieldList) {
                Field field = c.getDeclaredField(fieldName);
                //设置对象的访问权限，保证对private的属性的访问
                field.setAccessible(true);
                //设置属性对象可读
                field.setAccessible(true);
                //获取data中该属性的值
                Object insertToCell = field.get(data);
                //创建单元格
                Cell cell = row.createCell(cellIndex);
                //设置单元格类型为字符串
                cell.setCellType(CellType.STRING);
                cell.setCellStyle(defaultCellStyle);
                //插入数据
                if (insertToCell == null) {
                    cell.setCellValue("");
                } else if(field.getType() == String.class){
                    String value = String.valueOf(insertToCell);
                    if(pic && value.contains("http")){

                        if(!pic){
                            cell.setCellFormula("HYPERLINK(\"" + value + "\")");
                            cell.setCellStyle(getLinkCellStyle(workbook));
                            cell.setCellValue(value);
                            cellIndex ++;
                            continue;
                        }
                        //TODO 插入图片
                        byte[] bytes = new byte[0];
                        try {
                            bytes = image2Byte(value);
                        } catch (Exception e) {
                            cell.setCellFormula("HYPERLINK(\"" + value + "\")");
                            cell.setCellStyle(getLinkCellStyle(workbook));
                            cell.setCellValue(value);
                            cellIndex ++;
                            continue;
                        }
                        row.setHeightInPoints(160);
                        Drawing patriarch = sheet.createDrawingPatriarch();

                        CreationHelper helper = sheet.getWorkbook().getCreationHelper();
                        ClientAnchor anchor = helper.createClientAnchor();

                        // 图片插入坐标
                        anchor.setCol1(cellIndex);
                        anchor.setRow1(index);

                        // 指定我想要的长宽
                        double standardWidth = 360;
                        double standardHeight = 200;

                        // 计算单元格的长宽
                        double cellWidth = sheet.getColumnWidthInPixels(cell.getColumnIndex());
                        double cellHeight = cell.getRow().getHeightInPoints()/72*96;

                        // 计算需要的长宽比例的系数
                        double a = standardWidth / cellWidth;
                        double b = standardHeight / cellHeight;

                        // 插入图片
                        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
                        Picture pict = patriarch.createPicture(anchor, pictureIdx);
                        pict.resize(a,b);
                    } else {
                        cell.setCellValue(value);
                    }
                } else if(field.getType() == Date.class){
                    Date date = (Date)insertToCell;
                    String s = DateTimeTool.formatFullDateTime(date.getTime());
                    cell.setCellValue(s);
                }else {
                    cell.setCellValue("" + insertToCell);
                }
                cellIndex ++;
            }
            index++;
        }
        return workbook;
    }


    public static byte[] image2Byte(String imgUrl) {
        if(imgUrl.contains("/ngx/proxy?i=")){
            String urlBase64 = StringUtils.substringAfter(imgUrl, "/ngx/proxy?i=");
            imgUrl = new String(Base64Utils.decodeFromString(urlBase64));
        }
        URL url;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(imgUrl);
            urlConnection = ( HttpURLConnection ) url.openConnection();
            urlConnection.setConnectTimeout(60000);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();

            baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = inputStream.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                baos.write(buffer, 0, len);
            }
            // 对字节数组Base64编码
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return new byte[]{};
    }

    public static class MergeHeader{
        private RichTextString richTextString;
        private int size;

        public MergeHeader(RichTextString richTextString, int size) {
            this.richTextString = richTextString;
            this.size = size;
        }

        public RichTextString getRichTextString() {
            return richTextString;
        }

        public void setRichTextString(RichTextString richTextString) {
            this.richTextString = richTextString;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}
