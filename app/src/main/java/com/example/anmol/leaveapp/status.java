package com.example.anmol.leaveapp;

public class status {
    String category,state,from,to;

    public status(String category, String state, String from,String to) {
        this.category = category;
        this.state = state;
        this.from = from;
        this.to=to;
    }

    public status() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
