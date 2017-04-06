package com.guoyi.gyvideo.ui.activity;

import android.os.Bundle;

import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.mvp.presenter.WelcomePresenter;
import com.guoyi.gyvideo.mvp.view.WelcomeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity<WelcomePresenter> {

    @BindView(R.id.welcome)
    WelcomeView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        mPresenter = new WelcomePresenter(welcome);
    }
}
