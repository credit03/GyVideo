package com.guoyi.gyvideo.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class AboutBean implements Serializable {

    public String name;

    public String owner;

    @SerializedName("link")
    public String url;

    @SerializedName("des")
    public String description;
    public String meta;

    public String language;

    public String path;

    public List<User> contributors;

    @Override
    public boolean equals(Object o) {
        if (o instanceof AboutBean) {
            return name.equals(((AboutBean) o).name) && owner.equals(((AboutBean) o).owner);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + owner.hashCode();
    }


    @Override
    public String toString() {
        return "GithubResponse{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", meta='" + meta + '\'' +
                ", language='" + language + '\'' +
                ", contributors=" + contributors +
                '}';
    }

    public static class User implements Serializable {

        public String name;

        public String avatar;
    }
}
