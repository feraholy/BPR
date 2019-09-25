package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

public class MatchInfo {
    private String entryFee,map,matchName,matchTime,peopleJoined,perKill,type,version,winPrize,uniqueId,roomId,roomPass,liveStram,peopleLimit;
    private boolean playingState,live,resultPublished;

    public MatchInfo() {
    }

    public MatchInfo(String entryFee, String map, String matchName, String matchTime, String peopleJoined, String perKill, String type, String version, String winPrize, boolean playingState, String uniqueId, boolean live, boolean resultPublished,String peopleLimit) {
        this.entryFee = entryFee;
        this.map = map;
        this.matchName = matchName;
        this.matchTime = matchTime;
        this.peopleJoined = peopleJoined;
        this.perKill = perKill;
        this.type = type;
        this.version = version;
        this.winPrize = winPrize;
        this.playingState=playingState;
        this.uniqueId=uniqueId;
        this.live=live;
        this.resultPublished=resultPublished;
        this.peopleLimit=peopleLimit;
    }

    public String getPeopleLimit() {
        return peopleLimit;
    }

    public void setPeopleLimit(String peopleLimit) {
        this.peopleLimit = peopleLimit;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getPeopleJoined() {
        return peopleJoined;
    }

    public void setPeopleJoined(String peopleJoined) {
        this.peopleJoined = peopleJoined;
    }

    public String getPerKill() {
        return perKill;
    }

    public void setPerKill(String perKill) {
        this.perKill = perKill;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getWinPrize() {
        return winPrize;
    }

    public void setWinPrize(String winPrize) {
        this.winPrize = winPrize;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomPass() {
        return roomPass;
    }

    public void setRoomPass(String roomPass) {
        this.roomPass = roomPass;
    }

    public String getLiveStram() {
        return liveStram;
    }

    public void setLiveStram(String liveStram) {
        this.liveStram = liveStram;
    }

    public boolean isPlayingState() {
        return playingState;
    }

    public void setPlayingState(boolean playingState) {
        this.playingState = playingState;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isResultPublished() {
        return resultPublished;
    }

    public void setResultPublished(boolean resultPublished) {
        this.resultPublished = resultPublished;
    }
}
