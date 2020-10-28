package com.thd.danhtran12797.moapp.models;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Customer implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("class")
    @Expose
    private String classCus;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;

    public String getCity() {
        String[] a = address.split(",");
        return a[3];
    }

    public String getDistrict() {
        String[] a = address.split(",");
        return a[2];
    }

    public String getWard() {
        String[] a = address.split(",");
        return a[1];
    }

    public String getStreet() {
        String[] a = address.split(",");
        return a[0];
    }

    public Customer() {
        this.classCus = "0";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassCus() {
        return classCus;
    }

    public void setClassCus(String classCus) {
        this.classCus = classCus;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return name.equals(customer.name) &&
                phone.equals(customer.phone) &&
                address.equals(customer.address) &&
                phone.equals(customer.phone) &&
                classCus.equals(customer.getClassCus());
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
