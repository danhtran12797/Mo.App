package com.thd.danhtran12797.moapp;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.io.Serializable;
import java.util.Objects;

public class Customer implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String classify;
    private String kilomet;

    public Customer(String id, String name, String phone, String address, String classify, String kilomet) {
        this.name = name;
        this.phone = phone;
        this.classify = classify;
        this.kilomet = kilomet;
        this.address = address;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return name.equals(customer.name) &&
                kilomet.equals(customer.kilomet) &&
                address.equals(customer.address) &&
                phone.equals(customer.phone) &&
                classify.equals(customer.classify);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, address, classify, kilomet);
    }

    public static DiffUtil.ItemCallback<Customer> itemCallback = new DiffUtil.ItemCallback<Customer>() {
        @Override
        public boolean areItemsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
            return oldItem.equals(newItem);
        }
    };
}
