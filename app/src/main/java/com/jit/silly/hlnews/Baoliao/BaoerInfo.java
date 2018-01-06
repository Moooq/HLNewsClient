package com.jit.silly.hlnews.Baoliao;

/**
 * Created by moqiandemac on 2017/6/23.
 */

public class BaoerInfo {
    int id;
    String username;
    String nickname;
    String imageUrl;

    public BaoerInfo(int id, String username, String nickname, String imageUrl) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
