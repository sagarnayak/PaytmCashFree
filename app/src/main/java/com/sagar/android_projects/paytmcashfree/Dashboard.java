package com.sagar.android_projects.paytmcashfree;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import com.sagar.android_projects.paytmcashfree.util.CalendarUtil;
import com.sagar.android_projects.paytmcashfree.util.CustomExcetptionHandler;
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
    TextView textViewContact;
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
    DatabaseReference refForParams;
    ValueEventListener valueEventListener;
    ValueEventListener valueEventListenerToCreditMoney;

    User userLoggedIn;

    InterstitialAd interstitialAd;
    InterstitialAd interstitialAdForSmallPrice;

    private double dailyLimit;

    private double dailyBannerThreshold = 1.8;

    private boolean readyToClickAd = false;

    private boolean smallPriceAdClosed = true;

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
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAdForSmallPrice = new InterstitialAd(Dashboard.this);
        interstitialAdForSmallPrice.setAdUnitId(KeyWords.INTERSTITIAL_ADD_ID);
        interstitialAdForSmallPrice.loadAd(new AdRequest.Builder().build());

        interstitialAdForSmallPrice.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                smallPriceAdClosed = true;
                readyToClickAd = true;
                interstitialAdForSmallPrice.loadAd(new AdRequest.Builder().build());
            }
        });

        drawerLayout = findViewById(R.id.drawerlayout);
        appCompatImageViewRupee = findViewById(R.id.appcompatimageview_rupee_dashboard);
        textViewCurrentBalNav = findViewById(R.id.textview_current_balance_value);
        textViewWithdraw = findViewById(R.id.textview_withdraw);
        textViewAboutApp = findViewById(R.id.textview_about_app);
        textViewContact = findViewById(R.id.textview_contact);
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

        refForParams = database.getReference("params").child("dailyLimit");

        refForParams.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dailyLimit = Double.parseDouble(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userLoggedIn = dataSnapshot.getValue(User.class);

                updateBalanceOnActionBar(String.valueOf(userLoggedIn.getCurrentBalance()));
                textViewCurrentBalNav.setText(String.valueOf(userLoggedIn.getCurrentBalance() + " INR"));

                readyToClickAd = true;
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

        textViewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, ContactUs.class));
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
                showMediumAd();
            }
        });

        textViewClickToEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMediumAd();
            }
        });

        checkIfConnectedToInternet();

        Thread.setDefaultUncaughtExceptionHandler(new CustomExcetptionHandler(Environment.getExternalStorageDirectory().getPath()));
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
        rotate3DAnimation =
                new Rotate3dAnimation(0f, 360f, centerX, centerY,
                        0f, true);
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

    private boolean checkIfAllowedToEarn() {
        if (userLoggedIn.getLastEarningDate().equals("")) {
            userLoggedIn.setLastEarningDate(CalendarUtil.getToday());
            userLoggedIn.setTodayEarning(0.0);
            refForUser.setValue(userLoggedIn);
            return true;
        } else {
            if (userLoggedIn.getLastEarningDate().equals(CalendarUtil.getToday())) {
                if (userLoggedIn.getTodayEarning() < dailyLimit) {
                    return true;
                }
            } else {
                userLoggedIn.setLastEarningDate(CalendarUtil.getToday());
                userLoggedIn.setTodayEarning(0.0);
                refForUser.setValue(userLoggedIn);
                return true;
            }
        }
        return false;
    }

    private void showMediumAd() {
        if (!readyToClickAd) {
            return;
        }
        readyToClickAd = false;
        if (!checkIfAllowedToEarn()) {
            Toasty.error(Dashboard.this, "Today's limit for " + dailyLimit + " INR earning is over. please try tomorrow.", 10000).show();
            readyToClickAd = true;
            return;
        }
        if (interstitialAdForSmallPrice.isLoaded()) {
            interstitialAdForSmallPrice.show();
            smallPriceAdClosed = false;
        } else {
            Toasty.error(Dashboard.this, "Ad not loaded yet. please try after few seconds", 10000).show();
            readyToClickAd = true;
            return;
        }

        Toasty.info(Dashboard.this, "Wait for 10 seconds to earn 0.1 INR", 5000).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            creditMoneyToUser(0.1);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void creditMoneyToUser(final double amount) {
        if (smallPriceAdClosed) {
            Toasty.error(Dashboard.this, "Ad Closed before 10 seconds.", 4000).show();
            return;
        }
        valueEventListenerToCreditMoney = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateNumberOfClick();
                refForUser.removeEventListener(valueEventListenerToCreditMoney);
                if (shouldAccountBeCredited()) {
                    userLoggedIn = dataSnapshot.getValue(User.class);
                    userLoggedIn.setCurrentBalance(userLoggedIn.getCurrentBalance() + amount);
                    userLoggedIn.setCurrentBalance(round(userLoggedIn.getCurrentBalance(), 2));
                    userLoggedIn.setTodayEarning(userLoggedIn.getTodayEarning() + amount);
                    userLoggedIn.setTodayEarning(round(userLoggedIn.getTodayEarning(), 2));
                    checkForImproperMoneyCredit(userLoggedIn.getCurrentBalance());
                    refForUser.setValue(userLoggedIn);
                    updateBalanceOnActionBar(String.valueOf(userLoggedIn.getCurrentBalance()));
                    textViewCurrentBalNav.setText(String.valueOf(userLoggedIn.getCurrentBalance() + " INR"));
                    Toasty.info(Dashboard.this, amount + " credited to you account.", 4000).show();
                }
                if (userLoggedIn.getTodayEarning() == dailyBannerThreshold)
                    tryToShowLargerAdd();
                readyToClickAd = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        refForUser.addValueEventListener(valueEventListenerToCreditMoney);
    }

    private void tryToShowLargerAdd() {
        final AlertDialog alertDialog = new AlertDialog.Builder(Dashboard.this).create();
        alertDialog.setTitle("Bonus");
        alertDialog.setMessage("Click on a ad and get 0.2 INR instantly");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Get it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showLargerAdd();
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "I dont want bonus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showLargerAdd() {
        Toasty.info(Dashboard.this, "Click on ad to earn 0.1 INR", 5000).show();
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                creditMoneyToUser(0.2);
            }
        });
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Toasty.info(Dashboard.this, "Please wait. ad is loading").show();
        }
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private void updateNumberOfClick() {
        getSharedPreferences(KeyWords.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .edit()
                .putInt(KeyWords.NUMBER_OF_CLICKS,
                        getSharedPreferences(KeyWords.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                                .getInt(KeyWords.NUMBER_OF_CLICKS, 0) + 1)
                .apply();
    }

    private boolean shouldAccountBeCredited() {
        if (getSharedPreferences(KeyWords.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .getInt(KeyWords.NUMBER_OF_CLICKS, 0) == 3) {
            getSharedPreferences(KeyWords.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                    .edit()
                    .putInt(KeyWords.NUMBER_OF_CLICKS, 0)
                    .apply();
            return false;
        }
        return true;
    }

    private void checkForImproperMoneyCredit(final double currentBalance) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                    if (userLoggedIn.getCurrentBalance() != currentBalance) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userLoggedIn.setCurrentBalance(currentBalance);
                                refForUser.setValue(userLoggedIn);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
