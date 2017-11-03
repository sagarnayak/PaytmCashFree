package com.sagar.android_projects.paytmcashfree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

/**
 * Created by SAGAR KUMAR NAYAK on 3rd NOV 2017.
 * the admin login activity
 */
public class AdminLogin extends AppCompatActivity {

    EditText editTextMasterPassword;
    Button buttonAdminLogin;

    FirebaseDatabase database;
    DatabaseReference refForAdmin;
    ValueEventListener valueEventListener;

    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            setTitle("Admin Login");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        database = FirebaseDatabase.getInstance();
        refForAdmin = database.getReference("Admin").child("Password");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                password = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        refForAdmin.addValueEventListener(valueEventListener);

        editTextMasterPassword = findViewById(R.id.edit_text_master_password);
        buttonAdminLogin = findViewById(R.id.button_admin_login);

        buttonAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextMasterPassword.getText().length() == 0) {
                    Toasty.error(AdminLogin.this, "Enter Master Password").show();
                    return;
                }
                if (editTextMasterPassword.getText().toString().trim().equals(password)) {
                    gotoWithdrawReqList();
                } else {
                    Toasty.error(AdminLogin.this, "Wrong Master Password").show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(AdminLogin.this, LoginActivity.class));
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminLogin.this, LoginActivity.class));
        finish();
    }

    private void gotoWithdrawReqList() {
        startActivity(new Intent(AdminLogin.this, WithDrawRequestList.class));
        finish();
    }
}
