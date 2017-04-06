package com.guoyi.gyvideo.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.guoyi.gyvideo.Constants;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.adapter.TopIcAdapter;
import com.guoyi.gyvideo.bean.EventBean;
import com.guoyi.gyvideo.bean.VideoInfo;
import com.guoyi.gyvideo.bean.VideoRes;
import com.guoyi.gyvideo.mvp.presenter.contract.TopIcContract;
import com.guoyi.gyvideo.ui.activity.TvListActivity;
import com.guoyi.gyvideo.ui.activity.VideoListActivity;
import com.guoyi.gyvideo.utils.AttrsHelper;
import com.guoyi.gyvideo.utils.DensityUtil;
import com.guoyi.gyvideo.utils.StringUtils;
import com.guoyi.gyvideo.utils.rxbus2.RxBusBuilder;
import com.guoyi.gyvideo.widget.theme.ColorTextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * Created by Credit on 2017/3/22.
 */

public class TopIcView extends RootView<TopIcContract.Presenter> implements TopIcContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.title_name)
    ColorTextView titleName;
    @BindView(R.id.rl_collect)
    RelativeLayout rlCollect;
    @BindView(R.id.rl_collect_clear)
    RelativeLayout rlCollectClear;
    @BindView(R.id.recylerView)
    EasyRecyclerView recylerView;

    private TopIcAdapter adapter;
    private Disposable rxDisposable;

    public TopIcView(Context context) {
        super(context);
    }

    public TopIcView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopIcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setPresenter(TopIcContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoadView(String msg) {
        if (recylerView != null) {
            recylerView.showProgress();
        }
    }

    @Override
    public void showNoDataView() {
        if (recylerView != null) {
            recylerView.showEmpty();
        }
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        if (adapter == null || adapter.getCount() == 0) {
            recylerView.showError();
        }
    }

    @Override
    public void showData(VideoRes res) {
        if (res != null) {
            adapter.clear();
            List<VideoInfo> videoInfos = new ArrayList<>();
            VideoInfo info = new VideoInfo();
            info.title = "电视节目";
            info.pic = "file:///android_asset/tv.jpg";
            videoInfos.add(info);
            for (int i = 1; i < res.list.size(); i++) {
                if (!TextUtils.isEmpty(res.list.get(i).moreURL) && !TextUtils.isEmpty(res.list.get(i).title)) {
                    VideoInfo videoInfo = res.list.get(i).childList.get(0);
                    videoInfo.title = res.list.get(i).title;
                    videoInfo.moreURL = res.list.get(i).moreURL;
                    videoInfos.add(videoInfo);
                }
            }
            adapter.addAll(videoInfos);
        }
    }

    @Override
    protected void getLayout() {
        inflate(mContext, R.layout.fragment_topic_view, this);
    }

    @Override
    protected void initView() {
        rlBack.setVisibility(GONE);
        titleName.setText(R.string.top_title);
        recylerView.setRefreshingColor(AttrsHelper.getColor(mContext, R.attr.colorPrimary), AttrsHelper.getColor(mContext, R.attr.colorPrimaryLight));
        adapter = new TopIcAdapter(mContext);
        recylerView.setAdapter(adapter);
        recylerView.setErrorView(R.layout.view_error);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        manager.setSpanSizeLookup(adapter.obtainGridSpanSizeLookUp(2));
        recylerView.setLayoutManager(manager);

        SpaceDecoration decoration = new SpaceDecoration(DensityUtil.dip2px(getContext(), 8));
        decoration.setPaddingEdgeSide(true);
        decoration.setPaddingStart(true);
        decoration.setPaddingHeaderFooter(false);
        recylerView.addItemDecoration(decoration);
    }

    @Override
    protected void initEvent() {
        recylerView.setRefreshListener(this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    mContext.startActivity(new Intent(mContext, TvListActivity.class));
                } else {
                    VideoListActivity.startCurrentActivity(mContext, adapter.getItem(position).title, StringUtils.getCatalogId(adapter.getItem(position).moreURL));
                }
            }
        });
        recylerView.getErrorView().setOnClickListener(view -> {
            onRefresh();
        });
    }

    @Override
    public void onRefresh() {
        mPresenter.getData();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //RxBus.get().withKey(Constants.EVENT_TOPIC_REFRESH).
        if (rxDisposable != null) {
            rxDisposable.dispose();
        }
        rxDisposable = null;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        rxDisposable = RxBusBuilder.create(EventBean.class)
                .withKey(Constants.EVENT_TOPIC_REFRESH, Constants.THEME_CHANGE)
                .subscribe(con -> {
                    switch (con.eventFlag) {
                        case Constants.THEME_CHANGE:
                            if (recylerView != null)
                                recylerView.setRefreshingColor(AttrsHelper.getColor(mContext, R.attr.colorPrimary), AttrsHelper.getColor(mContext, R.attr.colorPrimaryLight));
                            break;
                        case Constants.EVENT_TOPIC_REFRESH:
                            onRefresh();
                            break;

                    }

                });
    }
}
