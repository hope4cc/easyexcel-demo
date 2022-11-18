package com.hope.easyexcel.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 文件名：StudentInfoQuery
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-01:13
 * 描述：分页
 */
@Data
public class StudentInfoQuery {
    private String id;
    private String name;
    private Integer selectedAcademy;
    private Integer selectedMajor;
    private Integer selectedClass;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
    @NotNull
    private Integer pageNum;
    @NotNull
    private Integer pageSize;
}
