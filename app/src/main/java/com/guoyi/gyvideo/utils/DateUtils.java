package com.guoyi.gyvideo.utils;

import android.view.View;

/**
 * Created by Credit on 2017/3/23.
 */

public class DateUtils {


    /**
     * 双击退出
     *
     * @param v
     * @return
     */
    public static boolean isDoublePress(View v) {
        boolean back = false;
        if (v == null) {
            return back;
        }
        Object tag = v.getTag();
        if (tag == null) {
            long l = System.currentTimeMillis();
            v.setTag(l);
        } else {
            long ll = (long) tag;
            if (ll + 1000 > System.currentTimeMillis()) {
                back = true;
            }
        }

        return back;
    }


    public static long lastTime = 0;

    /**
     * 双击退出
     *
     * @return
     */
    public static boolean isDoublePress() {
        return isDoublePress(1500);
    }

    /**
     * 双击退出
     *
     * @return
     */
    public static boolean isDoublePress(int time) {
        long millis = System.currentTimeMillis();
        if (lastTime + time > millis) {
            lastTime = 0;
            return true;
        }
        lastTime = millis;
        return false;


    }
}
