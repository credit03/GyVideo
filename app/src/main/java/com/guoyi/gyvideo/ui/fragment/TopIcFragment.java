package com.guoyi.gyvideo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guoyi.gyvideo.Constants;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.bean.EventBean;
import com.guoyi.gyvideo.mvp.presenter.TopIcPresenter;
import com.guoyi.gyvideo.mvp.view.TopIcView;

import butterknife.BindView;

/**
 * Created by Credit on 2017/3/22.
 * 专题Fragment
 */

public class TopIcFragment extends BaseFragment {
    @BindView(R.id.topic)
    TopIcView topic;

    private TopIcPresenter presenter;

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_topic, container, false);
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initValue(Bundle savedInstanceState) {
        presenter = new TopIcPresenter(topic);
    }

    @Override
    protected void loadData(String msg) {
        EventBean bean = new EventBean(Constants.EVENT_TOPIC_REFRESH);
        bean.sendRxBus();
    }

    @Override
    public void onClick(View view) {

    }
}
