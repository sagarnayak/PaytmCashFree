package com.sagar.android_projects.paytmcashfree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sagar.android_projects.paytmcashfree.adapter.AdapterWithdrawReq;
import com.sagar.android_projects.paytmcashfree.pojo.User;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Created by SAGAR KUMAR NAYAK on 3rd NOV 2017.
 * the withdraw request list activity.
 */
public class WithDrawRequestList extends AppCompatActivity {


    private AppCompatImageView appCompatImageViewNoReq;
    private TextView textViewNoReq;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference refForAdmin;

    ArrayList<User> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw_request_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            setTitle("Withdraw Request List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        database = FirebaseDatabase.getInstance();
        refForAdmin = database.getReference("User");
        refForAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userArrayList = new ArrayList<>();
                progressBar.setVisibility(View.GONE);
                long value = dataSnapshot.getChildrenCount();
                if (value == 0) {
                    noDataAvailable();
                    return;
                } else {
                    hideNoDataAvailable();
                }
                for (DataSnapshot data :
                        dataSnapshot.getChildren()) {
                    if (!String.valueOf(data.getValue(User.class).getWithdrawRequestDate()).equals("")) {
                        userArrayList.add(data.getValue(User.class));
                    }
                }
                if (userArrayList.size() == 0) {
                    noDataAvailable();
                } else {
                    hideNoDataAvailable();
                    setUpRecyclerview();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toasty.error(WithDrawRequestList.this, "Can not get data due to some error. please try later", 5000).show();
                gotoAdminLogin();
            }
        });

        appCompatImageViewNoReq = findViewById(R.id.appcompatimageview_no_req);
        textViewNoReq = findViewById(R.id.textview_no_req);
        recyclerView = findViewById(R.id.recyclerview_withdraw_req);
        progressBar = findViewById(R.id.progress_withdraw_req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_withdraw_list, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        gotoAdminLogin();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.admin_logout) {
            gotoAdminLogin();
        } else if (item.getItemId() == android.R.id.home) {
            gotoAdminLogin();
        }
        return true;
    }

    private void gotoAdminLogin() {
        startActivity(new Intent(WithDrawRequestList.this, AdminLogin.class));
        finish();
    }

    private void noDataAvailable() {
        recyclerView.setLayoutManager(new LinearLayoutManager(WithDrawRequestList.this));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(WithDrawRequestList.this, R.anim.layout_anim_slide_up);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setAdapter(new AdapterWithdrawReq(new ArrayList<User>()));
        appCompatImageViewNoReq.setVisibility(View.VISIBLE);
        textViewNoReq.setVisibility(View.VISIBLE);
    }

    private void hideNoDataAvailable() {
        appCompatImageViewNoReq.setVisibility(View.GONE);
        textViewNoReq.setVisibility(View.GONE);
    }

    private void setUpRecyclerview() {
        recyclerView.setLayoutManager(new LinearLayoutManager(WithDrawRequestList.this));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(WithDrawRequestList.this, R.anim.layout_anim_slide_up);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setAdapter(new AdapterWithdrawReq(userArrayList));
    }

}
