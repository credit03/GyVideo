package com.guoyi.gyvideo.mvp.presenter.contract;

import com.guoyi.gyvideo.bean.VideoRes;
import com.guoyi.gyvideo.mvp.BasePresenter;
import com.guoyi.gyvideo.mvp.BaseView;

/**
 * Created by Credit on 2017/3/22.
 */

public interface SwipeDeckContract {

    interface Presenter<T> extends BasePresenter<T> {
        void getNextData();

        void getData();
    }

    interface View extends BaseView<Presenter> {

        void showData(VideoRes res);

        int getLastPage();

        void setLastPage(int page);

    }
}
