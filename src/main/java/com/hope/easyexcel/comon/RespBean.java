package com.hope.easyexcel.comon;

import lombok.Getter;
import lombok.ToString;

/**
 * 文件名：RespBean
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/11/17-02:13
 * 描述：统一返回类
 */
@ToString
@Getter
public class RespBean {
    private Integer status;
    private String msg;
    private Object object;

    public RespBean() {}

    public RespBean(ResponseCode code) {
        this(code.getCode(), code.getMsg());
    }

    public RespBean(ResponseCode code, Object object) {
        this(code.getCode(), code.getMsg());
        this.object = object;
    }

    private RespBean(Integer status, String message) {
        this.status = status;
        this.msg = message;
    }

    private RespBean(Integer status, String message, Object object) {
        this.status = status;
        this.msg = message;
        this.object = object;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.status = responseCode.getCode();
        this.msg = responseCode.getMsg();
    }

    public void setCodeAndData(ResponseCode responseCode, Object data) {
        this.status = responseCode.getCode();
        this.msg = responseCode.getMsg();
        this.object = data;
    }
}
