package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

public class ConfigInfo {
    private int status;
    private String versionCheck;

    public ConfigInfo() {
    }

    public ConfigInfo(int status, String versionCheck) {
        this.status = status;
        this.versionCheck = versionCheck;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVersionCheck() {
        return versionCheck;
    }

    public void setVersionCheck(String versionCheck) {
        this.versionCheck = versionCheck;
    }
}
