package com.thd.danhtran12797.moapp;

public class Customer {
    private String name;
    private String phone;
    private String address;
    private String classify;
    private String kilomet;

    public Customer(String name, String phone, String address, String classify, String kilomet) {
        this.name = name;
        this.phone = phone;
        this.classify = classify;
        this.kilomet = kilomet;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getKilomet() {
        return kilomet;
    }

    public void setKilomet(String kilomet) {
        this.kilomet = kilomet;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
