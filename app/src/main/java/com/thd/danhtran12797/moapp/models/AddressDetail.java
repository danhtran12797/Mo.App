package com.thd.danhtran12797.moapp.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class AddressDetail {

    @SerializedName("ID")
    @Expose
    private Integer id;

    @SerializedName("Title")
    @Expose
    private String title;

    public AddressDetail(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDetail address = (AddressDetail) o;
        return id.equals(address.getId()) &&
                title.equals(address.getTitle());
    }

    public static DiffUtil.ItemCallback<AddressDetail> itemCallback=new DiffUtil.ItemCallback<AddressDetail>() {
        @Override
        public boolean areItemsTheSame(@NonNull AddressDetail oldItem, @NonNull AddressDetail newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull AddressDetail oldItem, @NonNull AddressDetail newItem) {
            return oldItem.equals(newItem);
        }
    };
}
