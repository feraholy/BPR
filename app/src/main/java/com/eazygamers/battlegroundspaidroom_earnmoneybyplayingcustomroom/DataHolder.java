package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

public class DataHolder {
    private static ConfigInfo configInfo;
    private static MatchInfo matchInfo;
    private static UserProfile userProfile;
    private static CurrencyInfo currencyInfo;
    private static String url;
    private static int runningPubg,runningFreefire;
    private static String currencySign;
    private static String version;
    private static  TransactionInfo transactionInfo;

    static TransactionInfo getTransactionInfo() {
        return transactionInfo;
    }

    static void setTransactionInfo(TransactionInfo transactionInfo) {
        DataHolder.transactionInfo = transactionInfo;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        DataHolder.version = version;
    }

    static ConfigInfo getConfigInfo() {
        return configInfo;
    }

    static void setConfigInfo(ConfigInfo configInfo) {
        DataHolder.configInfo = configInfo;
    }

    static CurrencyInfo getCurrencyInfo() {
        return currencyInfo;
    }

    static void setCurrencyInfo(CurrencyInfo currencyInfo) {
        DataHolder.currencyInfo = currencyInfo;
    }

    static String getCurrencySign() {
        return currencySign;
    }

    static void setCurrencySign(String currencySign) {
        DataHolder.currencySign = currencySign;
    }

    static int getRunningPubg() {
        return runningPubg;
    }

    static void setRunningPubg(int runningPubg) {
        DataHolder.runningPubg = runningPubg;
    }

    static int getRunningFreefire() {
        return runningFreefire;
    }

    static void setRunningFreefire(int runningFreefire) {
        DataHolder.runningFreefire = runningFreefire;
    }


    static String getUrl() {
        return url;
    }

    static void setUrl(String url) {
        DataHolder.url = url;
    }

    static MatchInfo getMatchInfo() {
        return matchInfo;
    }
    static void setMatchInfo(MatchInfo matchInfo) {
        DataHolder.matchInfo = matchInfo;
    }

    public static UserProfile getUserProfile() {
        return userProfile;
    }

    public static void setUserProfile(UserProfile userProfile) {
        DataHolder.userProfile = userProfile;
    }
}
