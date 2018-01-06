package com.jit.silly.hlnews.Baoliao;

/**
 * Created by moqiandemac on 2017/6/20.
 */

public class returnBao {
    int code;
    String msg;
    BaoInfo[] data;

    public returnBao(BaoInfo[] data, int code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public BaoInfo[] getData() {
        return data;
    }

    public void setData(BaoInfo[] data) {
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
}
