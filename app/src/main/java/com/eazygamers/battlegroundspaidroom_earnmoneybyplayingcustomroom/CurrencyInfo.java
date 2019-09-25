package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

public class CurrencyInfo {
   private float USD,INR;

    public CurrencyInfo() {
    }

    public CurrencyInfo(float USD, float INR) {
        try {
            this.USD = USD;
            this.INR = INR;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public float getUSD() {
        return USD;
    }

    public void setUSD(float USD) {
        this.USD = USD;
    }

    public float getINR() {
        return INR;
    }

    public void setINR(float INR) {
        this.INR = INR;
    }
}
