package com.guoyi.gyvideo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description: 影片详情
 * Creator: yxc
 * date: 2016/9/29 9:39
 */
public class VideoInfo implements Parcelable {
    public String title;
    public String pic;
    public String dataId;
    public String score;
    public String airTime;
    public String moreURL;
    public String loadType;
    //    public String description;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.pic);
        dest.writeString(this.dataId);
        dest.writeString(this.score);
        dest.writeString(this.airTime);
        dest.writeString(this.moreURL);
        dest.writeString(this.loadType);
    }

    public VideoInfo() {
    }

    protected VideoInfo(Parcel in) {
        this.title = in.readString();
        this.pic = in.readString();
        this.dataId = in.readString();
        this.score = in.readString();
        this.airTime = in.readString();
        this.moreURL = in.readString();
        this.loadType = in.readString();
    }

    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel source) {
            return new VideoInfo(source);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };
}
