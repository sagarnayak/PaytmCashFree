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
    private String lastEarningDate;
    private double todayEarning;

    @SuppressWarnings("unused")
    public User() {
    }

    public User(String name, String email, String mobile, String dob, double currentBalance,
                String withdrawRequestDate, String reqMobileNumber, String lastEarningDate,
                double todayEarning) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.dob = dob;
        this.currentBalance = currentBalance;
        this.withdrawRequestDate = withdrawRequestDate;
        this.reqMobileNumber = reqMobileNumber;
        this.lastEarningDate = lastEarningDate;
        this.todayEarning = todayEarning;
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

    public String getReqMobileNumber() {
        return reqMobileNumber;
    }

    public void setReqMobileNumber(String reqMobileNumber) {
        this.reqMobileNumber = reqMobileNumber;
    }

    public String getLastEarningDate() {
        return lastEarningDate;
    }

    public void setLastEarningDate(String lastEarningDate) {
        this.lastEarningDate = lastEarningDate;
    }

    public double getTodayEarning() {
        return todayEarning;
    }

    public void setTodayEarning(double todayEarning) {
        this.todayEarning = todayEarning;
    }
}
