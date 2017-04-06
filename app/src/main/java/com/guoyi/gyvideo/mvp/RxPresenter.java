package com.guoyi.gyvideo.mvp;

import com.guoyi.gyvideo.utils.Preconditions;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Credit on 2017/3/20.
 */

public class RxPresenter<T extends BaseView> implements BasePresenter<T> {

    protected CompositeDisposable compositeDisposable;

    protected T mView;

    public RxPresenter(T mView) {
        this.mView = Preconditions.checkNotNull(mView);
        this.mView.setPresenter(this);
    }

    /**
     * 添加开关
     * <p>
     * 要使用subscribeWith而不是subscribe， subscribeWith返回不当前关联disposable， 因为subscribe方法现在返回void
     *
     * @param disposable
     */
    protected void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(disposable);
    }

    /**
     * 关闭RX桥梁
     */
    protected void closeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }


    @Override
    public void attachView(T view) {
        this.mView = view;

    }

    @Override
    public void detachView() {
        this.mView = null;
        closeDisposable();
    }


}
