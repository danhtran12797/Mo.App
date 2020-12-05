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
import java.util.List;

import static com.thd.danhtran12797.moapp.utils.Constants.BASE_IMAGE_PRO_URL;

public class Product implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("spec")
    @Expose
    private String spec;

    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("material")
    @Expose
    private String material;

    @SerializedName("width")
    @Expose
    private String width;

    @SerializedName("length")
    @Expose
    private String length;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("adh_force")
    @Expose
    private String adh_force;

    @SerializedName("elas")
    @Expose
    private String elas;

    @SerializedName("charac")
    @Expose
    private String charac;

    @SerializedName("exp_date")
    @Expose
    private String exp_date;

    @SerializedName("bearing")
    @Expose
    private String bearing;

    @SerializedName("unit")
    @Expose
    private String unit;

    @SerializedName("thickness")
    @Expose
    private String thickness;

    @SerializedName("image_detail")
    @Expose
    private List<ImageDetail> imageDetail = null;

    private boolean isAddPro;

    public boolean isAddPro() {
        return isAddPro;
    }

    public void setAddPro(boolean addPro) {
        isAddPro = addPro;
    }

    public Product() {
        this.isAddPro = false;
        this.id = "";
        this.name = "";
    }

    public String getImageFirst() {
        if (imageDetail != null && imageDetail.size() > 0){
            return imageDetail.get(0).getImage();
        }
        return null;
    }

    public List<ImageDetail> getImageDetail() {
        return imageDetail;
    }

    public void setImageDetail(List<ImageDetail> imageDetail) {
        this.imageDetail = imageDetail;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAdh_force() {
        return adh_force;
    }

    public void setAdh_force(String adh_force) {
        this.adh_force = adh_force;
    }

    public String getElas() {
        return elas;
    }

    public void setElas(String elas) {
        this.elas = elas;
    }

    public String getCharac() {
        return charac;
    }

    public void setCharac(String charac) {
        this.charac = charac;
    }

    public Product(String name, String spec) {
        this.name = name;
        this.spec = spec;
    }

    @BindingAdapter("android:productImage")
    public static void loadImage(ImageView imageView, String imageUrl) {
        if (imageUrl == null)
            return;
        Glide.with(imageView)
                .load(BASE_IMAGE_PRO_URL + imageUrl)
                .placeholder(R.drawable.default_placeholder_image)
                .error(R.drawable.error_image)
                .into(imageView);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return name.equals(product.name) &&
                price.equals(product.price) &&
                imageDetail.equals(product.imageDetail);
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(id, name, spec, image, price, quantity, material, width, length, color, adh_force, elas, charac, exp_date);
//    }

    public static DiffUtil.ItemCallback<Product> itemCallback = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.equals(newItem);
        }
    };

}