package com.guoyi.gyvideo.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.adapter.TvListAdapter;
import com.guoyi.gyvideo.bean.TvBean;
import com.guoyi.gyvideo.mvp.mode.db.RealmHelper;
import com.guoyi.gyvideo.utils.DensityUtil;
import com.guoyi.gyvideo.widget.theme.ColorRelativeLayout;
import com.guoyi.gyvideo.widget.theme.ColorTextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 电视节目列表
 */
public class TvListActivity extends BaseActivity implements TextWatcher {

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

    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.recylerView)
    EasyRecyclerView recylerView;
    @BindView(R.id.resultRecyler)
    EasyRecyclerView resultRecyler;

    /**
     * 电视节目adapter
     */
    private TvListAdapter listAdapter;
    /**
     * 搜索节目adapter
     */
    private TvListAdapter resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_list);
        ButterKnife.bind(this);

        initValue();
        initEvent();
    }

    private void initValue() {
        titleName.setText("电视节目");

        listAdapter = new TvListAdapter(this);
        resultAdapter = new TvListAdapter(this);

        recylerView.setAdapter(listAdapter);
        resultRecyler.setAdapter(resultAdapter);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setSpanSizeLookup(listAdapter.obtainGridSpanSizeLookUp(2));
        recylerView.setLayoutManager(manager);

        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        resultRecyler.setLayoutManager(manager1);

        List<TvBean> tvList = RealmHelper.getInstance().getTvList();
        listAdapter.addAll(tvList);

        SpaceDecoration decoration = new SpaceDecoration(DensityUtil.dip2px(this, 8));
        decoration.setPaddingEdgeSide(true);
        decoration.setPaddingStart(true);
        decoration.setPaddingHeaderFooter(false);
        recylerView.addItemDecoration(decoration);
        resultRecyler.addItemDecoration(decoration);
    }


    private void initEvent() {
        listAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TvBean item = listAdapter.getItem(position);
                VideoPlayActivity.startTvCurrentActivity(TvListActivity.this, item.getName(), item.getUrl());
            }
        });
        resultAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TvBean item = resultAdapter.getItem(position);
                VideoPlayActivity.startTvCurrentActivity(TvListActivity.this, item.getName(), item.getUrl());
            }
        });

        etSearch.addTextChangedListener(this);
        btnSearch.setOnClickListener(view -> {
            etSearch.setText("");
        });
        rlBack.setOnClickListener(view -> finish());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String keyword = editable.toString();
        if (TextUtils.isEmpty(keyword)) {
            if (resultAdapter.getCount() > 0) {
                resultRecyler.setVisibility(View.GONE);
                resultAdapter.clear();
            }
            btnSearch.setVisibility(View.GONE);
            recylerView.setVisibility(View.VISIBLE);
        } else {
            recylerView.setVisibility(View.GONE);
            btnSearch.setVisibility(View.VISIBLE);
            resultRecyler.setVisibility(View.VISIBLE);
            List<TvBean> tVlikeList = RealmHelper.getInstance().getTVlikeList(keyword);
            resultAdapter.clear();
            if (tVlikeList == null || tVlikeList.size() == 0) {
                resultRecyler.showEmpty();
            } else {
                resultAdapter.addAll(tVlikeList);
            }

        }

    }
}
