package com.guoyi.gyvideo.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.guoyi.gyvideo.adapter.viewholder.TopIcViewHolder;
import com.guoyi.gyvideo.bean.VideoInfo;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Description: 专题
 * Creator: yxc
 * date: 2016/9/30 11:07
 */
public class TopIcAdapter extends RecyclerArrayAdapter<VideoInfo> {

    public TopIcAdapter(Context context) {
        super(context);
    }


    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopIcViewHolder(parent);
    }

}
