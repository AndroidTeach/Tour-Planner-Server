package com.sultani.erfan.tourplannerserver.Model;

/**
 * Created by Erfan on 12/15/2017.
 */

public class GeneratePack {

    private String token,price,name,status,phone;

    public GeneratePack() {


    }

    public GeneratePack(String token, String price, String name, String status, String phone) {
        this.token = token;
        this.price = price;
        this.name = name;
        this.status = "0";
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
