package com.guoyi.gyvideo.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.guoyi.gyvideo.adapter.viewholder.TvViewHolder;
import com.guoyi.gyvideo.bean.TvBean;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;


/**
 * Created by Credit on 2017/3/24.
 */

public class TvListAdapter extends RecyclerArrayAdapter<TvBean> {
    public TvListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new TvViewHolder(parent);
    }
}
