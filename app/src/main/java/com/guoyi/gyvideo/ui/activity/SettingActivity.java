package com.guoyi.gyvideo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.utils.CleanUtils;
import com.guoyi.gyvideo.widget.theme.ColorTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.title_name)
    ColorTextView titleName;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.tv_cache_count)
    TextView tvCacheCount;
    @BindView(R.id.rl_cache)
    RelativeLayout rlCache;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;

    private boolean clear = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        titleName.setText(R.string.my_setting);
        long length = this.getCacheDir().length();
        tvCacheCount.setText(length / 1024 + "kb");
        rlBack.setOnClickListener(v -> finish());
        rlAbout.setOnClickListener(v -> {
            startActivity(new Intent(SettingActivity.this, AboutActivity.class));
        });
        rlCache.setOnClickListener(view -> {
            if (!clear) {
                clear = true;
                CleanUtils.cleanInternalCache(SettingActivity.this);
            }
            tvCacheCount.setText("0kb");
            Toast.makeText(this, "已清除缓存", Toast.LENGTH_SHORT).show();
        });

    }
}
