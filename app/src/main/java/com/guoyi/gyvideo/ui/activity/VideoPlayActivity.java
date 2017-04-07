package com.guoyi.gyvideo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.guoyi.gyvideo.Constants;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.adapter.RelatedAdapter;
import com.guoyi.gyvideo.bean.Collection;
import com.guoyi.gyvideo.bean.EventBean;
import com.guoyi.gyvideo.bean.Record;
import com.guoyi.gyvideo.bean.VideoInfo;
import com.guoyi.gyvideo.bean.VideoRes;
import com.guoyi.gyvideo.mvp.mode.db.OfflineACache;
import com.guoyi.gyvideo.mvp.mode.db.RealmHelper;
import com.guoyi.gyvideo.mvp.mode.net.RetrofitUtils;
import com.guoyi.gyvideo.mvp.mode.net.VideoApis;
import com.guoyi.gyvideo.mvp.mode.net.VideoHttpResponse;
import com.guoyi.gyvideo.utils.BeanUtil;
import com.guoyi.gyvideo.utils.DensityUtil;
import com.guoyi.gyvideo.utils.GsonTools;
import com.guoyi.gyvideo.utils.NetUtils;
import com.guoyi.gyvideo.utils.StringUtils;
import com.guoyi.gyvideo.widget.TextViewExpandableAnimation;
import com.guoyi.gyvideo.widget.theme.ColorRelativeLayout;
import com.guoyi.gyvideo.widget.theme.ColorTextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VideoPlayActivity extends BaseActivity {


    private String TvTitle;
    private String TvUrl;

    @NonNull
    public static void startCurrentActivity(Context context, VideoInfo info) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra(Constants.VIDEO_INFO, info);
        context.startActivity(intent);
    }

    @NonNull
    public static void startTvCurrentActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

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

    @BindView(R.id.video_paly)
    JCVideoPlayerStandard videoPaly;
    @BindView(R.id.recylerView)
    EasyRecyclerView recylerView;


    private VideoInfo video_info;

    private View headerView;
    private VideoRes videoRes;

    private RelatedAdapter adapter;
    private TextViewExpandableAnimation expandableAnimation;

    private OfflineACache aCache;

    @OnClick({R.id.rl_back, R.id.iv_collect})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                onBackPressed();
                break;
            case R.id.iv_collect:
                if (videoRes != null) {
                    collect(videoRes);
                }
                break;
        }
    }

    JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    SensorManager sensorManager;

    public void initValue() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();

        recylerView.setErrorView(R.layout.view_error);
        adapter = new RelatedAdapter(this);
        recylerView.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        //可变的span size
        gridLayoutManager.setSpanSizeLookup(adapter.obtainGridSpanSizeLookUp(3));
        recylerView.setLayoutManager(gridLayoutManager);

        SpaceDecoration itemDecoration = new SpaceDecoration(DensityUtil.dip2px(this, 8));
        itemDecoration.setPaddingEdgeSide(true);
        itemDecoration.setPaddingStart(true);
        itemDecoration.setPaddingHeaderFooter(false);
        recylerView.addItemDecoration(itemDecoration);


        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return headerView;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });


    }

    private void initEvent() {
        recylerView.getErrorView().setOnClickListener(r -> {
            if (video_info != null)
                getData();
        });
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startCurrentActivity(VideoPlayActivity.this, adapter.getItem(position));
                finish();

            }
        });
    }

    public void getData() {
        if (NetUtils.isNetworkConnected(this)) {
            recylerView.showProgress();
            VideoApis videoApis = RetrofitUtils.get().create(VideoApis.class);
            videoApis.getVideoInfo(video_info.dataId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        } else {
            Toast.makeText(this, "获取失败，请检查网络", Toast.LENGTH_SHORT).show();
        }


    }

    public Observer<VideoHttpResponse<VideoRes>> getObserver() {

        return new Observer<VideoHttpResponse<VideoRes>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(VideoHttpResponse<VideoRes> videoHttpResponse) {
                if (videoHttpResponse.getCode() == 200) {
                    videoRes = videoHttpResponse.getRet();
                    if (videoRes != null) {
                        setData(videoRes, false);
                        insertRecord(videoRes);
                        aCache.put("video_info_cache", GsonTools.createGsonString(videoRes), 7 * OfflineACache.TIME_DAY);
                    }
                } else {
                    Toast.makeText(VideoPlayActivity.this, "加载失败" + videoHttpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    recylerView.showError();
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(VideoPlayActivity.this, "加载失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                recylerView.showError();
            }

            @Override
            public void onComplete() {

            }
        };
    }


    /**
     * r播放电视不用获取服务器数据，直接播放就行
     */
    public void palyTvData() {
        /**
         * 播放电视不保存进度
         *  也可以使用： JCVideoPlayer.clearSavedProgress(this,TvUrl);
         */
        JCVideoPlayer.SAVE_PROGRESS = false;
        //

        titleName.setText(TvTitle);
        videoPaly.setUp(TvUrl
                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, TvTitle);
        videoPaly.onClick(videoPaly.thumbImageView);


        String dir = "导演：";
        String act = "主演：";
        String des = dir + "\n" + act + "\n" + "简介：" + TvTitle;
        expandableAnimation.setText(des);
        String json = aCache.getAsString("video_info_cache");
        if (TextUtils.isEmpty(json)) {
            return;
        }
        VideoRes data = GsonTools.changeGsonToBean(json, VideoRes.class);
        if (data.list != null) {
            if (data.list.size() > 1)
                adapter.addAll(data.list.get(1).childList);
            else
                adapter.addAll(data.list.get(0).childList);
        }
    }

    /**
     * 设置服务器返回的数据
     *
     * @param data
     * @param isLocal
     */
    public void setData(VideoRes data, boolean isLocal) {
        /**
         * 播放电影保存进度
         */
        JCVideoPlayer.SAVE_PROGRESS = true;
        titleName.setText(data.title);
        if (!TextUtils.isEmpty(data.pic))
            Glide.with(this).load(data.pic).into(videoPaly.thumbImageView);
        if (!TextUtils.isEmpty(data.getVideoUrl())) {
            videoPaly.setUp(data.getVideoUrl(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, data.title);
        }
        logD("视频URL：" + data.getVideoUrl());
        if (!isLocal) {
            /**
             * 注意要使用服务器返回的数据，离线数据不用管。
             *  expandableAnimation.setText(des); 只设置第一次有效了，之后再设置无反应
             *
             */
            String dir = "导演：" + StringUtils.removeOtherCode(data.director);
            String act = "主演：" + StringUtils.removeOtherCode(data.actors);
            String des = dir + "\n" + act + "\n" + "简介：" + StringUtils.removeOtherCode(data.description);
            expandableAnimation.setText(des);

        }
        if (data.list != null) {

            if (data.list.size() > 1)
                adapter.addAll(data.list.get(1).childList);
            else
                adapter.addAll(data.list.get(0).childList);
        }

    }

    /**
     * 收藏操作
     *
     * @param result
     */
    public void collect(VideoRes result) {
        //删除收藏
        if (RealmHelper.getInstance().isFavorts(video_info.dataId)) {
            ivCollect.setImageResource(R.mipmap.collection);
            RealmHelper.getInstance().deleteFavort(video_info.dataId);
            //发送事件
            EventBean eventBean = new EventBean(Constants.EVENT_COLLECTION);
            eventBean.op = false;
            eventBean.sendRxBus();
        } else {
            //添加收藏
            if (result != null) {
                ivCollect.setImageResource(R.mipmap.collection_select);
                Collection bean = new Collection();
                bean.setId(String.valueOf(video_info.dataId));
                bean.setPic(video_info.pic);
                bean.setTitle(result.title);
                bean.setAirTime(result.airTime);
                bean.setScore(result.score);
                bean.setTime(System.currentTimeMillis());
                RealmHelper.getInstance().addFavort(bean);

                EventBean eventBean = new EventBean(Constants.EVENT_COLLECTION);
                eventBean.op = true;
                eventBean.sendRxBus();
            }
        }

    }


    /**
     * 播放记录
     *
     * @param result
     */
    public void insertRecord(VideoRes result) {
        if (!RealmHelper.getInstance().queryRecordId(video_info.dataId)) {
            if (result != null) {
                Record bean = new Record();
                bean.setId(String.valueOf(video_info.dataId));
                bean.setPic(video_info.pic);
                bean.setTitle(result.title);
                bean.setTime(System.currentTimeMillis());
                RealmHelper.getInstance().insertRecord(bean, 10);
                EventBean eventBean = new EventBean(Constants.EVENT_RECORD);
                eventBean.op = true;
                eventBean.sendRxBus();


            }
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
        headerView = getLayoutInflater().inflate(R.layout.intro_header, null);
        expandableAnimation = ButterKnife.findById(headerView, R.id.tv_expand);
        aCache = OfflineACache.get(this);

        video_info = getIntent().getParcelableExtra(Constants.VIDEO_INFO);
        TvTitle = getIntent().getStringExtra("title");
        TvUrl = getIntent().getStringExtra("url");
        initValue();
        initEvent();

        //播放电影
        if (video_info != null) {
            rlCollect.setVisibility(View.VISIBLE);
            if (RealmHelper.getInstance().isFavorts(video_info.dataId)) {
                ivCollect.setImageResource(R.mipmap.collection_select);
            } else {
                ivCollect.setImageResource(R.mipmap.collection);
            }

            /**
             * 使用历史
             */
            VideoRes videoRes = BeanUtil.VideoInfo2VideoRes(video_info, null);
            setData(videoRes, true);
            getData();

            //播放电视
        } else if (!TextUtils.isEmpty(TvUrl) && !TextUtils.isEmpty(TvTitle)) {
            palyTvData();
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (videoRes != null) {
            /**
             *
             public static final int CURRENT_STATE_NORMAL                  = 0;  没状态
             public static final int CURRENT_STATE_PREPARING               = 1;  准备
             public static final int CURRENT_STATE_PLAYING                 = 2;  播放中
             public static final int CURRENT_STATE_PLAYING_BUFFERING_START = 3;  播放缓冲中
             public static final int CURRENT_STATE_PAUSE                   = 5;  暂停
             public static final int CURRENT_STATE_AUTO_COMPLETE           = 6;  完成
             public static final int CURRENT_STATE_ERROR                   = 7;  出错
             */
            int state = videoPaly.currentState;
            logD("视频state=" + state);
            //继续播放
            if (state != JCVideoPlayer.CURRENT_STATE_PLAYING) {
                videoPaly.startButton.performClick(); //模拟用户点击开始按钮，NORMAL状态下点击开始播放视频，播放中点击暂停视频
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //暂停
        if (videoPaly.currentState != JCVideoPlayer.CURRENT_STATE_NORMAL) {
            videoPaly.startButton.performClick();
        }
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放
        JCVideoPlayer.releaseAllVideos();
    }
}
