package com.jit.silly.hlnews.personal;

import java.io.Serializable;

/**
 * Created by lenovoo on 2017/6/18.
 */

public class UserInfo implements Serializable {
    private String username;
    private String nickname;
    private int id;
    private String birthday;
    private String password;
    private String imageUrl;
    private String introduction;
    private int sex;

    public UserInfo(String username, String nickname, int id, String birthday, String password, String imageUrl, String introduction, int sex) {
        this.username = username;
        this.nickname = nickname;
        this.id = id;
        this.birthday = birthday;
        this.password = password;
        this.imageUrl = imageUrl;
        this.introduction = introduction;
        this.sex = sex;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int isSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
