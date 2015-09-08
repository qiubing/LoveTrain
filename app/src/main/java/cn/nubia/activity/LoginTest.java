package cn.nubia.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by 胡立 on 2015/9/7.
 */
public class LoginTest extends Activity {
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_test);
        Button butonAsAdmin = (Button)findViewById(R.id.asAdmin);
        Button buttonAsClient = (Button)findViewById(R.id.asClient);

        butonAsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginTest.this, MainAdminActivity.class));
                LoginTest.this.finish();
            }
        });

        buttonAsClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginTest.this, MainClientActivity.class));
                LoginTest.this.finish();
            }
        });

    }
}
