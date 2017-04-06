package com.guoyi.gyvideo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.mvp.presenter.MinePresenter;
import com.guoyi.gyvideo.mvp.view.MineView;

import butterknife.BindView;

/**
 * Created by Credit on 2017/3/23.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.mineview)
    MineView mineview;

    private MinePresenter presenter;

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initValue(Bundle savedInstanceState) {
        presenter = new MinePresenter(mineview);
    }

    @Override
    protected void loadData(String msg) {
        presenter.getHistoryData();
    }

    @Override
    public void onClick(View view) {

    }


}
