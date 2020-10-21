package com.thd.danhtran12797.moapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thd.danhtran12797.moapp.models.Product;
import com.thd.danhtran12797.moapp.repositories.CategoryRepository;

import java.io.File;
import java.util.List;

public class CategoryViewModel extends ViewModel {

    private CategoryRepository categoryRepository;
    private MutableLiveData<Boolean> isChangeData = new MutableLiveData<>();

    public CategoryViewModel(){
        categoryRepository = new CategoryRepository();
        isChangeData.setValue(false);
    }

    public LiveData<Boolean> getIsChangeData() {
        return isChangeData;
    }

    public void setIsChangeData() {
        isChangeData.setValue(!isChangeData.getValue());
    }

    public LiveData<List<Product>> getProducts(String categoryId){
        return categoryRepository.getProducts(categoryId);
    }

    public LiveData<String> uploadImage(File file, String to){
        return categoryRepository.uploadImage(file, to);
    }

    public LiveData<String> insertCategory(String groupId, String nameCate, String imageCate){
        return categoryRepository.insertCategory(groupId, nameCate, imageCate);
    }

    public LiveData<String> updateCategory(String id, String nameCate, String imageCate){
        return categoryRepository.updateCategory(id, nameCate, imageCate);
    }

    public LiveData<String> deleteCategory(String categoryId){
        return categoryRepository.deleteCategory(categoryId);
    }

    public LiveData<String> insertProduct(String id_cate, String name_pro, String image_pro, String price,
                                          String quantity, String spec, String material, String thickness,
                                          String width, String length, String color, String adh_force, String elas,
                                          String charac, String unit, String bearing, String exp_date){

        return categoryRepository.insertProduct(id_cate, name_pro, image_pro, price, quantity, spec,
                material, thickness, width, length, color, adh_force, elas, charac, unit, bearing, exp_date);
    }

}