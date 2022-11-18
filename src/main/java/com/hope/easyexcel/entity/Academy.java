package com.hope.easyexcel.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 文件名：Academy
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-01:09
 * 描述：学院
 */
@Data
public class Academy {
    private Integer id;
    private String name;

    @JSONField(name = "children")
    private List<Major> majors;


}
