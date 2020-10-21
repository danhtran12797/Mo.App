package com.thd.danhtran12797.moapp.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Group {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;

    public Group(String id, String name, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id.equals(group.getId())&&
                name.equals(group.getName())&&
                categories.equals(group.categories);
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(id, name, categories);
//    }

    public static DiffUtil.ItemCallback<Group> itemCallback=new DiffUtil.ItemCallback<Group>() {
        @Override
        public boolean areItemsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
            return oldItem.equals(newItem);
        }
    };

}