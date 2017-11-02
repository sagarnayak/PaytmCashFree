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
    private String reqMobileNumber;

    @SuppressWarnings("unused")
    public User() {
    }

    public User(String name, String email, String mobile, String dob, double currentBalance,
                String withdrawRequestDate, String reqMobileNumber) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.dob = dob;
        this.currentBalance = currentBalance;
        this.withdrawRequestDate = withdrawRequestDate;
        this.reqMobileNumber = reqMobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    public String getEmail() {
        return email;
    }

    @SuppressWarnings("unused")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    @SuppressWarnings("unused")
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @SuppressWarnings("unused")
    public String getDob() {
        return dob;
    }

    @SuppressWarnings("unused")
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

    public String getReqMobileNumber() {
        return reqMobileNumber;
    }

    public void setReqMobileNumber(String reqMobileNumber) {
        this.reqMobileNumber = reqMobileNumber;
    }
}
