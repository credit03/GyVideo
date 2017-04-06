package com.guoyi.gyvideo.ui.activity;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.guoyi.gyvideo.Constants;
import com.guoyi.gyvideo.MyApplication;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.bean.EventBean;
import com.guoyi.gyvideo.mvp.presenter.MainPresenter;
import com.guoyi.gyvideo.mvp.view.MainView;
import com.guoyi.gyvideo.utils.AttrsHelper;
import com.guoyi.gyvideo.utils.DateUtils;
import com.guoyi.gyvideo.widget.theme.Theme;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<MainPresenter> implements ColorChooserDialog.ColorCallback {
    @BindView(R.id.mainview)
    MainView mainview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        dialog.dismiss();
        logD("选择颜色" + selectedColor);
        if (selectedColor == AttrsHelper.getColor(this, R.attr.colorPrimary))
            return;

        if (selectedColor == getResources().getColor(R.color.colorBluePrimary)) {
            this.setTheme(R.style.BlueTheme);
            aCache.setCurrentTheme(Theme.Blue);
        } else if (selectedColor == getResources().getColor(R.color.colorRedPrimary)) {
            this.setTheme(R.style.RedTheme);
            aCache.setCurrentTheme(Theme.Red);
        } else if (selectedColor == getResources().getColor(R.color.colorBrownPrimary)) {
            this.setTheme(R.style.BrownTheme);
            aCache.setCurrentTheme(Theme.Brown);
        } else if (selectedColor == getResources().getColor(R.color.colorGreenPrimary)) {
            this.setTheme(R.style.GreenTheme);
            aCache.setCurrentTheme(Theme.Green);
        } else if (selectedColor == getResources().getColor(R.color.colorPurplePrimary)) {
            this.setTheme(R.style.PurpleTheme);
            aCache.setCurrentTheme(Theme.Purple);
        } else if (selectedColor == getResources().getColor(R.color.colorTealPrimary)) {
            this.setTheme(R.style.TealTheme);
            aCache.setCurrentTheme(Theme.Teal);
        } else if (selectedColor == getResources().getColor(R.color.colorPinkPrimary)) {
            this.setTheme(R.style.PinkTheme);
            aCache.setCurrentTheme(Theme.Pink);
        } else if (selectedColor == getResources().getColor(R.color.colorDeepPurplePrimary)) {
            this.setTheme(R.style.DeepPurpleTheme);
            aCache.setCurrentTheme(Theme.DeepPurple);
        } else if (selectedColor == getResources().getColor(R.color.colorOrangePrimary)) {
            this.setTheme(R.style.OrangeTheme);
            aCache.setCurrentTheme(Theme.Orange);
        } else if (selectedColor == getResources().getColor(R.color.colorIndigoPrimary)) {
            this.setTheme(R.style.IndigoTheme);
            aCache.setCurrentTheme(Theme.Indigo);
        } else if (selectedColor == getResources().getColor(R.color.colorLightGreenPrimary)) {
            this.setTheme(R.style.LightGreenTheme);
            aCache.setCurrentTheme(Theme.LightGreen);
        } else if (selectedColor == getResources().getColor(R.color.colorDeepOrangePrimary)) {
            this.setTheme(R.style.DeepOrangeTheme);
            aCache.setCurrentTheme(Theme.DeepOrange);
        } else if (selectedColor == getResources().getColor(R.color.colorLimePrimary)) {
            this.setTheme(R.style.LimeTheme);
            aCache.setCurrentTheme(Theme.Lime);
        } else if (selectedColor == getResources().getColor(R.color.colorBlueGreyPrimary)) {
            this.setTheme(R.style.BlueGreyTheme);
            aCache.setCurrentTheme(Theme.BlueGrey);
        } else if (selectedColor == getResources().getColor(R.color.colorCyanPrimary)) {
            this.setTheme(R.style.CyanTheme);
            aCache.setCurrentTheme(Theme.Cyan);
        } else if (selectedColor == getResources().getColor(android.R.color.black)) {
            this.setTheme(R.style.BlackTheme);
            aCache.setCurrentTheme(Theme.Black);
        }

        /**
         * 发送主题改变事件
         */
        EventBean bean = new EventBean(Constants.THEME_CHANGE);
        bean.sendRxBus();
    }

    @Override
    public void onBackPressed() {
        if (DateUtils.isDoublePress()) {
            super.onBackPressed();
            MyApplication.getInstance().exitApp();
        } else {
            Toast.makeText(this, "再点击一次退出", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
