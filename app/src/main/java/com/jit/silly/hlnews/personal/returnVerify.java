package com.jit.silly.hlnews.personal;

/**
 * Created by moqiandemac on 2017/6/19.
 */

public class returnVerify {
    private int code;
    private String msg;
    private VerifyInfo data;

    public returnVerify(int code, String msg, VerifyInfo data) {
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

    public VerifyInfo getData() {
        return data;
    }

    public void setData(VerifyInfo data) {
        this.data = data;
    }
}
