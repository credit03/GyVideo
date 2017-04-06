package com.guoyi.gyvideo.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.guoyi.gyvideo.adapter.viewholder.MineHistoryVideoListViewHolder;
import com.guoyi.gyvideo.bean.VideoType;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class MineHistoryVideoListAdapter extends RecyclerArrayAdapter<VideoType> {

    public MineHistoryVideoListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MineHistoryVideoListViewHolder(parent);
    }

}
