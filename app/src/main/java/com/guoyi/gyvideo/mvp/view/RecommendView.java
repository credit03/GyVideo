package com.guoyi.gyvideo.mvp.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guoyi.gyvideo.Constants;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.adapter.RecommendAdapter;
import com.guoyi.gyvideo.bean.EventBean;
import com.guoyi.gyvideo.bean.VideoInfo;
import com.guoyi.gyvideo.bean.VideoRes;
import com.guoyi.gyvideo.mvp.presenter.contract.RecommendContract;
import com.guoyi.gyvideo.ui.activity.VideoPlayActivity;
import com.guoyi.gyvideo.ui.activity.VideoSearchActivity;
import com.guoyi.gyvideo.utils.AttrsHelper;
import com.guoyi.gyvideo.utils.DensityUtil;
import com.guoyi.gyvideo.utils.rxbus2.RxBusBuilder;
import com.guoyi.gyvideo.widget.RollViewPager;
import com.guoyi.gyvideo.widget.theme.ColorRelativeLayout;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.guoyi.gyvideo.utils.DensityUtil.dip2px;

/**
 * Created by Credit on 2017/3/21.
 */

public class RecommendView extends RootView<RecommendContract.Presenter> implements RecommendContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recylerView)
    EasyRecyclerView recylerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.title)
    ColorRelativeLayout title;


    private ArrayList<View> dianList;
    private ArrayList<VideoInfo> recommend;
    private Disposable rxDispsable;

    public RecommendView(Context context) {
        super(context);
    }

    public RecommendView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setPresenter(RecommendContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    protected void getLayout() {
        inflate(mContext, R.layout.fragment_recommend_view, this);
    }


    private RecommendAdapter adapter;


    private RollViewPager banner;
    private LinearLayout llDian;
    private TextView etSearchKey;
    private RelativeLayout rlGoSearch;
    private TextView tvType;
    private LinearLayout llType;

    private View headView;

    @Override
    protected void initView() {
        title.setVisibility(GONE);
        tvTitle.setText(R.string.recommend_title);

        headView = LayoutInflater.from(mContext).inflate(R.layout.recommend_header, null);
        banner = ButterKnife.findById(headView, R.id.banner);
        llDian = ButterKnife.findById(headView, R.id.ll_dian);
        etSearchKey = ButterKnife.findById(headView, R.id.etSearchKey);
        rlGoSearch = ButterKnife.findById(headView, R.id.rlGoSearch);
        tvType = ButterKnife.findById(headView, R.id.tv_type);
        llType = ButterKnife.findById(headView, R.id.ll_type);

        recylerView.setRefreshingColor(AttrsHelper.getColor(mContext, R.attr.colorPrimary), AttrsHelper.getColor(mContext, R.attr.colorPrimaryLight));

        recylerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new RecommendAdapter(mContext);
        recylerView.setAdapter(adapter);
        recylerView.setErrorView(R.layout.view_error);
        SpaceDecoration itemDecoration = new SpaceDecoration(DensityUtil.dip2px(getContext(), 8));
        itemDecoration.setPaddingEdgeSide(true);
        itemDecoration.setPaddingStart(true);
        itemDecoration.setPaddingHeaderFooter(false);
        recylerView.addItemDecoration(itemDecoration);


    }


    @Override
    protected void initEvent() {

        recylerView.setRefreshListener(this);

        recylerView.getErrorView().setOnClickListener(r -> {
            onRefresh();
        });

        recylerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (getHeaderScroll() <= DensityUtil.dip2px(mContext, 150)) {
                    new Handler().postAtTime(new Runnable() {
                        @Override
                        public void run() {
                            float percentage = (float) getHeaderScroll() / DensityUtil.dip2px(mContext, 150);
                            title.setAlpha(percentage);
                            if (percentage > 0) {
                                title.setVisibility(View.VISIBLE);
                            } else {
                                title.setVisibility(View.GONE);
                                banner.restartRoll();
                            }

                        }
                    }, 300);
                } else {
                    title.setAlpha(1.0f);
                    title.setVisibility(View.VISIBLE);
                }
            }
        });

        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                VideoPlayActivity.startCurrentActivity(mContext, adapter.getItem(position));
            }
        });

        rlGoSearch.setOnClickListener(view -> {
            if (recommend != null) {
                VideoSearchActivity.startCurrentActivity(mContext, recommend);
            }
        });

        tvTitle.setOnClickListener(view -> {
            Object tag = tvTitle.getTag();
            if (tag == null) {
                long l = System.currentTimeMillis();
                tvTitle.setTag(l);
            } else {
                long lastTime = (long) tag;
                if (lastTime + 1500 * 60 > System.currentTimeMillis()) {
                    recylerView.scrollToPosition(0);
                }
            }

        });
    }

    @Override
    public void showData(VideoRes videoRes) {
        logD("showData--" + videoRes.toString());
        recylerView.setRefreshing(false);
        if (videoRes != null) {
            adapter.clear();
            List<VideoInfo> videoInfos;
            for (int i = 1; i < videoRes.list.size(); i++) {
                if (videoRes.list.get(i).title.equals("精彩推荐")) {
                    videoInfos = videoRes.list.get(i).childList;
                    adapter.addAll(videoInfos);
                    break;
                }
            }
            for (int i = 1; i < videoRes.list.size(); i++) {
                if (videoRes.list.get(i).title.equals("免费推荐")) {
                    recommend = new ArrayList<>(videoRes.list.get(i).childList);
                    break;
                }
            }

            if (recommend != null && recommend.size() > 0) {
                VideoInfo info = recommend.get(0);
                etSearchKey.setText(info.title);
            }

            if (adapter.getHeaderCount() == 0) {
                adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
                    @Override
                    public View onCreateView(ViewGroup parent) {
                        logD("创建RollView");
                        List<VideoInfo> childList = videoRes.list.get(0).childList;
                        initDian(childList.size());
                        banner.setUriList(childList);
                        banner.setRollViewPager(dianList, R.mipmap.ic_page_indicator_focused, R.mipmap.ic_page_indicator, new RollViewPager.OnPagerClickCallback() {
                            @Override
                            public void onPagerClick(View v, int position) {
                                VideoInfo item = banner.getItem(position);
                                if (item != null) {
                                    VideoPlayActivity.startCurrentActivity(mContext, item);
                                }

                            }
                        });
                        return headView;
                    }

                    @Override
                    public void onBindView(View headerView) {
                        if (!banner.isStart()) {
                            banner.restartRoll();
                        }
                    }
                });
            }
        }
    }

    /**
     * 初始滚动view游标点
     *
     * @param size
     */

    private void initDian(int size) {
        if (dianList == null) {
            dianList = new ArrayList<View>();
        } else {
            dianList.clear();
        }
        llDian.removeAllViews();
        int i1 = dip2px(mContext, 6);
        for (int i = 0; i < size; i++) {

            LayoutParams params = new LayoutParams(
                    i1, i1);
            params.setMargins(5, 0, 5, 0);
            View d = new View(mContext);
            if (i == 0) {
                d.setBackgroundResource(R.mipmap.ic_page_indicator_focused);
            } else {
                d.setBackgroundResource(R.mipmap.ic_page_indicator);
            }
            d.setLayoutParams(params);
            llDian.addView(d);
            dianList.add(d);

        }

    }

    private int getHeaderScroll() {
        if (headView == null) {
            return 0;
        }
        int distance = headView.getTop();
        distance = Math.abs(distance);
        return distance;
    }

    @Override
    public void showLoadView(String msg) {
        if (recylerView != null) {
            recylerView.showProgress();
        }
    }

    @Override
    public void showNoDataView() {
        logD("showNoDataView--");
        if (recylerView != null) {
            recylerView.showEmpty();
        }
    }

    @Override
    public void showError(String msg) {
        logD("showError--" + msg);
        Toast.makeText(mContext, "获取失败，请检查网络", Toast.LENGTH_SHORT).show();
        if (adapter == null || adapter.getCount() == 0) {
            recylerView.showError();
        }
    }


    @Override
    public void onRefresh() {
        mPresenter.loadData();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        rxDispsable = RxBusBuilder.create(EventBean.class)
                .withKey(Constants.EVENT_RECOMMEND, Constants.EVENT_RECOMMEND_REFRESH, Constants.THEME_CHANGE)
                .subscribe(new Consumer<EventBean>() {
                    @Override
                    public void accept(EventBean event) {
                        logD("RxBus 监听" + event.eventFlag);
                        /**
                         * RxBus 监听，Fragment---onResume---onStop
                         */
                        switch (event.eventFlag) {
                            case Constants.EVENT_RECOMMEND:
                                if (adapter.getHeaderCount() > 0) {
                                    if (event.op) {
                                        if (!banner.isStart()) {
                                            banner.restartRoll();
                                        }
                                    } else {
                                        logD("停止轮播");
                                        banner.stopRoll();
                                    }
                                }
                                break;
                            /**
                             * 添加来自Fragment 5分钟刷新一次界面
                             */
                            case Constants.EVENT_RECOMMEND_REFRESH:
                                onRefresh();
                                break;
                            case Constants.THEME_CHANGE:
                                if (recylerView != null)
                                    recylerView.setRefreshingColor(AttrsHelper.getColor(mContext, R.attr.colorPrimary), AttrsHelper.getColor(mContext, R.attr.colorPrimaryLight));
                                break;

                        }

                    }
                });


    }

    @Override
    protected void onDetachedFromWindow() {
        if (rxDispsable != null) {
            rxDispsable.dispose();
        }
        rxDispsable = null;
        super.onDetachedFromWindow();

    }
}
