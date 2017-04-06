package com.guoyi.gyvideo.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.guoyi.gyvideo.adapter.viewholder.VideoListViewHolder;
import com.guoyi.gyvideo.bean.VideoType;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Description: 影片列表
 * Creator: yxc
 * date: 2016/9/30 11:10
 */
public class VideoListAdapter extends RecyclerArrayAdapter<VideoType> {

    public VideoListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoListViewHolder(parent);
    }

}
