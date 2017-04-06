package com.guoyi.gyvideo.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.guoyi.gyvideo.Constants;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.adapter.MineHistoryVideoListAdapter;
import com.guoyi.gyvideo.bean.EventBean;
import com.guoyi.gyvideo.bean.VideoType;
import com.guoyi.gyvideo.mvp.presenter.contract.MineContract;
import com.guoyi.gyvideo.ui.activity.MainActivity;
import com.guoyi.gyvideo.ui.activity.RecordHistoryActivity;
import com.guoyi.gyvideo.ui.activity.SettingActivity;
import com.guoyi.gyvideo.ui.activity.VideoPlayActivity;
import com.guoyi.gyvideo.ui.activity.WelfareActivity;
import com.guoyi.gyvideo.utils.AttrsHelper;
import com.guoyi.gyvideo.utils.BeanUtil;
import com.guoyi.gyvideo.utils.DensityUtil;
import com.guoyi.gyvideo.utils.rxbus2.RxBusBuilder;
import com.guoyi.gyvideo.widget.theme.ColorTextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 * Created by Credit on 2017/3/23.
 */

public class MineView extends RootView<MineContract.Presenter> implements MineContract.View {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.title_name)
    ColorTextView titleName;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_welfare)
    TextView tvWelfare;

    @BindView(R.id.rl_welfare)
    RelativeLayout rlWelfare;
    @BindView(R.id.tv_history)
    TextView tvHistory;

    @BindView(R.id.rl_record)
    RelativeLayout rlRecord;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;


    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.rl_collection)
    RelativeLayout rlCollection;

    @BindView(R.id.tv_them)
    TextView tvThem;
    @BindView(R.id.rl_them)
    RelativeLayout rlThem;

    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;

    private MineHistoryVideoListAdapter adapter;
    private Disposable disposable;
    private MainActivity activity1;


    @OnClick({R.id.rl_welfare, R.id.rl_record, R.id.rl_collection, R.id.rl_them, R.id.rl_setting})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.rl_welfare:
                mContext.startActivity(new Intent(mContext, WelfareActivity.class));
                break;
            case R.id.rl_record:
                RecordHistoryActivity.startCurrentActivity(mContext, Constants.OPEN_RECORD);
                break;
            case R.id.rl_collection:
                RecordHistoryActivity.startCurrentActivity(mContext, Constants.OPEN_FAVORT);
                break;
            case R.id.rl_them:
                new ColorChooserDialog.Builder(activity1, R.string.theme)
                        .customColors(R.array.colors, null)
                        .doneButton(R.string.done)
                        .cancelButton(R.string.cancel)
                        .allowUserColorInput(false)
                        .allowUserColorInputAlpha(false)
                        .show();
                break;
            case R.id.rl_setting:
                mContext.startActivity(new Intent(mContext, SettingActivity.class));
                break;
        }
    }

    public MineView(Context context) {
        super(context);
    }

    public MineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setPresenter(MineContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    protected void getLayout() {
        inflate(mContext, R.layout.fragment_mine_view, this);
    }

    @Override
    protected void initView() {


        activity1 = (MainActivity) mContext;
        rlBack.setVisibility(GONE);
        titleName.setText(R.string.mine);

        recyclerView.setRefreshingColor(AttrsHelper.getColor(mContext, R.attr.colorPrimary), AttrsHelper.getColor(mContext, R.attr.colorPrimaryLight));
        adapter = new MineHistoryVideoListAdapter(mContext);
        recyclerView.setAdapter(adapter);
        recyclerView.setErrorView(R.layout.view_error);
        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        manager.setSpanSizeLookup(adapter.obtainGridSpanSizeLookUp(3));
        recyclerView.setLayoutManager(manager);

        SpaceDecoration decoration = new SpaceDecoration(DensityUtil.dip2px(getContext(), 8));
        decoration.setPaddingEdgeSide(true);
        decoration.setPaddingStart(true);
        decoration.setPaddingHeaderFooter(false);
        recyclerView.addItemDecoration(decoration);

    }


    @Override
    protected void initEvent() {
        adapter.setOnItemClickListener(position -> {
            VideoType item = adapter.getItem(position);
            VideoPlayActivity.startCurrentActivity(mContext, BeanUtil.VideoType2VideoInfo(item, null));
        });
    }

    @Override
    public void showData(List<VideoType> data) {

        if (data != null && data.size() > 0) {
            recyclerView.setVisibility(VISIBLE);
            adapter.clear();
            adapter.addAll(data);
        } else {
            adapter.clear();
            recyclerView.setVisibility(GONE);
        }
    }

    @Override
    public void showLoadView(String msg) {

    }

    @Override
    public void showNoDataView() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        disposable = RxBusBuilder.create(EventBean.class)
                .withKey(Constants.EVENT_RECORD)
                .subscribe(con -> {
                    logD("收到播放记录");
                    mPresenter.getHistoryData();
                });
    }

    @Override
    protected void onDetachedFromWindow() {
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = null;
        super.onDetachedFromWindow();
    }
}
