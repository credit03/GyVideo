package com.guoyi.gyvideo.adapter.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.bean.GankItemBean;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Credit on 2017/3/23.
 */

public class WelfareViewHolder extends BaseViewHolder<GankItemBean> {

    ImageView image;

    public WelfareViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_welfare);
        image = (ImageView) itemView.findViewById(R.id.image);
    }

    @Override
    public void setData(GankItemBean data) {
        ViewGroup.LayoutParams params = image.getLayoutParams();
        params.height = data.getHeight();
        image.setLayoutParams(params);

        Glide.with(getContext()).load(data.getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(image);
    }
}
