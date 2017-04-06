package com.guoyi.gyvideo.utils;


import com.guoyi.gyvideo.bean.Collection;
import com.guoyi.gyvideo.bean.Record;
import com.guoyi.gyvideo.bean.VideoInfo;
import com.guoyi.gyvideo.bean.VideoRes;
import com.guoyi.gyvideo.bean.VideoType;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: BeanUtil
 * Creator: yxc
 * date: 2016/9/21 14:39
 */
public class BeanUtil {

    public static List<VideoType> RecordToVideoInfo(List<Record> records) {

        if (records == null) {
            return null;
        }
        List<VideoType> types = new ArrayList<>();
        for (Record record : records) {
            VideoType info = new VideoType();
            info.dataId = record.getId();
            info.pic = record.getPic();
            info.title = record.getTitle();
            types.add(info);
        }
        return types;
    }

    public static List<VideoType> FavortToVideoInfo(List<Collection> records) {
        if (records == null) {
            return null;
        }
        List<VideoType> types = new ArrayList<>();
        for (Collection record : records) {
            VideoType info = new VideoType();
            info.dataId = record.getId();
            info.pic = record.getPic();
            info.title = record.getTitle();
            info.score = record.getScore();
            info.airTime = record.getAirTime();
            types.add(info);
        }
        return types;
    }

    public static VideoInfo VideoType2VideoInfo(VideoType videoType, VideoInfo videoInfo) {
        if (videoInfo == null)
            videoInfo = new VideoInfo();
        videoInfo.title = videoType.title;
        videoInfo.dataId = videoType.dataId;
        videoInfo.pic = videoType.pic;
        videoInfo.airTime = videoType.airTime;
        videoInfo.score = videoType.score;
        return videoInfo;
    }

    public static VideoRes VideoInfo2VideoRes(VideoInfo videoInfo, VideoRes videoRes) {
        if (videoRes == null)
            videoRes = new VideoRes();
        videoRes.title = StringUtils.isEmpty(videoInfo.title);
        videoRes.pic = StringUtils.isEmpty(videoInfo.pic);
        videoRes.score = StringUtils.isEmpty(videoInfo.score);
        videoRes.airTime = StringUtils.isEmpty(videoInfo.airTime);
        videoRes.pic = StringUtils.isEmpty(videoInfo.pic);
        return videoRes;
    }
}
