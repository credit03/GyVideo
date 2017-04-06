package com.guoyi.gyvideo.mvp.presenter;

import android.support.annotation.NonNull;

import com.guoyi.gyvideo.MyApplication;
import com.guoyi.gyvideo.bean.TvBean;
import com.guoyi.gyvideo.mvp.RxPresenter;
import com.guoyi.gyvideo.mvp.mode.db.RealmHelper;
import com.guoyi.gyvideo.mvp.presenter.contract.WelcomeContract;
import com.guoyi.gyvideo.mvp.view.WelcomeView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Credit on 2017/3/20.
 */

public class WelcomePresenter extends RxPresenter<WelcomeView> implements WelcomeContract.Presenter<WelcomeView> {
    public static int TIME_GO_MAIN = 3200;

    public WelcomePresenter(@NonNull WelcomeView oneView) {
        super(oneView);
        getGalleryData();
    }

    @Override
    public void getGalleryData() {
        mView.showData(getImg());

        ResourceObserver<Long> observer = new ResourceObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                mView.goToMain();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
        boolean tvListCopy = RealmHelper.getInstance().isTvListCopy();
        addDisposable(Observable.create(new ObservableOnSubscribe<List<TvBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<TvBean>> e) throws Exception {
                MyApplication.logD("是否有电视节目了。。。" + tvListCopy);
                if (!tvListCopy) {
                    e.onNext(copyTv());
                } else {
                    e.onNext(new ArrayList<>());
                }
                e.onComplete();

            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<TvBean>, Boolean>() {
                    @Override
                    public Boolean apply(@io.reactivex.annotations.NonNull List<TvBean> tvBeen) throws Exception {
                        if (tvBeen.size() > 0) {
                            RealmHelper.getInstance().insertTvList(tvBeen);
                        }
                        return true;
                    }
                })
                .subscribeWith(new ResourceObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                        List<TvBean> tvList = RealmHelper.getInstance().getTvList();
                        List<TvBean> like = RealmHelper.getInstance().getTVlikeList("港");
                        MyApplication.logD("所有电视：" + tvList.toString());
                        MyApplication.logD("模糊电视：" + like.toString());
                        Observable.just(1l).delay(TIME_GO_MAIN, TimeUnit.MILLISECONDS).subscribeWith(observer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));

    }

    public List<String> getImg() {
        List<String> imgs = new ArrayList<>();
        imgs.add("file:///android_asset/a.jpg");
        imgs.add("file:///android_asset/b.jpg");
        imgs.add("file:///android_asset/c.jpg");
        imgs.add("file:///android_asset/d.jpg");
        imgs.add("file:///android_asset/e.jpg");

        imgs.add("file:///android_asset/f.jpg");
        imgs.add("file:///android_asset/g.jpg");
        return imgs;
    }

    private List<TvBean> copyTv() {
        List<TvBean> list = new ArrayList<>();
        try {
            InputStream inputStream = MyApplication.getInstance().getAssets().open("tvlist.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String s = "";

            // 读一行字符，不管换行符
            while ((s = in.readLine()) != null) {
                // 输出要自加换行符
                String[] split = s.split(",");
                if (split.length > 1) {
                    TvBean bean = new TvBean();
                    bean.name = split[0].trim();
                    bean.url = split[1].trim();
                    list.add(bean);
                }

            }

            MyApplication.logD("电视节目：" + list);
            //
            in.close();
            inputStream.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            MyApplication.logD("电视节目：" + e.getMessage());

        }

        return list;

    }


}
