package com.lesson.myahut.entity;

import java.io.Serializable;

/**
 * Created by qidunwei on 2016/3/16.
 */
public class PreScoreSerializable implements Serializable {
    private String uid;
    private String tid;
    private String drop_xn;
    private String drop_xq;

    public PreScoreSerializable(String uid,String tid,String drop_xn,String drop_xq){
        this.uid = uid;
        this.tid = tid;
        this.drop_xn = drop_xn;
        this.drop_xq = drop_xq;
    }

    public String getUid() {
        return uid;
    }

    public String getTid() {
        return tid;
    }

    public String getDrop_xn() {
        return drop_xn;
    }

    public String getDrop_xq() {
        return drop_xq;
    }
}
