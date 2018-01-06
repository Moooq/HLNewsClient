package com.jit.silly.hlnews.Baoliao;

/**
 * Created by moqiandemac on 2017/6/20.
 */

public class BaoInfo {
    int id;
    String content;
    String title;
    String editorid;
    String picurl1;
    String picurl2;
    String picurl3;

    public BaoInfo(int id, String content, String title, String editorid, String picurl1, String picurl2, String picurl3) {
        this.id = id;
        this.content = content;
        this.title = title;
        this.editorid = editorid;
        this.picurl1 = picurl1;
        this.picurl2 = picurl2;
        this.picurl3 = picurl3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEditorid() {
        return editorid;
    }

    public void setEditorid(String editorid) {
        this.editorid = editorid;
    }

    public String getPicurl1() {
        return picurl1;
    }

    public void setPicurl1(String picurl1) {
        this.picurl1 = picurl1;
    }

    public String getPicurl2() {
        return picurl2;
    }

    public void setPicurl2(String picurl2) {
        this.picurl2 = picurl2;
    }

    public String getPicurl3() {
        return picurl3;
    }

    public void setPicurl3(String picurl3) {
        this.picurl3 = picurl3;
    }
}
