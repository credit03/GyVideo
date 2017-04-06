package com.guoyi.gyvideo.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.guoyi.gyvideo.adapter.viewholder.RelatedViewHolder;
import com.guoyi.gyvideo.bean.VideoInfo;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Description: 推荐
 * Creator: yxc
 * date: 2016/9/30 11:10
 */
public class RelatedAdapter extends RecyclerArrayAdapter<VideoInfo> {

    public RelatedAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new RelatedViewHolder(parent);
    }

}
