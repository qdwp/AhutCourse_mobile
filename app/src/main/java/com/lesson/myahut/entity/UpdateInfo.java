package com.lesson.myahut.entity;

/**
 * Created by qidunwei on 2016/3/21.
 */
public class UpdateInfo {
    private static UpdateInfo updateInfo;
    private String version;
    private String url;

    private UpdateInfo() {

    }

    public static UpdateInfo getInstance() {
        if (updateInfo == null) {
            updateInfo = new UpdateInfo();
        }
        return updateInfo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
