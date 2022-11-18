package com.hope.easyexcel.exception;

/**
 * 文件名：StudentInfoInsertException
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-02:39
 * 描述：学校获取学院专业班级等信息 自定义异常类
 */
public class SchoolInfoNotFoundException extends Exception {
    public SchoolInfoNotFoundException() {
    }

    public SchoolInfoNotFoundException(String message) {
        super(message);
    }
}
