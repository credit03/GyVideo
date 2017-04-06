package com.guoyi.gyvideo.mvp;

/**
 * Created by Credit on 2017/3/20.
 */

public interface BasePresenter<T> {

    /**
     * 附加
     *
     * @param view
     */
    void attachView(T view);

    /**
     * 分离
     */
    void detachView();
}
