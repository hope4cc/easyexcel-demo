package com.hope.easyexcel.comon;

/**
 * 文件名：ResponseCode
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-02:14
 * 描述：统一返回类代码
 */
public enum ResponseCode {
    /**
     * 请求成功
     */
    SUCCESS(200, "请求成功"),
    /**
     * 发生其他错误
     */
    ERROR(406, "错误");
    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }

    private int code;
    private String msg;
}
