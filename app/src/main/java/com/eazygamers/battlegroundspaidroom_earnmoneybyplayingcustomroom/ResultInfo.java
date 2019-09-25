package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

public class ResultInfo {
    private String rank,kill,winning,playerName;

    public ResultInfo() {
    }

    public ResultInfo(String rank, String kill, String winning, String playerName) {
        this.rank = rank;
        this.kill = kill;
        this.winning = winning;
        this.playerName = playerName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getKill() {
        return kill;
    }

    public void setKill(String kill) {
        this.kill = kill;
    }

    public String getWinning() {
        return winning;
    }

    public void setWinning(String winning) {
        this.winning = winning;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
