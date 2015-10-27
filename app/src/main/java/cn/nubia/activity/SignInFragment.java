package cn.nubia.activity;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SignInFragment extends Fragment {
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/*和管理员共用同一个页面*/
        rootView = inflater.inflate(R.layout.activity_admin_all_course, container, false);
        initViews();
        initEvents();
        return rootView;
    }

    /*初始化View*/
    private  void initViews() {

    }

    private  void initEvents() {

    }


}