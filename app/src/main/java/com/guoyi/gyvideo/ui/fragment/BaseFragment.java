package com.guoyi.gyvideo.ui.fragment;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.guoyi.gyvideo.Constants;
import com.guoyi.gyvideo.MyApplication;
import com.guoyi.gyvideo.bean.EventBean;
import com.guoyi.gyvideo.ui.activity.MainActivity;
import com.guoyi.gyvideo.utils.rxbus2.RxBusBuilder;
import com.guoyi.gyvideo.widget.theme.ColorUiUtil;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * Created by Credit on 2016/12/12.
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {
    protected Context mContext;
    protected View rootView;

    protected Unbinder unbinder;


    protected Activity parentActivity;
    private Disposable disposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /**
         * activity+fragment多次切换出现页面空白问题,
         * 问题表现在，第一个fragment加载数据缓慢
         * 在未加载完全的时候切换其他页面的时候出现空白，
         * 在网上找了各种方法 刚开始以为缓存方面没做好(确实还没来得及做缓存那块的优化)，
         * 后来看各种帖子，设置在清单文件设至signTask,  ...终于意识到fragment状态没保存。。。
         */
        if (rootView == null) {
            rootView = setContentView(inflater, container);
            mContext = getActivity();
            parentActivity = (MainActivity) getActivity();
            unbinder = ButterKnife.bind(this, rootView);
            initView(rootView);
            initValue(savedInstanceState);
            initEvent();
        } else {
            log("初始化.... this.rootView != null" + this.getClass().getSimpleName());
            ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
            if (localViewGroup != null) {
                localViewGroup.removeView(this.rootView);
            }
        }
        return rootView;
    }

    /**
     * 获取根view
     *
     * @return
     */
    public View getRootView() {
        return rootView;
    }

    /**
     * 设置布局文件
     *
     * @param inflater
     * @param container
     * @return
     */
    protected abstract View setContentView(LayoutInflater inflater, ViewGroup container);

    /**
     * 初始化View
     *
     * @param rootView
     */
    protected abstract void initView(View rootView);

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

    /**
     * 初始化值
     *
     * @param savedInstanceState
     */
    protected abstract void initValue(Bundle savedInstanceState);

    @Override
    public void onResume() {
        super.onResume();
        if (comLastLoadTime(5)) {
            loadData("");
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        disposable = RxBusBuilder.create(EventBean.class).withKey(Constants.THEME_CHANGE)
                .subscribe(con -> {
                    changeTheme();
                });
    }


    /**
     * 改变主题
     */
    private void changeTheme() {
        final View rootView = getActivity().getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache(true);

        final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        if (null != localBitmap && rootView instanceof ViewGroup) {
            final View tmpView = new View(getContext());
            tmpView.setBackgroundDrawable(new BitmapDrawable(getResources(), localBitmap));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) rootView).addView(tmpView, params);
            tmpView.animate().alpha(0).setDuration(400).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    ColorUiUtil.changeTheme(rootView, getContext().getTheme());
                    System.gc();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ((ViewGroup) rootView).removeView(tmpView);
                    localBitmap.recycle();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }
    }

    public long lastime = 0;

    /**
     * 当前时间与上一次加载时间比较，是否大于3分钟
     *
     * @return
     */
    public boolean comLastLoadTime() {
        return comLastLoadTime(3);

    }

    /**
     * 当前时间与上一次加载时间比较，是否大于min分钟
     *
     * @return
     */
    public boolean comLastLoadTime(int min) {
        if (System.currentTimeMillis() - lastime > 1000 * 60 * min) {
            lastime = System.currentTimeMillis();
            return true;
        }
        return false;

    }


    /**
     * 加载数据
     *
     * @param msg
     */
    protected abstract void loadData(String msg);

    /**
     * 回收资源，在Activity--OnDestroy调用
     */
    public void destroyData() {

        if (disposable != null) {
            disposable.dispose();
        }
        if (unbinder != null) {
            unbinder.unbind();
        }
        disposable = null;
        unbinder = null;
    }

    /***
     * 测试LOG
     */
    public void log(String msg) {
        if (MyApplication.DEBUG) {
            Logger.d(msg);
        }
    }


}
