package com.guoyi.gyvideo.adapter.viewholder;

import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.bean.AboutBean;
import com.guoyi.gyvideo.widget.AsyncImageView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Credit on 2017/3/16.
 */

public class AboutViewHolder extends BaseViewHolder<AboutBean> {

    @BindView(R.id.cardView)
    CardView cardView;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.meta)
    TextView meta;
    @BindView(R.id.built_by)
    TextView builtBy;
    @BindView(R.id.avatar1)
    AsyncImageView avatar1;
    @BindView(R.id.avatar2)
    AsyncImageView avatar2;
    @BindView(R.id.avatar3)
    AsyncImageView avatar3;
    @BindView(R.id.avatar4)
    AsyncImageView avatar4;
    @BindView(R.id.avatar5)
    AsyncImageView avatar5;
    @BindView(R.id.contributors_line)
    LinearLayout contributorsLine;
    @BindView(R.id.star)
    ImageView star;
    @BindView(R.id.label)
    TextView label;

    private List<AsyncImageView> avatars;

    public AboutViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_github_view);
        ButterKnife.bind(this, itemView);
        avatars = new ArrayList<>();
        avatars.add(avatar1);
        avatars.add(avatar2);
        avatars.add(avatar3);
        avatars.add(avatar4);
        avatars.add(avatar5);
    }


    @Override
    public void setData(AboutBean mdata) {
        super.setData(mdata);
        if (mdata != null) {
            title.setText(mdata.owner + "/" + mdata.name);

            if (TextUtils.isEmpty(mdata.description)) {
                description.setVisibility(View.GONE);
            } else {
                description.setVisibility(View.VISIBLE);
                description.setText(mdata.description);
            }


            if (TextUtils.isEmpty(mdata.meta)) {
                meta.setVisibility(View.GONE);
            } else {
                meta.setText(mdata.meta);
                meta.setVisibility(View.VISIBLE);
            }

            label.setText("JAVA");
            label.setVisibility(View.VISIBLE);

            for (int i = 0; i < avatars.size(); i++) {
                if (mdata.contributors != null && i < mdata.contributors.size()) {
                    avatars.get(i).loadImage(mdata.contributors.get(i).avatar);
                    avatars.get(i).setVisibility(View.VISIBLE);
                } else {
                    avatars.get(i).setVisibility(View.GONE);
                }
            }

        }
    }


}
