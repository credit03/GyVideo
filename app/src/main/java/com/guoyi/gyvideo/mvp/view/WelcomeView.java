package com.guoyi.gyvideo.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.mvp.presenter.contract.WelcomeContract;
import com.guoyi.gyvideo.ui.activity.MainActivity;
import com.guoyi.gyvideo.ui.activity.WelcomeActivity;
import com.guoyi.gyvideo.utils.Preconditions;

import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * Created by Credit on 2017/3/20.
 */

public class WelcomeView extends RootView<WelcomeContract.Presenter> implements WelcomeContract.View {
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    public WelcomeView(Context context) {
        super(context);
    }

    public WelcomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WelcomeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setPresenter(WelcomeContract.Presenter presenter) {
        mPresenter = Preconditions.checkNotNull(presenter);
    }

    @Override
    public void showLoadView(String msg) {

    }

    @Override
    public void showNoDataView() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showData(List<String> data) {
        logD(" 数据返回 " + data.toString());
        if (data != null) {
            int i = getRandomNumber(0, data.size() - 1);
            Glide.with(mContext).load(data.get(i)).into(ivBg);
            ivBg.animate().scaleX(1.12f).scaleY(1.12f).setDuration(3000).setStartDelay(100).start();
        }
    }

    public int getRandomNumber(int min, int max) {
        return new Random().nextInt(max) % (max - min + 1) + min;
    }

    @Override
    public void goToMain() {
        logD("跳转到Main");
        mContext.startActivity(new Intent(mContext, MainActivity.class));
        ((WelcomeActivity) mContext).finish();

    }

    @Override
    protected void getLayout() {
        this.inflate(mContext, R.layout.activity_welcome_view, this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

    }

}
