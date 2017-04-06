package com.guoyi.gyvideo.mvp.view;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.adapter.ViewPageFragmentAdapter;
import com.guoyi.gyvideo.mvp.presenter.contract.MainContract;
import com.guoyi.gyvideo.ui.activity.MainActivity;
import com.guoyi.gyvideo.ui.fragment.BaseFragment;
import com.guoyi.gyvideo.ui.fragment.MineFragment;
import com.guoyi.gyvideo.ui.fragment.RecommendFragment;
import com.guoyi.gyvideo.ui.fragment.SwipeDeckFragment;
import com.guoyi.gyvideo.ui.fragment.TopIcFragment;
import com.guoyi.gyvideo.utils.Preconditions;
import com.guoyi.gyvideo.widget.UnScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Credit on 2017/3/21.
 */

public class MainView extends RootView<MainContract.Presenter> implements MainContract.View, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.view_pager)
    UnScrollViewPager viewPager;
    @BindView(R.id.tab_rg_menu)
    RadioGroup tabRgMenu;

    private ArrayList<BaseFragment> fragments;
    private ViewPageFragmentAdapter<BaseFragment> fragmentAdapter;
    private MainActivity activity;

    public MainView(Context context) {
        super(context);
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = Preconditions.checkNotNull(presenter);
    }


    @Override
    protected void getLayout() {
        inflate(mContext, R.layout.activity_main_view, this);
    }

    @Override
    protected void initView() {
        activity = (MainActivity) mContext;
        fragments = new ArrayList<>();
        fragments.add(new RecommendFragment());
        fragments.add(new TopIcFragment());
        fragments.add(new SwipeDeckFragment());
        fragments.add(new MineFragment());
        fragmentAdapter = new ViewPageFragmentAdapter<>(activity.getSupportFragmentManager());
        fragmentAdapter.addFragments(fragments);
        viewPager.setAdapter(fragmentAdapter);
        /**
         *一定要设置这项，viewPager默认懒加载两个页面数据，即只保存两个页面的状态，当第三个页面进入时，回收第一个页面;
         * 设置Viewpager保存fragments.size()个状态
         */
        viewPager.setOffscreenPageLimit(fragments.size());
    }

    @Override
    protected void initEvent() {
        tabRgMenu.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

        switch (i) {
            case R.id.tab_rb_1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tab_rb_2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.tab_rb_3:
                viewPager.setCurrentItem(2);
                break;
            case R.id.tab_rb_4:
                viewPager.setCurrentItem(3);
                //  viewPager.setCurrentItem(0);
                break;
        }

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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (fragments != null) {
            logD("回收 fragments");
            for (BaseFragment b : fragments) {
                b.destroyData();
            }
        }
        fragments = null;
    }
}
