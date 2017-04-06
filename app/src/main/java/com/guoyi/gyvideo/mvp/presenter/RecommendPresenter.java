package com.guoyi.gyvideo.mvp.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.guoyi.gyvideo.MyApplication;
import com.guoyi.gyvideo.bean.VideoRes;
import com.guoyi.gyvideo.mvp.RxPresenter;
import com.guoyi.gyvideo.mvp.mode.net.RetrofitUtils;
import com.guoyi.gyvideo.mvp.mode.net.VideoApis;
import com.guoyi.gyvideo.mvp.mode.net.VideoHttpResponse;
import com.guoyi.gyvideo.mvp.presenter.contract.RecommendContract;
import com.guoyi.gyvideo.mvp.view.RecommendView;
import com.guoyi.gyvideo.utils.NetUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Credit on 2017/3/21.
 */

public class RecommendPresenter extends RxPresenter<RecommendView> implements RecommendContract.Presenter<RecommendView> {


    public RecommendPresenter(@NonNull RecommendView recommend) {
        super(recommend);
        /**
         * 已经使用RxBus通知View去刷新获取数据
         */
        //loadData();
    }


    @Override
    public void loadData() {
        if (NetUtils.isNetworkConnected(MyApplication.getInstance())) {
            mView.showLoadView("正在加载");
            VideoApis videoApis = RetrofitUtils.get().create(VideoApis.class);
            addDisposable(videoApis.getHomePage().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getObserver()));
        } else {
            mView.showError("获取失败，请检查网络");
        }

    }

    public ResourceObserver<VideoHttpResponse<VideoRes>> getObserver() {
        return new ResourceObserver<VideoHttpResponse<VideoRes>>() {
            @Override
            public void onNext(VideoHttpResponse<VideoRes> videoResVideoHttpResponse) {
                if (videoResVideoHttpResponse.getCode() == 200) {
                    VideoRes ret = videoResVideoHttpResponse.getRet();
                    if (mView.ismActive()) {
                        if (ret != null) {
                            mView.showData(ret);
                        } else {
                            mView.showNoDataView();
                        }
                    }

                } else {
                    if (TextUtils.isEmpty(videoResVideoHttpResponse.getMsg())) {
                        mView.showError("服务器返回: ERROR");
                    } else {
                        mView.showError(videoResVideoHttpResponse.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                mView.showError(t.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };

    }
}
