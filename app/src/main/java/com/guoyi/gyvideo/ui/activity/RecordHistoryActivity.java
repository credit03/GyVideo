package com.guoyi.gyvideo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.guoyi.gyvideo.Constants;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.adapter.VideoListAdapter;
import com.guoyi.gyvideo.bean.Collection;
import com.guoyi.gyvideo.bean.EventBean;
import com.guoyi.gyvideo.bean.Record;
import com.guoyi.gyvideo.bean.VideoType;
import com.guoyi.gyvideo.mvp.mode.db.RealmHelper;
import com.guoyi.gyvideo.utils.BeanUtil;
import com.guoyi.gyvideo.utils.DateUtils;
import com.guoyi.gyvideo.utils.DensityUtil;
import com.guoyi.gyvideo.widget.theme.ColorRelativeLayout;
import com.guoyi.gyvideo.widget.theme.ColorTextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查看历史与收藏列表
 */
public class RecordHistoryActivity extends BaseActivity {

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
    private List<VideoType> typeslist;
    private int op;


    @OnClick({R.id.rl_back, R.id.rl_collect_clear, R.id.title_name})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_back:
                onBackPressed();
                break;
            case R.id.rl_collect_clear:
                new MaterialDialog.Builder(this).content(
                        "是否清空" + titleName.getText()
                ).negativeText("确定")
                        .neutralText("取消")
                        .onNegative((d, i) -> {
                            d.dismiss();
                            deleteData();
                        }).onNeutral(null).show();

                break;
            case R.id.title_name:
                if (DateUtils.isDoublePress(v)) {
                    recylerView.scrollToPosition(0);
                }
                break;
        }
    }


    public void deleteData() {
        rlCollectClear.setVisibility(View.GONE);
        if (op == Constants.OPEN_RECORD) {
            RealmHelper.getInstance().deleteAllRecord();
            EventBean bean = new EventBean(Constants.EVENT_RECORD);
            bean.op = false;
            bean.sendRxBus();
        } else {
            RealmHelper.getInstance().deleteAllFavorts();
            EventBean bean = new EventBean(Constants.EVENT_COLLECTION);
            bean.op = false;
            bean.sendRxBus();
        }
        adapter.clear();
        recylerView.showEmpty();
    }

    public static void startCurrentActivity(Context context, int opState) {
        Intent intent = new Intent(context, RecordHistoryActivity.class);
        intent.putExtra("op", opState);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_history);
        ButterKnife.bind(this);

        op = getIntent().getIntExtra("op", Constants.OPEN_RECORD);
        initValue();
        initEvent();
    }

    private void initValue() {
        rlCollectClear.setVisibility(View.VISIBLE);


        recylerView.setAdapter(adapter = new VideoListAdapter(this));

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setSpanSizeLookup(adapter.obtainGridSpanSizeLookUp(3));
        recylerView.setLayoutManager(manager);
        SpaceDecoration decoration = new SpaceDecoration(DensityUtil.dip2px(this, 8));
        decoration.setPaddingHeaderFooter(false);
        decoration.setPaddingStart(true);
        decoration.setPaddingEdgeSide(true);
        recylerView.addItemDecoration(decoration);

        if (op == Constants.OPEN_RECORD) {
            titleName.setText("播放历史");
            List<Record> recordList = RealmHelper.getInstance().getRecordList();
            typeslist = BeanUtil.RecordToVideoInfo(recordList);

        } else {
            titleName.setText("收藏");
            List<Collection> collections = RealmHelper.getInstance().queryFavorts();
            typeslist = BeanUtil.FavortToVideoInfo(collections);
        }
        if (typeslist != null && typeslist.size() > 0) {
            adapter.addAll(typeslist);
        } else {
            rlCollectClear.setVisibility(View.GONE);
            recylerView.showEmpty();
        }

    }


    private void initEvent() {
        adapter.setOnItemClickListener(position -> {
            VideoPlayActivity.startCurrentActivity(this, BeanUtil.VideoType2VideoInfo(adapter.getItem(position), null));
        });
    }
}
