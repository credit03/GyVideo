package com.guoyi.gyvideo.mvp.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeFrameLayout;
import com.guoyi.gyvideo.Constants;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.adapter.SwipeDeckAdapter;
import com.guoyi.gyvideo.bean.VideoRes;
import com.guoyi.gyvideo.bean.VideoType;
import com.guoyi.gyvideo.mvp.mode.db.OfflineACache;
import com.guoyi.gyvideo.mvp.presenter.contract.SwipeDeckContract;
import com.guoyi.gyvideo.utils.DensityUtil;
import com.guoyi.gyvideo.utils.StringUtils;
import com.guoyi.gyvideo.widget.LoadProgress;
import com.guoyi.gyvideo.widget.SwipeDeck;
import com.guoyi.gyvideo.widget.theme.ColorTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Credit on 2017/3/22.
 */

public class SwipeDeckView extends RootView<SwipeDeckContract.Presenter> implements SwipeDeckContract.View {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.title_name)
    ColorTextView titleName;

    @BindView(R.id.tv_nomore)
    TextView tvNomore;
    @BindView(R.id.swipe_deck)
    SwipeDeck swipeDeck;

    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.swipeLayout)
    SwipeFrameLayout swipeLayout;
    @BindView(R.id.loading)
    LoadProgress loading;

    private OfflineACache aCache;

    private SwipeDeckAdapter adapter;
    private List<VideoType> typeList = new ArrayList<>();

    public SwipeDeckView(Context context) {
        super(context);
    }

    public SwipeDeckView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeDeckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setPresenter(SwipeDeckContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void getLayout() {
        inflate(mContext, R.layout.fragment_found_view, this);
    }

    @Override
    protected void initView() {
        titleName.setText(R.string.found);
        aCache = OfflineACache.get(mContext);
        rlBack.setVisibility(GONE);
        ViewGroup.LayoutParams lp = swipeDeck.getLayoutParams();
        lp.height = DensityUtil.getScreenHeight(getContext()) / 3 * 2;
        swipeDeck.setLayoutParams(lp);
        swipeDeck.setHardwareAccelerationEnabled(true);

    }


    @Override
    protected void initEvent() {
        btnNext.setOnClickListener(v -> {
            nextVideos();
        });
        tvNomore.setOnClickListener(v -> {
            nextVideos();
        });

        swipeDeck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {

            }

            @Override
            public void cardSwipedRight(int position) {

            }

            @Override
            public void cardsDepleted() {
                swipeDeck.setVisibility(View.GONE);
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });
    }

    @Override
    public void showLoadView(String msg) {
        loading.setVisibility(VISIBLE);
    }

    @Override
    public void showNoDataView() {
        loading.setVisibility(GONE);
        swipeDeck.setVisibility(GONE);
        tvNomore.setVisibility(VISIBLE);

    }

    @Override
    public void showError(String msg) {
        loading.setVisibility(GONE);
        if (typeList.size() == 0) {
            swipeDeck.setVisibility(GONE);
            tvNomore.setVisibility(VISIBLE);
        }
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showData(VideoRes res) {
        loading.setVisibility(GONE);
        typeList.clear();
        typeList.addAll(res.list);
        swipeDeck.removeAllViews();
        adapter = new SwipeDeckAdapter(typeList, mContext);
        swipeDeck.setAdapter(adapter);
        tvNomore.setVisibility(VISIBLE);

    }


    private void nextVideos() {
        swipeDeck.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
        tvNomore.setVisibility(View.GONE);
        mPresenter.getData();
    }

    @Override
    public int getLastPage() {
        String str = aCache.getAsString(Constants.SWIPEDECK_LAST_PAGE);
        if (TextUtils.isEmpty(str)) {
            return 1;
        } else {
            return StringUtils.stringToInt(str);
        }

    }

    @Override
    public void setLastPage(int page) {
        aCache.put(Constants.SWIPEDECK_LAST_PAGE, page + "");
    }


}
