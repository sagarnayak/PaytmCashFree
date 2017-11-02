package com.sagar.android_projects.paytmcashfree.pojo;

/**
 * Created by sagar on 11/2/2017.
 * this is the pojo for setting up the user.
 */
public class User {
    private String name;
    private String email;
    private String mobile;
    private String dob;
    private double currentBalance;
    private String withdrawRequestDate;

    public User() {
    }

    public User(String name, String email, String mobile, String dob, double currentBalance, String withdrawRequestDate) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.dob = dob;
        this.currentBalance = currentBalance;
        this.withdrawRequestDate = withdrawRequestDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getWithdrawRequestDate() {
        return withdrawRequestDate;
    }

    public void setWithdrawRequestDate(String withdrawRequestDate) {
        this.withdrawRequestDate = withdrawRequestDate;
    }
}
