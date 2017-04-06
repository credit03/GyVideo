package com.guoyi.gyvideo;

import com.guoyi.gyvideo.bean.AboutBean;

import java.util.ArrayList;

/**
 * Created by Credit on 2017/3/17.
 */

public final class Constants {

    public static final int EVENT_RECOMMEND = 1; //RecommendFragment--onResume---onStop
    public static final int EVENT_RECOMMEND_REFRESH = 4; //推荐刷新
    public static final int EVENT_TOPIC_REFRESH = 5; //专题刷新

    public static final int EVENT_COLLECTION = 2; //收藏事件

    public static final int EVENT_RECORD = 3; //播放记录事件

    public static final int OPEN_RECORD = 6; //播放记录
    public static final int OPEN_FAVORT = 7; //收藏记录

    public static final int THEME_CHANGE = 100; //主题改变

    public static final String VIDEO_INFO = "video_info";
    public static final String VIDEO_TV = "TV_info";
    public static final String SWIPEDECK_LAST_PAGE = "last_page";

    public static AboutBean response;
    public static AboutBean my;

    static {
        response = new AboutBean();
        response.language = "java";
        response.name = "Ghost";
        response.description = "微影，一款纯粹的在线视频App，基于Material Design + MVP + RxJava + Retrofit + Realm + Glide";
        response.owner = "stevenMieMie";
        response.url = "https://github.com/GeekGhost/Ghost";
        response.contributors = new ArrayList<>();
        AboutBean.User user = new AboutBean.User();
        user.avatar = "https://avatars2.githubusercontent.com/u/15865879";
        response.contributors.add(user);


        my = new AboutBean();
        my.language = "java";
        my.name = "Cre";
        my.description = "当前项目是开源的，参考Ghost开发\\n 使用RxJava2+Retrofit2+Rxbus2等热门技术整合开发\\n\n" +
                "        bug反馈:creidt_yi@163.com \\n\n" +
                "        QQ：874951370\\n\n" +
                "        Github项目链接";
        my.owner = "creidt03";
        my.url = "https://github.com/credit03";
        my.contributors = new ArrayList<>();
        AboutBean.User user1 = new AboutBean.User();
        user1.avatar = "https://avatars1.githubusercontent.com/u/19606272";
        my.contributors.add(user1);
    }


}
