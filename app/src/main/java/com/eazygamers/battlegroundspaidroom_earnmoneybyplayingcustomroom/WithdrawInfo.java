package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

public class WithdrawInfo {
    private String name,bKashNumber,amount,currency,email,uid,method,appVersion;

    public WithdrawInfo() {
    }

    WithdrawInfo(String name, String bKashNumber, String amount, String currency, String email, String uid, String method,String appVersion) {
        this.name = name;
        this.bKashNumber = bKashNumber;
        this.amount = amount;
        this.currency = currency;
        this.email = email;
        this.uid = uid;
        this.method=method;
        this.appVersion=appVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getbKashNumber() {
        return bKashNumber;
    }

    public void setbKashNumber(String bKashNumber) {
        this.bKashNumber = bKashNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
