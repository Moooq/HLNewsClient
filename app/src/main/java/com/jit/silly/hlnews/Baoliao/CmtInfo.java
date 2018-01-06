package com.jit.silly.hlnews.Baoliao;

/**
 * Created by moqiandemac on 2017/6/25.
 */

public class CmtInfo {
    int id;
    String content;
    int baoliaoid;
    int sponsor;

    public CmtInfo(int id, String content, int baoliaoid, int sponsor) {
        this.id = id;
        this.content = content;
        this.baoliaoid = baoliaoid;
        this.sponsor = sponsor;
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

    public int getBaoliaoid() {
        return baoliaoid;
    }

    public void setBaoliaoid(int baoliaoid) {
        this.baoliaoid = baoliaoid;
    }

    public int getSponsor() {
        return sponsor;
    }

    public void setSponsor(int sponsor) {
        this.sponsor = sponsor;
    }
}
