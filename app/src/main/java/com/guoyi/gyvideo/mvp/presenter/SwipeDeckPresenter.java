package com.guoyi.gyvideo.mvp.presenter;

import android.text.TextUtils;

import com.guoyi.gyvideo.MyApplication;
import com.guoyi.gyvideo.bean.VideoRes;
import com.guoyi.gyvideo.mvp.RxPresenter;
import com.guoyi.gyvideo.mvp.mode.net.RetrofitUtils;
import com.guoyi.gyvideo.mvp.mode.net.VideoApis;
import com.guoyi.gyvideo.mvp.mode.net.VideoHttpResponse;
import com.guoyi.gyvideo.mvp.presenter.contract.SwipeDeckContract;
import com.guoyi.gyvideo.mvp.view.SwipeDeckView;
import com.guoyi.gyvideo.utils.NetUtils;
import com.guoyi.gyvideo.utils.StringUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Credit on 2017/3/22.
 */

public class SwipeDeckPresenter extends RxPresenter<SwipeDeckView> implements SwipeDeckContract.Presenter<SwipeDeckView> {

    final String catalogId = "402834815584e463015584e53843000b";

    int max = 90;
    int min = 1;

    public SwipeDeckPresenter(SwipeDeckView mView) {
        super(mView);
    }

    @Override
    public void getNextData() {
        if (NetUtils.isNetworkConnected(MyApplication.getInstance())) {
            mView.showLoadView("");
            VideoApis apis = RetrofitUtils.get().create(VideoApis.class);
            addDisposable(apis.getVideoList(catalogId, getNextPage() + "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getObserver()));
        } else {
            mView.showError("获取失败，请检查网络");
        }

    }

    @Override
    public void getData() {
        getNextData();
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

    private int getNextPage() {
        int page = mView.getLastPage();
        if (NetUtils.isNetworkConnected(MyApplication.getInstance())) {
            page = StringUtils.getRandomNumber(min, max);
            mView.setLastPage(page);
        }
        return page;
    }
}
