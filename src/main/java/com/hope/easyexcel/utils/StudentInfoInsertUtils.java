package com.hope.easyexcel.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.hope.easyexcel.dto.StudentExcelData;
import com.hope.easyexcel.dto.StudentExcelTemplateData;
import com.hope.easyexcel.entity.Student;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件名：StudentInfoInsertUtils
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-01:24
 * 描述：excel
 */
public class StudentInfoInsertUtils {
    private static final List<StudentExcelData> EXCEL_DATA = new ArrayList<>();
    /**
     * 样式
     * @return
     */
    private static HorizontalCellStyleStrategy getHorizontalCellStyleStrategy() {
        //表头样式策略
        WriteCellStyle headStyle = new WriteCellStyle();
        //设置表头居中对齐
        headStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 初始化表格样式
        return new HorizontalCellStyleStrategy(headStyle, contentWriteCellStyle);
    }

    /**
     * 导出模板
     * @param response
     * @throws IOException
     */
    public static void getTemplateFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //防止中文乱码
        String filename = URLEncoder.encode("学生信息模板", "utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        EasyExcel.write(response.getOutputStream(), StudentExcelTemplateData.class)
                .registerWriteHandler(getHorizontalCellStyleStrategy())
                .sheet().doWrite(EXCEL_DATA);
    }


    /**
     * 导出数据
     * @param response
     * @param students
     * @param currFilename
     * @throws IOException
     */
    public static void getStudentInfoFile(HttpServletResponse response, List<Student> students, String currFilename) throws IOException {
        List<StudentExcelData> excelData = convertStudentInfo(students);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //防止中文乱码
        String filename = URLEncoder.encode(currFilename, "utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        EasyExcel.write(response.getOutputStream(), StudentExcelData.class)
                .registerWriteHandler(getHorizontalCellStyleStrategy())
                .sheet().doWrite(excelData);
    }

    /**
     * excel list 转换
     * @param students
     * @return
     */
    private static List<StudentExcelData> convertStudentInfo(List<Student> students) {
        List<StudentExcelData> excelData = new ArrayList<>();
        for (Student student : students) {
            excelData.add(new StudentExcelData(student.getId(), student.getName(), student.getNation(), student.getSex(),
                    student.getAge().toString(), student.getPoliticsStatus(), student.getIdCard(), student.getPhoneNum(),
                    student.getEmail(), student.getAcademy().getName(), student.getMajor().getName(), student.getClasses().getName(),
                    student.getCreateTime(),student.getStatus()));
        }
        return excelData;
    }

}
