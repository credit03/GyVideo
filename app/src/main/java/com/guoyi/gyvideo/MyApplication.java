package com.guoyi.gyvideo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.Logger;

import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;

/**
 * Created by Credit on 2017/3/20.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    private Set<Activity> allActivities;

    public static final boolean DEBUG = true;
    private Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Logger.init("gyvideo");
        Realm.init(this);
       /* RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);*/
    }

    public void registerActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<Activity>();
        }
        allActivities.add(act);
    }

    public void unregisterActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    if (act != null && !act.isFinishing())
                        act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public Context getContext() {
        return context;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static void logD(String msg) {
        if (DEBUG) {
            Logger.d(msg);
        }
    }

    public static void logJSON(String msg) {
        if (DEBUG) {
            Logger.json(msg);
        }
    }

    public static void logE(String msg) {
        if (DEBUG) {
            Logger.e(msg);
        }
    }
}
