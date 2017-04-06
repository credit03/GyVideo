package com.guoyi.gyvideo.ui.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.guoyi.gyvideo.MyApplication;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.mvp.BasePresenter;
import com.guoyi.gyvideo.mvp.mode.db.OfflineACache;
import com.guoyi.gyvideo.utils.DensityUtil;
import com.guoyi.gyvideo.utils.StatusBarUtil;
import com.guoyi.gyvideo.widget.theme.ColorRelativeLayout;
import com.guoyi.gyvideo.widget.theme.Theme;
import com.orhanobut.logger.Logger;

import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Credit on 2017/3/20.
 */

public class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    protected OfflineACache aCache;
    protected T mPresenter;

    protected Unbinder unbinder;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logD("--onCreate--" + this.getClass().getSimpleName());
        init();

    }


    @Override
    protected void onStart() {
        logD("--onStart--" + this.getClass().getSimpleName());
        super.onStart();
        setTitleHeight(getRootView(this));
    }

    @Override
    protected void onStop() {
        logD("--onStop--" + this.getClass().getSimpleName());
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        logD("--onDestroy--" + this.getClass().getSimpleName());
        super.onDestroy();
        closeDisposable();
        MyApplication.getInstance().unregisterActivity(this);
        if (unbinder != null)
            unbinder.unbind();
        unbinder = null;
        mPresenter = null;
    }

    public void init() {
        StatusBarUtil.setTranslucent(this);
        onPreCreate();
        MyApplication.getInstance().registerActivity(this);
    }

    public void onPreCreate() {
        aCache = OfflineACache.get(this);
        Theme currentTheme = aCache.getCurrentTheme();
        switch (currentTheme) {
            case Blue:
                setTheme(R.style.BlueTheme);
                break;
            case Red:
                setTheme(R.style.RedTheme);
                break;
            case Brown:
                setTheme(R.style.BrownTheme);
                break;
            case Green:
                setTheme(R.style.GreenTheme);
                break;
            case Purple:
                setTheme(R.style.PurpleTheme);
                break;
            case Teal:
                setTheme(R.style.TealTheme);
                break;
            case Pink:
                setTheme(R.style.PinkTheme);
                break;
            case DeepPurple:
                setTheme(R.style.DeepPurpleTheme);
                break;
            case Orange:
                setTheme(R.style.OrangeTheme);
                break;
            case Indigo:
                setTheme(R.style.IndigoTheme);
                break;
            case LightGreen:
                setTheme(R.style.LightGreenTheme);
                break;
            case Lime:
                setTheme(R.style.LimeTheme);
                break;
            case DeepOrange:
                setTheme(R.style.DeepOrangeTheme);
                break;
            case Cyan:
                setTheme(R.style.CyanTheme);
                break;
            case BlueGrey:
                setTheme(R.style.BlueGreyTheme);
                break;
            case Black:
                setTheme(R.style.BlackTheme);
                break;
        }
    }


    private void setTitleHeight(View view) {
        if (view != null) {
            ColorRelativeLayout title = (ColorRelativeLayout) view.findViewById(R.id.title);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                if (title != null) {
                    ViewGroup.LayoutParams lp = title.getLayoutParams();
                    lp.height = DensityUtil.dip2px(this, 48);
                    title.setLayoutParams(lp);
                    title.setPadding(0, 0, 0, 0);
                }
            }
        }
    }

    protected static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

    public void logD(String msg) {
        if (MyApplication.DEBUG) {
            Logger.d(msg);
        }
    }

    public static void logJSON(String msg) {
        if (MyApplication.DEBUG) {
            Logger.json(msg);
        }
    }

    public static void logE(String msg) {
        if (MyApplication.DEBUG) {
            Logger.e(msg);
        }
    }

    /**
     * 添加开关
     * <p>
     * 要使用subscribeWith而不是subscribe， subscribeWith返回不当前关联disposable， 因为subscribe方法现在返回void
     *
     * @param disposable
     */
    protected void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    /**
     * 关闭RX桥梁
     */
    protected void closeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
