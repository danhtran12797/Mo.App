package com.thd.danhtran12797.moapp.models;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thd.danhtran12797.moapp.R;

import java.io.Serializable;

import static com.thd.danhtran12797.moapp.utils.Constants.BASE_IMAGE_CATE_URL;

public class Category implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;

    private boolean isAddCate;

    public boolean isAddCate() {
        return isAddCate;
    }

    public void setAddCate(boolean addCate) {
        isAddCate = addCate;
    }

    public Category(){
        this.isAddCate=false;
        this.id = "";
        this.name = "";
        this.image = "";
    }

    public Category(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @BindingAdapter("android:categoryImage")
    public static void loadImage(ImageView imageView, String imageUrl){
        if(imageUrl==null)
            return;
        Glide.with(imageView)
                .load(BASE_IMAGE_CATE_URL+imageUrl)
                .placeholder(R.drawable.default_placeholder_image)
                .error(R.drawable.error_image)
                .fitCenter()
                .into(imageView);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return  id.equals(category.getId())&&
                name.equals(category.getName())&&
                image.equals(category.getImage())&&
                isAddCate==category.isAddCate();
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(id, name, image);
//    }

    public static DiffUtil.ItemCallback<Category> itemCallback=new DiffUtil.ItemCallback<Category>() {
        @Override
        public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.equals(newItem);
        }
    } ;

}