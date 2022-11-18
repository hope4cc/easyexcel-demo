package com.hope.easyexcel.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 文件名：StudentInsert
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-01:13
 * 描述：批量插入
 */
@Data
public class StudentInsert {
    private String id;
    @NotEmpty(message = "姓名不能为空")
    private String name;
    @NotEmpty(message = "性别不能空")
    private String sex;
    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄不能小于0")
    private Integer age;
    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;
    @NotNull(message = "学院信息不能为空")
    private Integer academyId;
    @NotNull(message = "专业信息不能为空")
    private Integer majorId;
    @NotEmpty(message = "身份证号不能为空")
    private String idCard;
    @NotNull(message = "班级信息不能为空")
    private Integer classId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "入学时间不能为空")
    private Date createTime;
    @NotNull(message = "学籍状态不能为空")
    private String status;
}
