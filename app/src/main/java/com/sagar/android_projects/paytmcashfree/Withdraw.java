package com.sagar.android_projects.paytmcashfree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sagar.android_projects.paytmcashfree.pojo.User;
import com.sagar.android_projects.paytmcashfree.util.KeyWords;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/**
 * Created by SAGAR KUMAR NAYAK on 2nd NOV 2017.
 * this is the withdraw activity
 */
public class Withdraw extends AppCompatActivity {

    private TextView textViewCurrentBalance;
    private TextView textViewInstruction;
    private EditText editTextMobileNumber;
    @SuppressWarnings("FieldCanBeLocal")
    private Button buttonRequest;

    FirebaseDatabase database;
    DatabaseReference refForUser;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            setTitle(String.valueOf("Withdraw"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewCurrentBalance = findViewById(R.id.textview_current_balance_value_heading_withdraw);
        textViewInstruction = findViewById(R.id.textview_min_bal_instruction_withdraw);
        editTextMobileNumber = findViewById(R.id.edit_text_mobile_number_withdraw);
        buttonRequest = findViewById(R.id.button_req_for_withdraw);

        database = FirebaseDatabase.getInstance();
        refForUser = database.getReference("User")
                .child(getSharedPreferences(KeyWords.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                        .getString(KeyWords.LOGGED_IN_MOBILE_NUMBER, ""));
        refForUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                textViewCurrentBalance.setText(String.valueOf(user.getCurrentBalance() + " INR"));
                if (user.getCurrentBalance() < 100) {
                    textViewInstruction.setVisibility(View.VISIBLE);
                } else {
                    textViewInstruction.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextMobileNumber.getText().length() == 0) {
                    Toasty.error(Withdraw.this, "Please enter mobile number").show();
                    return;
                }
                if (!editTextMobileNumber.getText().toString().matches("^[789]\\d{9}$")) {
                    Toasty.error(Withdraw.this, "PLease enter a valid mobile number").show();
                    return;
                }
                if (user.getCurrentBalance() < 100) {
                    Toasty.info(Withdraw.this,
                            "Minimum balance for withdraw is 100 INR", 3000).show();
                    return;
                }
                String reqDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                        + "/"
                        + Calendar.getInstance().get(Calendar.MONTH) + 1
                        + "/"
                        + Calendar.getInstance().get(Calendar.YEAR);

                user.setWithdrawRequestDate(reqDate);
                user.setReqMobileNumber(editTextMobileNumber.getText().toString().trim());

                refForUser.setValue(user);

                Toasty.success(Withdraw.this,
                        "Your withdraw request has been sent to admin. money will be sent to paytm number " +
                                user.getReqMobileNumber() + " soon", 5000).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Withdraw.this, Dashboard.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(Withdraw.this, Dashboard.class));
            finish();
        }
        return true;
    }

}
