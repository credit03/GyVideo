package com.guoyi.gyvideo.adapter.viewholder;

import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.bean.VideoType;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;


/**
 * Created by cp on 16/6/2.
 */

public class MineHistoryVideoListViewHolder extends BaseViewHolder<VideoType> {
    ImageView imgPicture;
    TextView tv_title;

    public MineHistoryVideoListViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_mine_history);
        imgPicture = $(R.id.img_video);
        tv_title = $(R.id.tv_title);
        imgPicture.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public void setData(VideoType data) {
        tv_title.setText(data.title);
        ViewGroup.LayoutParams params = imgPicture.getLayoutParams();

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels / 3;//宽度为屏幕宽度一半
//        int height = data.getHeight()*width/data.getWidth();//计算View的高度

        params.height = width;
        imgPicture.setLayoutParams(params);
        Glide.with(getContext()).load(data.pic).into(imgPicture);
    }
}
