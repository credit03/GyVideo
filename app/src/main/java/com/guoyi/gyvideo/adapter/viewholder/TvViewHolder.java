package com.guoyi.gyvideo.adapter.viewholder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.bean.TvBean;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Credit on 2017/3/24.
 */

public class TvViewHolder extends BaseViewHolder<TvBean> {
    public TextView name;

    public TvViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_tvlist);
        name = (TextView) itemView.findViewById(R.id.name);
    }

    @Override
    public void setData(TvBean data) {
        super.setData(data);
        name.setText(data.name);
    }
}
