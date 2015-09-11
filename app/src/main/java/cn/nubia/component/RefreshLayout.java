package cn.nubia.component;


import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ListView;

import cn.nubia.activity.R;

/**
 * transplant by WJ on 2015/9/1.
 * 继承自SwipeRefreshLayout,从而实现滑动到底部时上拉加载更多的功能.
 */

/*huhu，SwipeRefreshLayout类注释
SwipeRefreshLayout是Google的support v419.1版本的library更新的一个下拉刷新组件，俗称“彩虹条”，继承于ViewGroup，
但是官方版本只有下拉刷新而没有上拉加载更多，需进行继承扩展。也可以使用第三方库，如PullToRefresh,ActionBar-PullToRefresh等完成下拉上拉加载更多功能

在xml文件中引用android.support.v4.widget.SwipeRefreshLayout控件，在里面可以放置任何一个控件，例如ListView，gridview等
<android.support.v4.widget.SwipeRefreshLayout
xmlns:android="http://schemas.android.com/apk/res/android"
android:id="@+id/swipe_container"
android:layout_width="match_parent"
android:layout_height="match_parent">

<ScrollView
android:layout_width="match_parent"
android:layout_height="wrap_content">
<ListView
android:id="@+id/listview"
android:layout_width="match_parent"
android:layout_height="match_parent">
</ListView>
</ScrollView>
</android.support.v4.widget.SwipeRefreshLayout>


public class MainActivity extends ActionBarActivity implements OnRefreshListener{

        private SwipeRefreshLayout swipeRefreshLayout;
        Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        swipeRefreshLayout.setRefreshing(false);
                }
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swip);
                swipeRefreshLayout.setOnRefreshListener(this);
                ////设置刷新时动画的颜色，可以设置4个
                swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,android.R.color.holo_green_light,android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);
        }

        @Override
        public void onRefresh() {
                handler.sendEmptyMessageDelayed(1, 5000);
        }
}

在上面的onRefresh()函数中实现获取数据功能以及更新数据，当更新完数据后，调用swipeRefreshLayout.setRefreshing(false);来关闭刷新。
或者，SwipeRefreshLayout有方法postDelayed(new Runnable()),Handle的postDelayed(new Runnable())是在主线程运行的
      swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
              @Override
              public void onRefresh() {
                  tv.setText("正在刷新");
                 swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                     public void run() {
                         loadData(34);
                         swipeRefreshLayout.setRefreshing(false);
                    }
                 }, 6000);
             }
       });

*/



public class RefreshLayout extends SwipeRefreshLayout implements
        AbsListView.OnScrollListener {

        /**
         * huhu，是否触发操作的临界值
         */

        private int mTouchSlop;
        /**
         * listview实例
         */
        private ListView mListView;

        /**
         * huhu，自定义实现上拉加载更多功能的接口
         */
        private OnLoadListener mOnLoadListener;

        /**
         * ListView的加载中footer
         */
        private View mListViewFooter;
        /**
         * ListView的加载中网络错误
         */
        private View mNetworkLoadFailedView;

        /**
         * 按下时的y坐标
         */
        private int mYDown;
        /**
         * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
         */
        private int mLastY;
        /**
         * 是否在加载中 ( 上拉加载更多 )
         */
        private boolean isLoading = false;
        // 均匀旋转动画
        private RotateAnimation refreshingAnimation;
        View loadingView;
        /**
         * @param context
         */
        public RefreshLayout(Context context) {
                this(context, null);
        }

        public RefreshLayout(Context context, AttributeSet attrs) {
                super(context, attrs);
                //huhu,getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件，得到系统的默认值
                mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

                mListViewFooter = LayoutInflater.from(context).inflate(
                        R.layout.listview_footer, null, false);
                mNetworkLoadFailedView = LayoutInflater.from(context).inflate(
                        R.layout.network_load_failed, null, false);

                loadingView = mListViewFooter.findViewById(R.id.loading_icon);
                refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                        context, R.anim.rotating);
                // 添加匀速转动动画
                LinearInterpolator lir = new LinearInterpolator();
                refreshingAnimation.setInterpolator(lir);
        }

        public View getNetworkLoadFailView() {
                return mNetworkLoadFailedView;
        }

        //huhu，属于ViewGroup的方法，当View分配所有子元素的大小和位置时触发该方法
        @Override
        protected void onLayout(boolean changed, int left, int top, int right,
                                int bottom) {
                super.onLayout(changed, left, top, right, bottom);

                // 初始化ListView对象
                if (mListView == null) {
                        getListView();
                }
        }

        /**
         * 获取ListView对象
         */
        private void getListView() {
                int childs = getChildCount();
                if (childs > 0) {
                        View childView = getChildAt(0);
                        if (childView instanceof ListView) {
                                mListView = (ListView) childView;
                                // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                                //huhu，这个不是和dispatchTouchEvent方法功能重复了？
                                mListView.setOnScrollListener(this);
                        }
                }
        }

        /*
         * (non-Javadoc)
         *
         * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
         */
        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
                final int action = event.getAction();

                switch (action) {
                        //huhu,A pressed gesture has started, the motion contains the initial starting location
                        case MotionEvent.ACTION_DOWN:
                                // 按下
                                mYDown = (int) event.getRawY();
                                break;
                        //huhu,  A change has happened during apress gesture (between {@link #ACTION_DOWN} and {@link #ACTION_UP}).
                        //The motion contains the most recent point,
                        //该参数主要用于描述轨迹的，不适合当前应用，应舍弃
                        //还是有比较好，这样可以更快的进行加载，缺点是需要不停的进行判断，资源消耗加大
                        case MotionEvent.ACTION_MOVE:
                                // 移动
                                mLastY = (int) event.getRawY();
                                break;
                        //huhu,A pressed gesture has finished, the
                        //motion contains the final release location
                        case MotionEvent.ACTION_UP:
                                //huhu，add  mLastY = (int) event.getRawY();
                                mLastY = (int) event.getRawY();
                                if (canLoad()) {
                                        loadData();
                                }
                                break;
                        default:
                                break;
                }

                return super.dispatchTouchEvent(event);
        }

        /**
         * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
         *
         * @return
         */
        private boolean canLoad() {
                return isBottom() && !isLoading && isPullUp();
        }

        /**
         * 判断是否到了最底部
         */
        private boolean isBottom() {

                if (mListView != null && mListView.getAdapter() != null) {
                        return mListView.getLastVisiblePosition() == (mListView
                                .getAdapter().getCount() - 1);
                }
                return false;
        }

        /**
         * 是否是上拉操作
         *
         * @return
         */
        private boolean isPullUp() {
                return (mYDown - mLastY) >= mTouchSlop;
        }

        /**
         * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
         */
        private void loadData() {
                if (mOnLoadListener != null) {
                        // 设置状态
                        setLoading(true);
                        mOnLoadListener.onLoad();
                }
        }

        /**
         * @param loading
         */
        public void setLoading(boolean loading) {
                isLoading = loading;
                if (isLoading) {
                        mListView.removeFooterView(mNetworkLoadFailedView);
                        mListView.addFooterView(mListViewFooter);
                        loadingView.startAnimation(refreshingAnimation);
                } else {
                        mListView.removeFooterView(mListViewFooter);
                        loadingView.clearAnimation();
                        mYDown = 0;
                        mLastY = 0;
                }
        }

        public void showNetworkFailedHeader(boolean loading) {
                if (loading && mListView.getHeaderViewsCount()== 0) {
                        mListView.addHeaderView(mNetworkLoadFailedView);
                }else {
                        mListView.addFooterView(mNetworkLoadFailedView);
                }
        }

        public void showNetworkFailedFooter(boolean loading) {
                if (loading && mListView.getFooterViewsCount()== 0) {
                        mListView.addFooterView(mNetworkLoadFailedView);
                } else {
                        mListView.removeFooterView(mNetworkLoadFailedView);
                }
        }
        /**
         * @param loadListener
         */
        public void setOnLoadListener(OnLoadListener loadListener) {
                mOnLoadListener = loadListener;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        //该方法貌似没用
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
                // 滚动时到了最底部也可以加载更多
                if (canLoad()) {
                        loadData();
                }
        }

        /**
         * 加载更多的监听器
         *
         * @author mrsimple
         */
        public  interface OnLoadListener {
                void onLoad();
        }
}

