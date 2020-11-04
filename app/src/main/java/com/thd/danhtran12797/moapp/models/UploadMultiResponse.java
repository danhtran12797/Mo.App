package com.thd.danhtran12797.moapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadMultiResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("name_images")
    @Expose
    private List<String> name_images = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getName_images() {
        return name_images;
    }

    public void setName_images(List<String> name_images) {
        this.name_images = name_images;
    }
}
