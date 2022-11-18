package com.hope.easyexcel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hope.easyexcel.utils.StatusConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * 文件名：Student
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-01:10
 * 描述：学生
 */
@Data
@NoArgsConstructor
public class Student implements Serializable {
    private static final long serialVersionUID = -6195864794816991937L;
    @NotEmpty(message = "学工号不能为空")
    private String id;
    @NotEmpty(message = "姓名不能为空")
    private String name;
    private String nation;
    private String sex;
    private Integer age;
    private String politicsStatus;
    private String idCard;
    private String phoneNum;
    @Email(message = "邮箱格式有误")
    private String email;
    private String avatar;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    private String status;

    private Classes classes;
    private Academy academy;
    private Major major;

    public Student(String id, String name, String idCard, String email) {
        this.id = id;
        this.name = name;
        this.idCard = idCard;
        this.email = email;
    }
}