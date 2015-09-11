package cn.nubia.util;

import android.view.GestureDetector;
import android.view.MotionEvent;

import cn.nubia.interfaces.IOnGestureListener;

/**
 * Created by 胡立 on 2015/9/10.
 */
public class GestureDetectorManager implements  GestureDetector.OnGestureListener{
    private IOnGestureListener iOnGestureListener;
    //手指向右滑动时的最小速度
    private static final int XSPEED_MIN = 200;

    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 150;

    //使用一个变量来缓存曾经创建的实例
    private static GestureDetectorManager instance;
    //将构造器使用private修饰，隐藏该构造器
    private GestureDetectorManager(){}
    //提供一个静态方法，用于返回Singleton实例
    //该方法可以加入自定义的控制，保证只产生一个Singleton对象
    public static GestureDetectorManager getInstance() {

        //如果instance为null，表明还不曾创建Singleton对象
        //如果instance不为null，则表明已经创建了Singleton对象，
        //将不会重新创建新的实例
        if (instance == null){
            //创建一个Singleton对象，并将其缓存起来
            instance = new GestureDetectorManager();
        }
        return instance;
    }

    public void setOnGestureListener(IOnGestureListener iOnGestureListener) {
        this.iOnGestureListener = iOnGestureListener;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if ((e2.getX() - e1.getX() > XDISTANCE_MIN) && (velocityX > XSPEED_MIN)) {
            iOnGestureListener.finishActivity();
        }
        return false;
    }
}
