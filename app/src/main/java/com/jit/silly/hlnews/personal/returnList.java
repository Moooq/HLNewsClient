package com.jit.silly.hlnews.personal;

import java.io.Serializable;

/**
 * Created by lenovoo on 2017/6/18.
 */

public class returnList implements Serializable{
    private int code;
    private String msg;
    private UserInfo data;

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

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
