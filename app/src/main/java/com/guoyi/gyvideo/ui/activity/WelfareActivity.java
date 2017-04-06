package com.guoyi.gyvideo.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.adapter.WelfareAdapter;
import com.guoyi.gyvideo.bean.GankHttpResponse;
import com.guoyi.gyvideo.bean.GankItemBean;
import com.guoyi.gyvideo.mvp.mode.net.GankApis;
import com.guoyi.gyvideo.mvp.mode.net.RetrofitUtils;
import com.guoyi.gyvideo.utils.AttrsHelper;
import com.guoyi.gyvideo.utils.DateUtils;
import com.guoyi.gyvideo.utils.DensityUtil;
import com.guoyi.gyvideo.utils.NetUtils;
import com.guoyi.gyvideo.utils.StringUtils;
import com.guoyi.gyvideo.widget.theme.ColorRelativeLayout;
import com.guoyi.gyvideo.widget.theme.ColorTextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;

public class WelfareActivity extends BaseActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.title_name)
    ColorTextView titleName;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;
    @BindView(R.id.rl_collect)
    RelativeLayout rlCollect;
    @BindView(R.id.rl_collect_clear)
    RelativeLayout rlCollectClear;
    @BindView(R.id.title)
    ColorRelativeLayout title;
    @BindView(R.id.recylerView)
    EasyRecyclerView recylerView;

    private int pageIndex = 1;
    private int pageSize = 16;
    private WelfareAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare);
        ButterKnife.bind(this);
        initValue();
        initEvent();
        recylerView.showProgress();
        onRefresh();
    }


    private void initValue() {
        recylerView.setAdapter(adapter = new WelfareAdapter(this));
        recylerView.setErrorView(R.layout.view_error);
        recylerView.setRefreshListener(this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setMore(R.layout.view_more, this);
        recylerView.setRefreshingColor(AttrsHelper.getColor(this, R.attr.colorPrimary), AttrsHelper.getColor(this, R.attr.colorPrimaryLight));

        titleName.setText("福利");

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //  manager.setSpanSizeLookup(adapter.obtainGridSpanSizeLookUp(2));
        recylerView.setLayoutManager(manager);

        SpaceDecoration decoration = new SpaceDecoration(DensityUtil.dip2px(this, 8));
        decoration.setPaddingHeaderFooter(false);
        decoration.setPaddingStart(true);
        decoration.setPaddingEdgeSide(true);
        recylerView.addItemDecoration(decoration);
    }


    private void initEvent() {
        recylerView.getErrorView().setOnClickListener(r -> {
            recylerView.showProgress();
            onRefresh();
        });

        /**
         * 加载更多出错
         */
        adapter.setError(R.layout.view_error_footer).setOnClickListener(v -> {
            //重新加载
            adapter.resumeMore();
        });

        rlBack.setOnClickListener(view -> {
            finish();
        });

        titleName.setOnClickListener(view -> {
            if (DateUtils.isDoublePress(view)) {
                recylerView.scrollToPosition(0);
            }

        });

        adapter.setOnItemClickListener(position -> {
            ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(DensityUtil.getScreenWidth(WelfareActivity.this), DensityUtil.getScreenHeight(WelfareActivity.this));
            ImagePagerActivity.startImagePagerActivity(WelfareActivity.this, adapter.getAllData(), position, imageSize);
        });

    }


    @Override
    public void onRefresh() {
        if (NetUtils.isNetworkConnected(this)) {
            pageIndex = 1;
            GankApis apis = RetrofitUtils.getWelfare().create(GankApis.class);

            addDisposable(apis.getGirlList(pageSize, pageIndex)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getObserver(1)));
        } else {
            this.recylerView.showRecycler();
            if (adapter.getCount() == 0) {
                this.recylerView.showError();
            }
            Toast.makeText(this, "获取失败，请检查网络", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoadMore() {
        if (NetUtils.isNetworkConnected(this)) {
            pageIndex++;
            GankApis apis = RetrofitUtils.getWelfare().create(GankApis.class);
            addDisposable(apis.getGirlList(pageSize, pageIndex)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getObserver(2)));
        } else {
            this.recylerView.showRecycler();
            adapter.pauseMore();
            Toast.makeText(this, "获取失败，请检查网络", Toast.LENGTH_SHORT).show();
        }

    }


    public ResourceObserver<GankHttpResponse<List<GankItemBean>>> getObserver(final int flags) {
        return new ResourceObserver<GankHttpResponse<List<GankItemBean>>>() {
            @Override
            public void onNext(GankHttpResponse<List<GankItemBean>> result) {
                if (!result.getError()) {
                    if (flags == 1) {
                        adapter.clear();
                    }
                    List<GankItemBean> results = result.getResults();
                    if (results != null && results.size() > 0) {
                        setHeight(results);
                        adapter.addAll(results);
                        logD("返回数据：" + results);
                    } else {
                        recylerView.showRecycler();
                    }
                } else {
                    Toast.makeText(WelfareActivity.this, "服务返回出错:", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(WelfareActivity.this, "加载失败，请检查网络" + t.getMessage(), Toast.LENGTH_SHORT).show();
                if (flags == 1) {
                    if (adapter.getCount() == 0) {
                        recylerView.showError();
                    }
                } else {
                    pageSize--;
                    adapter.pauseMore();
                }
            }

            @Override
            public void onComplete() {

            }
        };

    }

    private void setHeight(List<GankItemBean> list) {
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int width = dm.widthPixels / 2;//宽度为屏幕宽度一半
        for (GankItemBean gankItemBean : list) {
            gankItemBean.setHeight(width * StringUtils.getRandomNumber(3, 6) / 3);//随机的高度
        }

    }

}
