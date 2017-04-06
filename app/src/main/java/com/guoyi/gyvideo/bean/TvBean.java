package com.guoyi.gyvideo.bean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Credit on 2017/3/24.
 */

public class TvBean extends RealmObject implements Serializable{
    public String name;
    public String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "TvBean{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
