package com.guoyi.gyvideo.widget;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guoyi.gyvideo.MyApplication;
import com.guoyi.gyvideo.R;
import com.guoyi.gyvideo.bean.VideoInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * 自动滚动ViewPager
 *
 * @author Administrator
 */
public class RollViewPager extends ViewPager {
    private String TAG = "RollViewPager";
    private Context context;
    private int currentItem;
    private List<VideoInfo> uriList;
    private ArrayList<View> dots;
    private TextView title;
    private ArrayList<String> titles;
    private int[] resImageIds;
    private int dot_focus_resId;
    private int dot_normal_resId;
    private OnPagerClickCallback onPagerClickCallback;
    private boolean isShowResImage = false;
    MyOnTouchListener myOnTouchListener;
    ViewPagerTask viewPagerTask;
    private PagerAdapter adapter;
    private static int depalyTime = 2000;

    /**
     * 触摸时按下的位置
     */
    PointF downP = new PointF();
    /**
     * 触摸时当前的位置
     */
    PointF curP = new PointF();
    private int abc = 1;
    private float mLastMotionX;
    private float mLastMotionY;

    private boolean flag = false;
    private long start = 0;

    public class MyOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            curP.x = event.getX();
            curP.y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:// 在触摸按下时
                    start = System.currentTimeMillis();
                    handler.removeCallbacksAndMessages(null);
                    // 记录按下时触摸的坐标
                    // 切记不可以 downP = curP ，这样在改变curP的时候，downP也会改变
                    downP.x = event.getX();
                    downP.y = event.getY();
                    // 此句代码是为了告知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
                    // getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_MOVE:// 在触摸移动时
                    handler.removeCallbacks(viewPagerTask);
                    Log.i("d", (curP.x - downP.x) + "----" + (curP.y - downP.y));

                    break;
                case MotionEvent.ACTION_CANCEL:// 在触摸取消时
                    // 启动滚动
                    startRoll();
                    break;
                case MotionEvent.ACTION_UP:// 在触摸弹起时
                    downP.x = event.getX();
                    downP.y = event.getY();
                    long duration = System.currentTimeMillis() - start;
                    if (duration <= 500 && downP.x == curP.x) {
                        // 回调
                        if (onPagerClickCallback != null)
                            onPagerClickCallback.onPagerClick(v, currentItem);
                    } else {
                    }
                    // 启动滚动
                    startRoll();
                    break;
            }
            return true;
        }
    }

    /*
     * 快速滑动事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                abc = 1;
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (abc == 1) {
                    if (Math.abs(x - mLastMotionX) < Math.abs(y - mLastMotionY)) {
                        abc = 0;
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    public RollViewPager(Context context) {
        super(context);
        this.context = context;
    }

    public RollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * 构造器
     *
     * @param context              上下文
     * @param dots                 页面点集合
     * @param dot_focus_resId      选中的状态图片资源
     * @param dot_normal_resId     非选中的状态图片资源
     * @param onPagerClickCallback 回调接口
     */
    public RollViewPager(Context context, ArrayList<View> dots,
                         int dot_focus_resId, int dot_normal_resId,
                         OnPagerClickCallback onPagerClickCallback) {
        super(context);
        this.context = context;
        this.dots = dots;
        this.dot_focus_resId = dot_focus_resId;
        this.dot_normal_resId = dot_normal_resId;
        this.onPagerClickCallback = onPagerClickCallback;
        viewPagerTask = new ViewPagerTask();
        myOnTouchListener = new MyOnTouchListener();

    }

    /**
     * @param dots                 页面点集合
     * @param dot_focus_resId      选中的状态图片资源
     * @param dot_normal_resId     非选中的状态图片资源
     * @param onPagerClickCallback 回调接口
     */
    public void setRollViewPager(ArrayList<View> dots,
                                 int dot_focus_resId, int dot_normal_resId,
                                 OnPagerClickCallback onPagerClickCallback) {

        this.dots = dots;
        this.dot_focus_resId = dot_focus_resId;
        this.dot_normal_resId = dot_normal_resId;
        this.onPagerClickCallback = onPagerClickCallback;
        viewPagerTask = new ViewPagerTask();
        myOnTouchListener = new MyOnTouchListener();

    }


    /**
     * 设置联网的URL集合，自动加载条目
     *
     * @param uriList
     */
    public void setUriList(List<VideoInfo> uriList) {
        isShowResImage = false;
        this.uriList = uriList;
    }

    /**
     * 设置使用本地模式，不启用联网
     *
     * @param resImageIds
     */
    public void setResImageIds(int[] resImageIds) {
        isShowResImage = true;
        this.resImageIds = resImageIds;
    }

    /**
     * 通知适配器内容改变
     */
    public void notifyDataChange() {
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取点集合
     *
     * @return
     */
    public ArrayList<View> getDots() {
        return dots;
    }

    /**
     * 设置点集合
     */
    public void setDots(ArrayList<View> dots) {
        this.dots = dots;
    }

    /**
     * 设置标题
     *
     * @param title  标题组件
     * @param titles 标题集合
     */
    public void setTitle(TextView title, ArrayList<String> titles) {
        this.title = title;
        this.titles = titles;
        if (title != null && titles != null && titles.size() > 0)
            title.setText(titles.get(0));//
    }

    /**
     * 获取轮播图跳转时间
     *
     * @return
     */
    public int getDepalyTime() {
        return depalyTime;
    }

    /**
     * 设置轮播图跳转时间
     */
    public void setDepalyTime(int depalyTime) {
        this.depalyTime = depalyTime;
    }


    /**
     * ViewPager定时器
     *
     * @author Administrator
     */
    public class ViewPagerTask implements Runnable {
        @Override
        public void run() {
            if (uriList != null) {
                currentItem = (currentItem + 1)
                        % (isShowResImage ? resImageIds.length : uriList.size());
            } else if (resImageIds != null) {
                currentItem = (currentItem + 1) % resImageIds.length;
            }
            if (uriList != null || resImageIds != null)
                handler.obtainMessage().sendToTarget();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            RollViewPager.this.setCurrentItem(currentItem);
            startRoll();
        }
    };

    /**
     * 是否设置了适配器
     */
    private boolean hasSetAdapter = false;

    private static long lastTime = 0;


    private boolean isStart = false;

    /**
     * 启动ViewPager自动滚动
     */

    public synchronized void startRoll() {

        if (!hasSetAdapter) {
            hasSetAdapter = true;
            this.setOnPageChangeListener(new MyOnPageChangeListener());
            adapter = new ViewPagerAdapter();
            this.setAdapter(adapter);
        }
        if (uriList != null || resImageIds != null) {
            long l = System.currentTimeMillis();
            /**
             * 防止多次触发,depalyTime S内只能触发一次
             */
            if (l > lastTime + (depalyTime)) {
                lastTime = l;
                isStart = true;
                handler.postDelayed(viewPagerTask, depalyTime);
            }
        }
    }


    public synchronized void restartRoll() {
        if (dots != null && dots.size() > 0) {
            for (View v : dots) {
                v.setBackgroundResource(dot_normal_resId);
            }
            dots.get(currentItem).setBackgroundResource(dot_focus_resId);
        }

        if (!hasSetAdapter) {
            hasSetAdapter = true;
            this.setOnPageChangeListener(new MyOnPageChangeListener());
            adapter = new ViewPagerAdapter();
            this.setAdapter(adapter);
        }
        if (uriList != null || resImageIds != null) {
            isStart = true;
            lastTime = System.currentTimeMillis();
            handler.postDelayed(viewPagerTask, depalyTime);
        }


    }

    /**
     * 停止自动滚动，在onDestroy中使用
     */
    public synchronized void stopRoll() {
        if (viewPagerTask != null) {
            handler.removeCallbacks(viewPagerTask);
        }
        handler.removeCallbacksAndMessages(null);

        this.removeAllViews();
        isStart = false;
        currentItem = 0;
        lastTime = 0;
        adapter = null;
        hasSetAdapter = false;
    }


    public boolean isStart() {
        return isStart;
    }

    /**
     * 监听器
     *
     * @author Administrator
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {
        int oldPosition = 0;

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            if (title != null)
                title.setText(titles.get(position));

            if (dots != null && dots.size() > 0) {
                dots.get(position).setBackgroundResource(dot_focus_resId);
                dots.get(oldPosition).setBackgroundResource(dot_normal_resId);
            }
            oldPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    /**
     * 适配器
     *
     * @author Administrator
     */
    class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            // 判断使用模式，uriList！=null和isShowResImage==false时，则使用网络加载图片
            if (uriList != null && !isShowResImage) {
                return uriList.size();
            } else if (isShowResImage) {// isShowResImage==true ，则使用本地的图片
                return resImageIds.length;
            } else {
                return 0;
            }

        }

        @Override
        public Object instantiateItem(View container, final int position) {
            View view = View.inflate(context, R.layout.roll_viewpager_item, null);
            ((ViewPager) container).addView(view);
            if (myOnTouchListener != null)
                view.setOnTouchListener(myOnTouchListener);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            if (isShowResImage) {
                imageView.setImageResource(resImageIds[position]);
            } else if (uriList != null) {
                VideoInfo videoInfo = uriList.get(position);
                MyApplication.logD("加载滚动图片 " + videoInfo.pic);
                Glide.with(context).load(videoInfo.pic).placeholder(R.mipmap.default_320).into(imageView);
            }
            return view;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        handler.removeCallbacksAndMessages(null);
        isStart = false;
        super.onDetachedFromWindow();
    }

    /**
     * 回调接口
     *
     * @author Administrator
     */
    public interface OnPagerClickCallback {
        public abstract void onPagerClick(View v, int position);
    }

    public VideoInfo getItem(int postition) {
        if (uriList.size() > postition) {
            return uriList.get(postition);
        }
        return null;

    }
}
