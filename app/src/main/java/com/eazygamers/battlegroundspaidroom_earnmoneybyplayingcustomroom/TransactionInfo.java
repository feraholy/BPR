package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

public class TransactionInfo {
    private String pay_status,epw_txnid,pay_time,amount;

    TransactionInfo(String pay_status, String epw_txnid, String pay_time, String amount) {
        this.pay_status = pay_status;
        this.epw_txnid = epw_txnid;
        this.pay_time = pay_time;
        this.amount = amount;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getEpw_txnid() {
        return epw_txnid;
    }

    public void setEpw_txnid(String epw_txnid) {
        this.epw_txnid = epw_txnid;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
