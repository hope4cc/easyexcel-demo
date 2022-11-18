package com.hope.easyexcel.exception;

/**
 * 文件名：StudentInfoInsertException
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-02:35
 * 描述：学生导入导出业务 自定义异常类
 */
public class StudentInfoInsertException  extends Exception{
    public StudentInfoInsertException() {
    }

    public StudentInfoInsertException(String message) {
        super(message);
    }
}
