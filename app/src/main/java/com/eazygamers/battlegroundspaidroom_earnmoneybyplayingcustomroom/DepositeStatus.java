package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

public class DepositeStatus {
    public int people,amount;

    public DepositeStatus() {
    }

    public DepositeStatus(int people, int amount) {
        this.people = people;
        this.amount = amount;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
