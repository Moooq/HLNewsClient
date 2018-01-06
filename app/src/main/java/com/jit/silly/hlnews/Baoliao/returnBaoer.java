package com.jit.silly.hlnews.Baoliao;

/**
 * Created by moqiandemac on 2017/6/23.
 */

public class returnBaoer {
    int code;
    String msg;
    BaoerInfo data;

    public returnBaoer(int code, String msg, BaoerInfo data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BaoerInfo getData() {
        return data;
    }

    public void setData(BaoerInfo data) {
        this.data = data;
    }
}
