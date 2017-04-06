package com.guoyi.gyvideo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.mvp.presenter.SwipeDeckPresenter;
import com.guoyi.gyvideo.mvp.view.SwipeDeckView;

import butterknife.BindView;

/**
 * Created by Credit on 2017/3/22.
 */

public class SwipeDeckFragment extends BaseFragment {
    @BindView(R.id.deckview)
    SwipeDeckView deckview;

    private SwipeDeckPresenter presenter;

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fargment_found, container, false);
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initValue(Bundle savedInstanceState) {
        presenter = new SwipeDeckPresenter(deckview);
    }

    @Override
    protected void loadData(String msg) {
        presenter.getData();
    }

    @Override
    public void onClick(View view) {

    }
}
