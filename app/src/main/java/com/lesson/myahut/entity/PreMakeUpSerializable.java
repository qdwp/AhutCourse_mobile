package com.lesson.myahut.entity;

import java.io.Serializable;

/**
 * Created by qidunwei on 2016/3/20.
 */
public class PreMakeUpSerializable implements Serializable {

    private String uid;
    private String pwd;
    private String xnd;
    private String xqd;

    public PreMakeUpSerializable(String uid, String pwd, String xnd, String xqd){
        this.uid = uid;
        this.pwd = pwd;
        this.xnd = xnd;
        this.xqd = xqd;
    }

    public String getUid() {
        return uid;
    }

    public String getXnd() {
        return xnd;
    }

    public String getPwd() {
        return pwd;
    }

    public String getXqd() {
        return xqd;
    }
}
