package cn.nubia.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import cn.nubia.interfaces.IOnGestureListener;
import cn.nubia.util.GestureDetectorManager;

/**
 * Created by JiangYu on 2015/9/30.
 */
public abstract class BaseGestureActivity extends Activity {
    protected GestureDetectorManager mGestureDetectorManager  = GestureDetectorManager.getInstance();
    protected GestureDetector gestureDetector ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestureDetector = new GestureDetector(this, mGestureDetectorManager);
        mGestureDetectorManager.setOnGestureListener(new IOnGestureListener() {
            @Override
            public void finishActivity() {
                finish();
            }
        });
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return  gestureDetector.onTouchEvent(event);
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent){
        super.dispatchTouchEvent(motionEvent); //让Activity响应触碰事件
        gestureDetector.onTouchEvent(motionEvent); //让GestureDetector响应触碰事件
        return false;
    }

}
