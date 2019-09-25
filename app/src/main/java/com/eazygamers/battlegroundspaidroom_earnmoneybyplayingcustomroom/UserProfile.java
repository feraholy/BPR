package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

public class UserProfile {
    private String firstName,lastName,userName,email,mobileNumber,password,uid,balance,currency,pubgUserName,freefireUserName,rosUserName,winningBalance,depositStatus;
    public UserProfile()
    {

    }

    public UserProfile(String firstName, String lastName, String userName, String email, String mobileNumber, String password, String uid, String currency) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.uid = uid;
        this.currency = currency;
        this.balance="0";
        this.winningBalance="0";

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPubgUserName() {
        return pubgUserName;
    }

    public void setPubgUserName(String pubgUserName) {
        this.pubgUserName = pubgUserName;
    }

    public String getFreefireUserName() {
        return freefireUserName;
    }

    public void setFreefireUserName(String freefireUserName) {
        this.freefireUserName = freefireUserName;
    }

    public String getWinningBalance() {
        return winningBalance;
    }

    public void setWinningBalance(String winningBalance) {
        this.winningBalance = winningBalance;
    }

    public String getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(String depositStatus) {
        this.depositStatus = depositStatus;
    }
}
