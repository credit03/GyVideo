package com.guoyi.gyvideo.mvp.presenter;

import com.guoyi.gyvideo.mvp.RxPresenter;
import com.guoyi.gyvideo.mvp.presenter.contract.MainContract;
import com.guoyi.gyvideo.mvp.view.MainView;

/**
 * Created by Credit on 2017/3/21.
 */

public class MainPresenter extends RxPresenter<MainView> implements MainContract.Presenter<MainView> {


    public MainPresenter(MainView mView) {
        super(mView);
    }
}
