package com.guoyi.gyvideo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.adapter.VideoListAdapter;
import com.guoyi.gyvideo.bean.VideoRes;
import com.guoyi.gyvideo.bean.VideoType;
import com.guoyi.gyvideo.mvp.mode.net.RetrofitUtils;
import com.guoyi.gyvideo.mvp.mode.net.VideoApis;
import com.guoyi.gyvideo.mvp.mode.net.VideoHttpResponse;
import com.guoyi.gyvideo.utils.AttrsHelper;
import com.guoyi.gyvideo.utils.BeanUtil;
import com.guoyi.gyvideo.utils.DateUtils;
import com.guoyi.gyvideo.utils.DensityUtil;
import com.guoyi.gyvideo.utils.NetUtils;
import com.guoyi.gyvideo.widget.theme.ColorRelativeLayout;
import com.guoyi.gyvideo.widget.theme.ColorTextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;

public class VideoListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

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

    private VideoListAdapter adapter;

    private int pageIndex = 1;
    private int pageSize = 30;

    private String mTitle = "";
    private String mCatalogId = "";

    public static void startCurrentActivity(Context context, String mTitle, String mCatalogId) {
        Intent intent = new Intent(context, VideoListActivity.class);
        intent.putExtra("catalogId", mCatalogId);
        intent.putExtra("title", mTitle);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);

        mCatalogId = getIntent().getStringExtra("catalogId");
        mTitle = getIntent().getStringExtra("title");
        initValue();
        initEvent();
        recylerView.showProgress();
        onRefresh();
    }

    private void initValue() {
        recylerView.setAdapter(adapter = new VideoListAdapter(this));
        recylerView.setErrorView(R.layout.view_error);
        recylerView.setRefreshListener(this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setMore(R.layout.view_more, this);
        recylerView.setRefreshingColor(AttrsHelper.getColor(this, R.attr.colorPrimary), AttrsHelper.getColor(this, R.attr.colorPrimaryLight));

        titleName.setText(mTitle);

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setSpanSizeLookup(adapter.obtainGridSpanSizeLookUp(3));
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
            VideoType item = adapter.getItem(position);
            VideoPlayActivity.startCurrentActivity(VideoListActivity.this, BeanUtil.VideoType2VideoInfo(item, null));

        });

    }


    @Override
    public void onRefresh() {
        if (NetUtils.isNetworkConnected(this)) {
            pageIndex = 1;
            VideoApis apis = RetrofitUtils.get().create(VideoApis.class);
            addDisposable(apis.getVideoList(mCatalogId, pageIndex + "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getObserver(1)));
        } else {
            this.recylerView.showRecycler();
            Toast.makeText(this, "获取失败，请检查网络", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoadMore() {
        if (NetUtils.isNetworkConnected(this)) {
            pageIndex++;
            VideoApis apis = RetrofitUtils.get().create(VideoApis.class);
            addDisposable(apis.getVideoList(mCatalogId, pageIndex + "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getObserver(2)));
        } else {
            this.recylerView.showRecycler();
            adapter.pauseMore();
            Toast.makeText(this, "获取失败，请检查网络", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 若刷新返回的数据数量小于30时,表明服务器没有更多数据了- 禁用加载更多界面
     */
    public void clearFooter() {
        adapter.setMore(new View(this), this);
        adapter.setError(new View(this));
        adapter.setNoMore(new View(this));
    }

    public ResourceObserver<VideoHttpResponse<VideoRes>> getObserver(final int flags) {
        return new ResourceObserver<VideoHttpResponse<VideoRes>>() {
            @Override
            public void onNext(VideoHttpResponse<VideoRes> videoResVideoHttpResponse) {
                if (videoResVideoHttpResponse.getCode() == 200) {
                    VideoRes ret = videoResVideoHttpResponse.getRet();
                    if (ret != null) {
                        if (flags == 1) {
                            adapter.clear();
                            if (ret.list != null && ret.list.size() < pageSize) {
                                // 若刷新返回的数据小于30时, 禁用加载更多界面。表明服务器没有更多数据了
                                clearFooter();
                            }
                        }
                        adapter.addAll(ret.list);

                    }
                    recylerView.showRecycler();

                } else {
                    Toast.makeText(VideoListActivity.this, "服务返回出错:" + videoResVideoHttpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(VideoListActivity.this, "加载失败，请检查网络" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
}
