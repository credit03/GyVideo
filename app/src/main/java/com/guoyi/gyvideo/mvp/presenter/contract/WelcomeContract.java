package com.guoyi.gyvideo.mvp.presenter.contract;

import com.guoyi.gyvideo.mvp.BasePresenter;
import com.guoyi.gyvideo.mvp.BaseView;

import java.util.List;

/**
 * Created by Credit on 2017/3/20.
 */


public interface WelcomeContract {

    interface View extends BaseView<Presenter> {


        /**
         * 返回的数据集
         *
         * @param data
         */
        void showData(List<String> data);

        /**
         * 跳到到主界面
         */
        void goToMain();
    }

    interface Presenter<T> extends BasePresenter<T> {

        /**
         * 获取图片集
         */
        void getGalleryData();

    }
}
