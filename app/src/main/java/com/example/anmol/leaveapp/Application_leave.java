package com.example.anmol.leaveapp;

public class Application_leave {

    public Application_leave() {
    }

    String from, fromdate, to, todate, status, subject, category, countdays, currentdate;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCountdays() {
        return countdays;
    }

    public void setCountdays(String countdays) {
        this.countdays = countdays;
    }

    public String getCurrentdate() {
        return currentdate;
    }

    public void setCurrentdate(String currentdate) {
        this.currentdate = currentdate;
    }

    public Application_leave(String from, String fromdate, String to, String todate, String status, String subject, String category, String countdays, String currentdate) {
        this.from = from;
        this.fromdate = fromdate;
        this.to = to;
        this.todate = todate;
        this.status = status;
        this.subject = subject;
        this.category = category;
        this.countdays = countdays;
        this.currentdate = currentdate;
    }
}
