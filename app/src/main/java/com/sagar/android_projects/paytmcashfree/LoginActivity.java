package com.sagar.android_projects.paytmcashfree;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sagar.android_projects.paytmcashfree.pojo.User;
import com.sagar.android_projects.paytmcashfree.util.KeyWords;
import com.sagar.android_projects.paytmcashfree.util.NetworkUtil;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/**
 * Created by SAGAR KUMAR NAYAK on 2nd NOV 2017.
 * this is the login activity
 */
public class LoginActivity extends AppCompatActivity {

    @SuppressWarnings("FieldCanBeLocal")
    private Button buttonLogin;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextMobile;
    private EditText editTextDob;
    @SuppressWarnings("FieldCanBeLocal")
    private TextView textViewAdminLogin;

    FirebaseDatabase database;
    DatabaseReference refForUser;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = findViewById(R.id.button_login);
        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextMobile = findViewById(R.id.edit_text_mobile_number);
        editTextDob = findViewById(R.id.edit_text_date_of_birth);
        textViewAdminLogin = findViewById(R.id.textview_admin_login);


        database = FirebaseDatabase.getInstance();

        editTextDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerForDOB();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedForLogin();
            }
        });

        textViewAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, AdminLogin.class));
                finish();
            }
        });

        checkIfLoggedIn();

        isStoragePermissionGranted();
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("sdfgbdsfb", "Permission is granted");
                return true;
            } else {

                Log.v("sdfgbdsfb", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("sdfgbdsfb", "Permission is granted");
            return true;
        }
    }

    private void proceedForLogin() {
        hideSoftKeyBoard();
        if (!NetworkUtil.isConnected(LoginActivity.this)) {
            Toasty.error(LoginActivity.this, "Please connect to internet.").show();
            return;
        }
        if (editTextName.getText().length() == 0) {
            Toasty.error(LoginActivity.this, "Please enter name").show();
            return;
        }
        if (editTextEmail.getText().length() == 0) {
            Toasty.error(LoginActivity.this, "Please enter email-id").show();
            return;
        }
        if (!editTextEmail.getText().toString().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            Toasty.error(LoginActivity.this, "PLease enter a valid email").show();
            return;
        }
        if (editTextMobile.getText().length() == 0) {
            Toasty.error(LoginActivity.this, "Please enter mobile number").show();
            return;
        }
        if (!editTextMobile.getText().toString().matches("^[789]\\d{9}$")) {
            Toasty.error(LoginActivity.this, "PLease enter a valid mobile number").show();
            return;
        }
        if (editTextDob.getText().length() == 0) {
            Toasty.error(LoginActivity.this, "Please enter date of birth").show();
            return;
        }
        sendDataToFirebase(new User(
                editTextName.getText().toString().trim(),
                editTextEmail.getText().toString().trim(),
                editTextMobile.getText().toString().trim(),
                editTextDob.getText().toString().trim(),
                0.0f,
                "",
                "",
                "",
                0.0));
    }

    private void showDatePickerForDOB() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(LoginActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
                    Toasty.error(LoginActivity.this, "PLease enter valid date of birth").show();
                    return;
                }
                editTextDob.setText(String.valueOf(dayOfMonth + "/" + (month + 1) + "/" + year));
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void hideSoftKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null)
                return;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void sendDataToFirebase(final User user) {
        refForUser = database.getReference("User").child(user.getMobile());
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    if (dataSnapshot.getValue(User.class) != null) {
                        user.setCurrentBalance(user.getCurrentBalance() + dataSnapshot.getValue(User.class).getCurrentBalance());
                        user.setWithdrawRequestDate(dataSnapshot.getValue(User.class).getWithdrawRequestDate());
                        user.setReqMobileNumber(dataSnapshot.getValue(User.class).getReqMobileNumber());
                        user.setLastEarningDate(dataSnapshot.getValue(User.class).getLastEarningDate());
                        user.setTodayEarning(user.getTodayEarning() + dataSnapshot.getValue(User.class).getTodayEarning());
                    }
                }
                sendDataToFirebaseAfterEdit(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        refForUser.addValueEventListener(valueEventListener);
    }

    private void sendDataToFirebaseAfterEdit(User user) {
        refForUser.removeEventListener(valueEventListener);
        refForUser.setValue(user);
        getSharedPreferences(KeyWords.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .edit()
                .putString(KeyWords.LOGGED_IN_MOBILE_NUMBER, user.getMobile())
                .apply();
        getSharedPreferences(KeyWords.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .edit()
                .putBoolean(KeyWords.LOGGED_IN_STATUS, true)
                .apply();
        gotoDashboard();
    }

    private void gotoDashboard() {
        startActivity(new Intent(LoginActivity.this, Dashboard.class));
        finish();
    }

    private void checkIfLoggedIn() {
        if (getSharedPreferences(KeyWords.SHARED_PREFERENCE_NAME, MODE_PRIVATE).getBoolean(KeyWords.LOGGED_IN_STATUS, false)) {
            gotoDashboard();
        }
    }

}
