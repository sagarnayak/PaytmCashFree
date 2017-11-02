package com.sagar.android_projects.paytmcashfree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by SAGAR KUMAR NAYAK on 2nd NOV 2017.
 * this is the login activity
 */
public class LoginActivity extends AppCompatActivity {

    @SuppressWarnings("FieldCanBeLocal")
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Dashboard.class));
                finish();
            }
        });
    }

}
