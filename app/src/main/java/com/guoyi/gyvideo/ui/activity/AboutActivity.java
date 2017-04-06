package com.guoyi.gyvideo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.method.LinkMovementMethod;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guoyi.gyvideo.Constants;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.adapter.viewholder.AboutViewHolder;
import com.guoyi.gyvideo.bean.AboutBean;
import com.guoyi.gyvideo.utils.UrlUtils;
import com.guoyi.gyvideo.widget.theme.ColorTextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关于我们
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.title_name)
    ColorTextView titleName;
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.recylerView)
    EasyRecyclerView recylerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        titleName.setText("关于我们");
        title.setMovementMethod(LinkMovementMethod.getInstance());
        //格式化字符串
        title.setText(UrlUtils.formatUrlString(getString(R.string.about_title)));

        description.setText(UrlUtils.formatUrlString(String.format(getString(R.string.about_desc))));

        recylerView.setLayoutManager(new LinearLayoutManager(this));
        AboutAdaper adaper = new AboutAdaper(this);
        List<AboutBean> responses = new ArrayList<>();
        responses.add(Constants.my);
        responses.add(Constants.response);
        adaper.addAll(responses);
        recylerView.setAdapter(adaper);
        rlBack.setOnClickListener(v -> finish());

        adaper.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                AboutBean item = adaper.getItem(position);
                Uri uri = Uri.parse(item.url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    private class AboutAdaper extends RecyclerArrayAdapter<AboutBean> {

        public AboutAdaper(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new AboutViewHolder(parent);
        }
    }
}
