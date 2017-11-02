package com.sagar.android_projects.paytmcashfree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by SAGAR KUMAR NAYAK on 3rd NOV 2017.
 * the about app activity
 */
public class AboutApp extends AppCompatActivity {

    @SuppressWarnings("FieldCanBeLocal")
    private TextView textViewAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            setTitle(String.valueOf("About App"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewAdmin = findViewById(R.id.textview_admin);

        textViewAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutApp.this, AdminLogin.class));
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(AboutApp.this, Dashboard.class));
            finish();
        }
        return true;
    }
}
