package cn.nubia.activity.client;

import android.app.ActivityGroup;
import android.os.Bundle;
import cn.nubia.activity.R;
/**
 * Created by 胡立 on 2015/9/7.
 */
@SuppressWarnings("deprecation")
public class ClientMyActivity extends ActivityGroup {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_client_tab);
    }

}

