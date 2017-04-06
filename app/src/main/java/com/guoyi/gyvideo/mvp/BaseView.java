package com.guoyi.gyvideo.mvp;

/**
 * Created by Credit on 2017/3/20.
 */

public interface BaseView<T> {

    public void setPresenter(T presenter);
    /**
     * 是否加载
     *
     * @param msg
     */
    void showLoadView(String msg);

    /**
     * 显示没有数据
     */
    void showNoDataView();

    /**
     * 显示出错
     *
     * @param msg
     */
    void showError(String msg);
}
