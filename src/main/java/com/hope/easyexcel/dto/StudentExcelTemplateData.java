package com.hope.easyexcel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.hope.easyexcel.utils.StatusConverter;
import lombok.Data;

import java.util.Date;

/**
 * 文件名：StudentExcelTemplateData
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-01:13
 * 描述：模板
 */
@Data
public class StudentExcelTemplateData {
    @ExcelProperty(value = "姓名")
    @ColumnWidth(15)
    private String name;

    @ColumnWidth(15)
    @ExcelProperty(value = "民族")
    private String nation;

    @ColumnWidth(15)
    @ExcelProperty(value = "性别")
    private String sex;

    @ExcelProperty(value = "年龄")
    @ColumnWidth(15)
    private String age;

    @ExcelProperty(value = "政治面貌")
    @ColumnWidth(15)
    private String politicsStatus;

    @ExcelProperty(value = "身份证号")
    @ColumnWidth(30)
    private String idCard;

    @ExcelProperty(value = "联系电话")
    @ColumnWidth(25)
    private String phoneNum;

    @ExcelProperty(value = "邮箱")
    @ColumnWidth(25)
    private String email;

    @ExcelProperty(value = "学院")
    @ColumnWidth(30)
    private String academyName;

    @ExcelProperty(value = "专业")
    @ColumnWidth(20)
    private String majorName;

    @ExcelProperty(value = "班级")
    @ColumnWidth(15)
    private String className;

    @ExcelProperty(value = "入学时间")
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd")
    private Date createTime;

    @ExcelProperty(value = "学籍状态",converter = StatusConverter.class)
    @ColumnWidth(15)
    private String status;
}
