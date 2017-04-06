package com.guoyi.gyvideo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guoyi.gyvideo.Constants;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.bean.EventBean;
import com.guoyi.gyvideo.mvp.presenter.RecommendPresenter;
import com.guoyi.gyvideo.mvp.view.RecommendView;

import butterknife.BindView;

/**
 * Created by Credit on 2017/3/21.
 */

public class RecommendFragment extends BaseFragment {
    @BindView(R.id.recommend)
    RecommendView recommend;

    private RecommendPresenter presenter;

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_recommend, container, false);
    }

    @Override
    protected void initView(View rootView) {
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initValue(Bundle savedInstanceState) {
        presenter = new RecommendPresenter(recommend);
    }

    @Override
    protected void loadData(String msg) {

        /**
         * 使用RxBus通知获取更新
         */
        EventBean bean = new EventBean(Constants.EVENT_RECOMMEND_REFRESH);
        bean.sendRxBus();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBean bean = new EventBean(Constants.EVENT_RECOMMEND);
        bean.op = true;
        bean.sendRxBus();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBean bean = new EventBean(Constants.EVENT_RECOMMEND);
        bean.op = false;
        bean.sendRxBus();
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    public void onClick(View view) {

    }


}
