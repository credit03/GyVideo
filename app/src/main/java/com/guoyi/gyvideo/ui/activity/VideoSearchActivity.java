package com.guoyi.gyvideo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.adapter.VideoListAdapter;
import com.guoyi.gyvideo.bean.SearchKey;
import com.guoyi.gyvideo.bean.VideoInfo;
import com.guoyi.gyvideo.bean.VideoRes;
import com.guoyi.gyvideo.bean.VideoType;
import com.guoyi.gyvideo.mvp.mode.db.RealmHelper;
import com.guoyi.gyvideo.mvp.mode.net.RetrofitUtils;
import com.guoyi.gyvideo.mvp.mode.net.VideoApis;
import com.guoyi.gyvideo.mvp.mode.net.VideoHttpResponse;
import com.guoyi.gyvideo.utils.AttrsHelper;
import com.guoyi.gyvideo.utils.BeanUtil;
import com.guoyi.gyvideo.utils.CommonUtils;
import com.guoyi.gyvideo.utils.DensityUtil;
import com.guoyi.gyvideo.utils.NetUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.gujun.android.taggroup.TagGroup;

public class VideoSearchActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.btn_search)
    Button mBtn_search;
    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.tv_search_history)
    TextView tvSearchHistory;

    @BindView(R.id.img_search_clear)
    ImageView imgSearchClear;

    @BindView(R.id.tv_search_recommend)
    TextView tvSearchRecommend;

    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.recommend_1)
    LinearLayout recommend1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.recommend_2)
    LinearLayout recommend2;
    @BindView(R.id.recommend_video)
    LinearLayout recommendVideo;

    @BindView(R.id.search_tip)
    LinearLayout searchTip;
    @BindView(R.id.recylerView)
    EasyRecyclerView recylerView;

    @BindView(R.id.rl_search_history)
    RelativeLayout rlSearchHistory;
    @BindView(R.id.tag_group_beauty_inverse)
    TagGroup tagGroupBeauty;

    private ArrayList<VideoInfo> freeVideo;
    private VideoListAdapter adapter;
    private String searchKey;
    private int pageSize = 30;

    public static void startCurrentActivity(Context context, ArrayList<VideoInfo> infos) {
        Intent intent = new Intent(context, VideoSearchActivity.class);
        intent.putParcelableArrayListExtra("videolist", infos);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_search);
        ButterKnife.bind(this);

        freeVideo = getIntent().getParcelableArrayListExtra("videolist");
        initValue();
        initEvent();

    }


    private List<String> tags;

    private void initValue() {

        if (freeVideo != null && freeVideo.size() > 1) {
            recommendVideo.setVisibility(View.VISIBLE);
            VideoInfo info = freeVideo.get(0);
            VideoInfo info1 = freeVideo.get(1);
            tv1.setText(info.title);
            tv2.setText(info1.title);
            Glide.with(this).load(info.pic).into(iv1);
            Glide.with(this).load(info1.pic).into(iv2);
        } else {
            recommendVideo.setVisibility(View.GONE);
        }

        List<SearchKey> searchHistoryListAll = RealmHelper.getInstance().getSearchHistoryListAll();
        if (searchHistoryListAll != null && searchHistoryListAll.size() > 0) {
            rlSearchHistory.setVisibility(View.VISIBLE);
            //  tagGroupBeauty.
            tags = new ArrayList<>();
            for (SearchKey k : searchHistoryListAll) {
                tags.add(k.getSearchKey());
            }
            tagGroupBeauty.setTags(tags);
        } else {
            rlSearchHistory.setVisibility(View.GONE);
        }


        recylerView.setRefreshingColor(AttrsHelper.getColor(this, R.attr.colorPrimary), AttrsHelper.getColor(this, R.attr.colorPrimaryLight));
        recylerView.setVisibility(View.GONE);
        recylerView.setAdapter(adapter = new VideoListAdapter(this));
        recylerView.setErrorView(R.layout.view_error);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setSpanSizeLookup(adapter.obtainGridSpanSizeLookUp(3));
        recylerView.setLayoutManager(manager);

        recylerView.setRefreshListener(this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setMore(R.layout.view_more, this);
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
        adapter.setOnItemClickListener(position -> {
            VideoType item = adapter.getItem(position);
            VideoPlayActivity.startCurrentActivity(VideoSearchActivity.this,
                    BeanUtil.VideoType2VideoInfo(item, null));
        });
        adapter.setError(R.layout.view_error_footer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resumeMore();
            }
        });

        tagGroupBeauty.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                etSearch.setText(tag);
                onClick(mBtn_search);
            }
        });

    }


    private boolean btn_press = false;

    @OnClick({R.id.recommend_1, R.id.recommend_2, R.id.img_search_clear, R.id.back, R.id.btn_search})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.recommend_1://推荐1
                VideoPlayActivity.startCurrentActivity(this, freeVideo.get(0));
                break;
            case R.id.recommend_2: //推荐2
                VideoPlayActivity.startCurrentActivity(this, freeVideo.get(1));
                break;
            case R.id.img_search_clear: //清空历史
                new MaterialDialog.Builder(this)
                        .content("是否清空搜索历史记录")
                        .negativeText("确定")
                        .neutralText("取消")
                        .onNegative((d, i) -> {
                            d.dismiss();
                            RealmHelper.getInstance().deleteSearchHistoryAll();
                            rlSearchHistory.setVisibility(View.GONE);
                            //清空
                            tagGroupBeauty.setTags(new ArrayList<String>());

                        }).onNeutral(null)
                        .build().show();

                break;
            case R.id.back: //后退
                onBackPressed();
                break;
            case R.id.btn_search: //查找
                if (!btn_press) {
                    searchKey = etSearch.getText().toString().trim();
                    if (TextUtils.isEmpty(searchKey)) {
                        Toast.makeText(this, "不能输入为空哦", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    CommonUtils.hideSoftInput(this, v);
                    btn_press = true;
                    mBtn_search.setText(R.string.cancel);
                    mBtn_search.setEnabled(false);
                    etSearch.setEnabled(false);
                    /**
                     * 插入搜索历史
                     */
                    SearchKey key = new SearchKey();
                    key.insertTime = System.currentTimeMillis();
                    key.searchKey = searchKey;
                    RealmHelper.getInstance().insertSearchHistory(key);

                    searchTip.setVisibility(View.GONE);
                    recylerView.setVisibility(View.VISIBLE);
                    recylerView.showProgress();
                    onRefresh();
                } else {
                    etSearch.setEnabled(true);
                    mBtn_search.setText("查找");
                    pageIndex = 1;
                    btn_press = false;
                    etSearch.setText("");
                    recylerView.setRefreshing(false);
                    adapter.clear();
                    recylerView.setVisibility(View.GONE);
                    searchTip.setVisibility(View.VISIBLE);

                    rlSearchHistory.setVisibility(View.VISIBLE);
                    List<SearchKey> ser = RealmHelper.getInstance().getSearchHistoryListAll();
                    tags = new ArrayList<>();
                    for (SearchKey k : ser) {
                        tags.add(k.getSearchKey());
                    }
                    tagGroupBeauty.setTags(tags);
                }

                break;
        }
    }


    private int pageIndex = 1;

    /**
     * 若刷新返回的数据数量小于30时,表明服务器没有更多数据了- 禁用加载更多界面
     */
    public void clearFooter() {
        adapter.setMore(new View(this), this);
        adapter.setError(new View(this));
        adapter.setNoMore(new View(this));
    }


    private void getData(int flags) {

        if (NetUtils.isNetworkConnected(this)) {
            VideoApis videoApis = RetrofitUtils.get().create(VideoApis.class);
            videoApis.getVideoListByKeyWord(searchKey, pageIndex + "").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<VideoHttpResponse<VideoRes>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }

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
                                        } else {
                                            recylerView.showEmpty();
                                        }
                                    }
                                    adapter.addAll(ret.list);

                                }

                            } else {
                                Toast.makeText(VideoSearchActivity.this, "服务返回出错:" + videoResVideoHttpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (flags == 1) {
                                if (adapter.getCount() == 0) {
                                    recylerView.showError();
                                }
                            } else {
                                pageIndex--;
                                adapter.pauseMore();
                            }
                        }

                        @Override
                        public void onComplete() {
                            mBtn_search.setEnabled(true);
                        }
                    });
        } else {
            Toast.makeText(this, "获取失败，请检查网络", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {
        if (btn_press) {
            onClick(mBtn_search);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        getData(1);
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        getData(2);
    }
}
