package com.guoyi.gyvideo.mvp.presenter.contract;

import com.guoyi.gyvideo.mvp.BasePresenter;
import com.guoyi.gyvideo.mvp.BaseView;

/**
 * Created by Credit on 2017/3/21.
 */

public interface MainContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter<T> extends BasePresenter<T> {

    }
}
