package com.thd.danhtran12797.moapp.viewmodels;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thd.danhtran12797.moapp.models.Product;
import com.thd.danhtran12797.moapp.models.UploadMultiResponse;
import com.thd.danhtran12797.moapp.repositories.ProductRepository;

import java.util.List;

public class ProductViewModel extends ViewModel {
    private ProductRepository productRepository;
    private MutableLiveData<Boolean> isChangeData = new MutableLiveData<>();

    public ProductViewModel() {
        productRepository = new ProductRepository();
        isChangeData.setValue(false);
    }

    public LiveData<Boolean> getIsChangeData() {
        return isChangeData;
    }

    public void setIsChangeData() {
        isChangeData.setValue(!isChangeData.getValue());
    }

    public LiveData<Product> getProduct(String id) {
        return productRepository.getProduct(id);
    }

    public LiveData<String> updateProduct(String id_pro, String name_pro, String price,
                                          String quantity, String spec, String material, String thickness,
                                          String width, String length, String color, String adh_force, String elas,
                                          String charac, String unit, String bearing, String exp_date) {

        return productRepository.updateProduct(id_pro, name_pro, price, quantity, spec,
                material, thickness, width, length, color,
                adh_force, elas, charac, unit, bearing, exp_date);
    }

    public LiveData<String> deleteProduct(String productId) {
        return productRepository.deleteProduct(productId);
    }

    public LiveData<UploadMultiResponse> uploadFileMultilPart(List<Bitmap> lstBitmap){
        return productRepository.uploadFileMultilPart(lstBitmap);
    }

    public MutableLiveData<String> insertImage(String id, String json) {
        return productRepository.insertImage(id, json);
    }

    public MutableLiveData<String> updateImage(String id, String image) {
        return productRepository.updateImage(id, image);
    }

}
