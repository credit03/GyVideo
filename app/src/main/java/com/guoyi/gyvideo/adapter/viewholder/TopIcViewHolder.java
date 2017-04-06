package com.guoyi.gyvideo.adapter.viewholder;

import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.bean.VideoInfo;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Description: 专题
 * Creator: yxc
 * date: 2016/9/30 11:06
 */

public class TopIcViewHolder extends BaseViewHolder<VideoInfo> {
    ImageView imgPicture;
    TextView tv_title;

    public TopIcViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_found);
        imgPicture = $(R.id.img_video);
        tv_title = $(R.id.tv_title);
        imgPicture.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public void setData(VideoInfo data) {
        tv_title.setText(data.title);
        ViewGroup.LayoutParams params = imgPicture.getLayoutParams();

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels / 2;//宽度为屏幕宽度一半
//        int height = data.getHeight()*width/data.getWidth();//计算View的高度

        params.height = (int) (width / 1.8);
        imgPicture.setLayoutParams(params);
        Glide.with(getContext()).load(data.pic).into(imgPicture);
        // ImageLoader.load(getContext(),data.pic,imgPicture);
    }
}
