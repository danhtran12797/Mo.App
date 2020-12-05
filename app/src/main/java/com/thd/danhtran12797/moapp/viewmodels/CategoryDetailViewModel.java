package com.thd.danhtran12797.moapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thd.danhtran12797.moapp.models.Product;
import com.thd.danhtran12797.moapp.repositories.CategoryDetailRepository;

import java.util.List;

public class CategoryDetailViewModel extends ViewModel {
    private CategoryDetailRepository categoryDetailRepository;
    private MutableLiveData<Boolean> isChangeData = new MutableLiveData<>();

    public CategoryDetailViewModel() {
        categoryDetailRepository = new CategoryDetailRepository();
        isChangeData.setValue(false);
    }

    public LiveData<Boolean> getIsChangeData() {
        return isChangeData;
    }

    public void setIsChangeData() {
        isChangeData.setValue(!isChangeData.getValue());
    }

    public LiveData<List<Product>> getProducts(String id_cate) {
        return categoryDetailRepository.getProducts(id_cate);
    }

    public LiveData<String> updateCategory(String id_cate, String name_cate) {
        return categoryDetailRepository.updateCategory(id_cate, name_cate);
    }

    public LiveData<String> deleteCategory(String id_cate) {
        return categoryDetailRepository.deleteCategory(id_cate);
    }
}
