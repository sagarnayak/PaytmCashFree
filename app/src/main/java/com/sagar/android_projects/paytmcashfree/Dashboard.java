package com.sagar.android_projects.paytmcashfree;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sagar.android_projects.paytmcashfree.pojo.User;
import com.sagar.android_projects.paytmcashfree.util.KeyWords;
import com.sagar.android_projects.paytmcashfree.util.NetworkUtil;
import com.sagar.android_projects.paytmcashfree.util.Rotate3dAnimation;

import es.dmoral.toasty.Toasty;

/**
 * Created by SAGAR KUMAR NAYAK on 2nd NOV 2017.
 * this is the dashboard activity.
 */
public class Dashboard extends AppCompatActivity {

    AppCompatImageView appCompatImageViewRupee;
    TextView textViewCurrentBalNav;
    TextView textViewWithdraw;
    TextView textViewAboutApp;
    TextView textViewLogout;
    AdView addViewBanner;
    TextView textViewClickToEarn;

    Rotate3dAnimation rotate3DAnimation;

    float centerX;
    float centerY;

    Menu menu;
    DrawerLayout drawerLayout;

    FirebaseDatabase database;
    DatabaseReference refForUser;
    ValueEventListener valueEventListener;

    User userLoggedIn;

    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(Dashboard.this, KeyWords.AD_MOB_ID);

        if (getSupportActionBar() != null) {
            setTitle(String.valueOf("Paytm Free Cash"));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_button);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        interstitialAd = new InterstitialAd(Dashboard.this);
        interstitialAd.setAdUnitId(KeyWords.INTERSTITIAL_ADD_ID);

        drawerLayout = findViewById(R.id.drawerlayout);
        appCompatImageViewRupee = findViewById(R.id.appcompatimageview_rupee_dashboard);
        textViewCurrentBalNav = findViewById(R.id.textview_current_balance_value);
        textViewWithdraw = findViewById(R.id.textview_withdraw);
        textViewAboutApp = findViewById(R.id.textview_about_app);
        textViewLogout = findViewById(R.id.textview_logout);
        addViewBanner = findViewById(R.id.adView);
        textViewClickToEarn = findViewById(R.id.textview_click_to_earn);

        AdRequest adRequest = new AdRequest.Builder().build();
        addViewBanner.loadAd(adRequest);

        addViewBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                rotatePicture();
            }
        });

        database = FirebaseDatabase.getInstance();
        refForUser = database
                .getReference("User")
                .child(getSharedPreferences(KeyWords.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                        .getString(KeyWords.LOGGED_IN_MOBILE_NUMBER, ""));

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userLoggedIn = dataSnapshot.getValue(User.class);

                updateBalanceOnActionBar(String.valueOf(userLoggedIn.getCurrentBalance()));
                textViewCurrentBalNav.setText(String.valueOf(userLoggedIn.getCurrentBalance() + " INR"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        refForUser.addValueEventListener(valueEventListener);

        textViewWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Withdraw.class));
                finish();
            }
        });

        textViewAboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, AboutApp.class));
                finish();
            }
        });

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        appCompatImageViewRupee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLargerAdd();
            }
        });

        textViewClickToEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLargerAdd();
            }
        });

        checkIfConnectedToInternet();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    private void rotatePicture() {
        centerX = appCompatImageViewRupee.getWidth() / 2.0f;
        centerY = appCompatImageViewRupee.getHeight() / 2.0f;
        rotate3DAnimation = new Rotate3dAnimation(0f, 360f, centerX, centerY, 0f, true);
        rotate3DAnimation.setDuration(4000);
        rotate3DAnimation.setFillAfter(true);
        rotate3DAnimation.setRepeatMode(Animation.RESTART);
        rotate3DAnimation.setRepeatCount(Animation.INFINITE);
        rotate3DAnimation.setInterpolator(new LinearInterpolator());
        rotate3DAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        appCompatImageViewRupee.startAnimation(rotate3DAnimation);
    }

    private void updateBalanceOnActionBar(final String newBalance) {
        try {
            MenuItem balance = menu.findItem(R.id.current_balance);
            balance.setTitle(String.valueOf(newBalance + " INR"));
        } catch (Exception e) {
            e.printStackTrace();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateBalanceOnActionBar(newBalance);
                            }
                        });
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void logout() {
        getSharedPreferences(KeyWords.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .edit()
                .putBoolean(KeyWords.LOGGED_IN_STATUS, false)
                .apply();
        startActivity(new Intent(Dashboard.this, LoginActivity.class));
        finish();
    }

    private void showLargerAdd() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Toasty.info(Dashboard.this, "Please ait. add is loading").show();
        }
    }

    private void checkIfConnectedToInternet() {
        if (!NetworkUtil.isConnected(Dashboard.this)) {
            final AlertDialog alertDialog = new AlertDialog.Builder(Dashboard.this).create();
            alertDialog.setMessage("Please connect to internet");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.setTitle("Alert");
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }
}
