package com.guoyi.gyvideo.mvp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.guoyi.gyvideo.MyApplication;
import com.guoyi.gyvideo.mvp.BasePresenter;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description:
 * Creator: yxc
 * date: $date $time
 */
public abstract class RootView<T extends BasePresenter> extends LinearLayout {
    protected boolean mActive;//是否被销毁
    protected Context mContext;
    protected Unbinder unbinder = null;
    protected T mPresenter;

    public RootView(Context context) {
        super(context);
        init();
    }

    public RootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected abstract void getLayout();

    protected abstract void initView();

    protected abstract void initEvent();

    protected void init() {
        mContext = getContext();
        getLayout();
        unbinder = ButterKnife.bind(this);
        mActive = true;
        initView();
        initEvent();
    }


    public boolean ismActive() {
        return mActive;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mPresenter != null)
            mPresenter.attachView(this);
        mActive = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPresenter != null)
            mPresenter.detachView();
        mActive = false;
        if (unbinder != null) {
            unbinder.unbind();
        }
        unbinder = null;
        mContext = null;
    }

    /**
     * logcat--loge
     *
     * @param msg
     */
    public void logD(String msg) {
        if (MyApplication.DEBUG) {
            Logger.d(msg);
        }
    }

    /**
     * logcat--json
     *
     * @param msg
     */
    public static void logJSON(String msg) {
        if (MyApplication.DEBUG) {
            Logger.json(msg);
        }
    }

    /**
     * logcat--E
     *
     * @param msg
     */
    public static void logE(String msg) {
        if (MyApplication.DEBUG) {
            Logger.e(msg);
        }
    }
}
