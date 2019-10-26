package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:56
 * @Version V1.0
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    MemberService memberService;

    @Reference
    SetmealService setmealService;

    @Reference
    ReportService reportService;

    // 统计报表：按月份查询注册会员的数量（完成折线图）
    @RequestMapping(value = "/getMemberReport")
    public Result getMemberReport(){
        try {
            // 组织月份的集合List<String>，当前月计算前12个月
            List<String> monthsList = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH,-12); // 当前时间往前推12个月
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH,1); // 向前推12个月，再向后推1个月（2018-11）
                String month = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
                monthsList.add(month);
            }
            List<Integer> memberCountList = memberService.findMemberCountByRegTime(monthsList);
            Map<String,Object> map = new HashMap<>();
            map.put("months",monthsList);
            map.put("memberCount",memberCountList);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    // 统计报表：预约套餐占比情况（完成饼图）
    @RequestMapping(value = "/getSetmealReport")
    public Result getSetmealReport(){
        try {
            // 查询套餐预约占比情况
            // 组织查询数据中的setmealCount
            List<Map<String,Object>> setmealCountList =  setmealService.findSetmealReport();
            // 组织查询数据中的setmealNames
            List<String> setmealList = new ArrayList<>();
            if(setmealCountList!=null && setmealCountList.size()>0){
                for (Map<String, Object> map : setmealCountList) {
                    String name = (String)map.get("name");
                    setmealList.add(name);
                }
            }
            Map<String,Object> map = new HashMap<>();
            map.put("setmealNames",setmealList);
            map.put("setmealCount",setmealCountList);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    // 统计报表：运营数据展示（页面）
    @RequestMapping(value = "/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> results = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,results);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    // 统计报表：运营数据展示（Excel导出报表）
    @RequestMapping(value = "/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            // 一：获取对应的excel报表的数据
            Map<String,Object> results = reportService.getBusinessReportData();
            // 数据
            String reportDate = (String) results.get("reportDate");
            Integer todayNewMember = (Integer)results.get("todayNewMember");
            Integer totalMember = (Integer)results.get("totalMember");
            Integer thisWeekNewMember = (Integer)results.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer)results.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer)results.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer)results.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer)results.get("thisWeekOrderNumber");
            Integer thisWeekVisitsNumber = (Integer)results.get("thisWeekVisitsNumber");
            Integer thisMonthOrderNumber = (Integer)results.get("thisMonthOrderNumber");
            Integer thisMonthVisitsNumber = (Integer)results.get("thisMonthVisitsNumber");
            List<Map<String,Object>> hotSetmeal = (List<Map<String,Object>>)results.get("hotSetmeal");

            // 二：使用POI查找模板的位置，加载excel模板，将数据填充到excel的对应单元格中
            String path = request.getSession().getServletContext().getRealPath("template")+File.separator+"report_template.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(path)));
            XSSFSheet sheet = workbook.getSheetAt(0);
            // 日期
            sheet.getRow(2).getCell(5).setCellValue(reportDate);
            // 新增会员数
            sheet.getRow(4).getCell(5).setCellValue(todayNewMember);
            // 总会员数
            sheet.getRow(4).getCell(7).setCellValue(totalMember);
            // 本周新增会员数
            sheet.getRow(5).getCell(5).setCellValue(thisWeekNewMember);
            // 本月新增会员数
            sheet.getRow(5).getCell(7).setCellValue((Integer)results.get("thisMonthNewMember"));

            Row row = null;
            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            // 三：数据加载到excel文件，将excel文件已IO的形式在页面上导出（下载）
            ServletOutputStream outputStream = response.getOutputStream();

            // 1：下载的文件类型（excel）
            response.setContentType("application/vnd.ms-excel");
            // 2：下载的文件的形式（附件/内连）
            // 附件：response.setHeader("Content-Disposition","attachment;filename=report75.xls");
            // 内连：response.setHeader("Content-Disposition","inline");
            response.setHeader("Content-Disposition","attachment;filename=report75.xlsx");

            workbook.write(outputStream);
            // 刷新缓冲区
            outputStream.flush();
            // 关闭
            outputStream.close();
            // 关闭workbook
            workbook.close();

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
