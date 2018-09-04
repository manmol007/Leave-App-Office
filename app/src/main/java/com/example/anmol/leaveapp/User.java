package com.example.anmol.leaveapp;

public class User {

    String name,email,password,college,designation,city,imageurl;

    public User() {
    }

    public User(String name, String email, String password, String college, String designation, String city, String imageurl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.college = college;
        this.designation = designation;
        this.city = city;
        this.imageurl = imageurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
