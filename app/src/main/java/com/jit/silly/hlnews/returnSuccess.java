package com.jit.silly.hlnews;

/**
 * Created by moqiandemac on 2017/6/22.
 */

public class returnSuccess {
    int code;
    String msg;

    public returnSuccess(int code, String msg) {
        this.code = code;
        this.msg = msg;
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

