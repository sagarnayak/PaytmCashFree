package com.sagar.android_projects.paytmcashfree;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.rmiri.buttonloading.ButtonLoading;

public class Launcher extends AppCompatActivity {

    private ButtonLoading buttonLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonLoading = findViewById(R.id.buttonPanel);

        buttonLoading.setOnButtonLoadingListener(new ButtonLoading.OnButtonLoadingListener() {
            @Override
            public void onClick() {
                finishLoading();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    void finishLoading() {
        //call setProgress(false) after 5 second
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonLoading.setProgress(false);
            }
        }, 5000);
    }
}
