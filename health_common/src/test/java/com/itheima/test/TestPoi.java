package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @ClassName TestPoi
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/17 8:56
 * @Version V1.0
 */
public class TestPoi {

    // 2.2.1. 从Excel文件读取数据
    /**
     * XSSFWorkBook：工作簿
     * XSSFSheet：工作表
     * XSSFRow：行
     * XSSFCell：单元格
     */
    @Test
    public void readPoi() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook("D:/hello.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);// 第1个工作表
        // 简便
        for (Row row : sheet) {
            for (Cell cell : row) {
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
        }
        workbook.close();
    }

    // 2.2.1. 从Excel文件读取数据
    // 还有一种方式就是获取工作表最后一个行号，从而根据行号获得行对象，通过行获取最后一个单元格索引，从而根据单元格索引获取每行的一个单元格对象，代码如下：
    /**
     * XSSFWorkBook：工作簿
     * XSSFSheet：工作表
     * XSSFRow：行
     * XSSFCell：单元格
     */
    @Test
    public void readPoi2() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook("D:/hello.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);// 第1个工作表
        // 通过行号和单元格号
        int rows = sheet.getLastRowNum();
        System.out.println("最后一行的行号："+rows);
        for(int i=0;i<=rows;i++){
            XSSFRow row = sheet.getRow(i);
            short cells = row.getLastCellNum();
            for(int j=0;j<cells;j++){
                XSSFCell cell = row.getCell(j);
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
        }

        workbook.close();
    }

    // 2.2.2. 向Excel文件写入数据
    /**
     * XSSFWorkBook：工作簿
     * XSSFSheet：工作表
     * XSSFRow：行
     * XSSFCell：单元格
     */
    @Test
    public void writePoi() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();// 创建工作表
        XSSFRow row1 = sheet.createRow(0);// 创建一行（0表示第1行）
        row1.createCell(0).setCellValue("姓名");
        row1.createCell(1).setCellValue("年龄");
        row1.createCell(2).setCellValue("地址");
        row1.createCell(3).setCellValue("学历");

        XSSFRow row2 = sheet.createRow(1);// 创建一行（0表示第1行）
        row2.createCell(0).setCellValue("熊大");
        row2.createCell(1).setCellValue("20");
        row2.createCell(2).setCellValue("北京");
        row2.createCell(3).setCellValue("本科");

        XSSFRow row3 = sheet.createRow(2);// 创建一行（0表示第1行）
        row3.createCell(0).setCellValue("熊二");
        row3.createCell(1).setCellValue("18");
        row3.createCell(2).setCellValue("东北");
        row3.createCell(3).setCellValue("研究生");

        OutputStream out = new FileOutputStream("D:/abc76.xlsx");
        workbook.write(out);
        out.flush();
        out.close();
        workbook.close();
    }
}
