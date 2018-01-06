package com.jit.silly.hlnews.Baoliao;

/**
 * Created by moqiandemac on 2017/6/25.
 */

public class returnCmts {
    int code;
    String msg;
    CmtInfo[] data;

    public returnCmts(int code, String msg, CmtInfo[] data) {
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

    public CmtInfo[] getData() {
        return data;
    }

    public void setData(CmtInfo[] data) {
        this.data = data;
    }
}
