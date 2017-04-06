package com.guoyi.gyvideo.mvp.presenter.contract;

import com.guoyi.gyvideo.bean.VideoType;
import com.guoyi.gyvideo.mvp.BasePresenter;
import com.guoyi.gyvideo.mvp.BaseView;

import java.util.List;

/**
 * Created by Credit on 2017/3/23.
 */

public interface MineContract {

    interface Presenter<T> extends BasePresenter<T> {
        void getHistoryData();
    }

    interface View extends BaseView<Presenter> {

        void showData(List<VideoType> data);
    }
}
