package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

public class DepositInfo {
    private String name,txnId,amount,email,currency,uid,appVersion;

    public DepositInfo() {
    }

    public DepositInfo(String name, String txnId, String amount, String email, String currency,String uid,String appVersion) {
        this.name = name;
        this.txnId = txnId;
        this.currency=currency;
        this.amount = amount;
        this.email = email;
        this.uid=uid;
        this.appVersion=appVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
