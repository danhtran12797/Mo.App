package com.thd.danhtran12797.moapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thd.danhtran12797.moapp.models.Category;
import com.thd.danhtran12797.moapp.models.Product;
import com.thd.danhtran12797.moapp.repositories.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private CategoryRepository categoryRepository;
    private MutableLiveData<Boolean> isChangeData = new MutableLiveData<>();

    public CategoryViewModel() {
        categoryRepository = new CategoryRepository();
        isChangeData.setValue(false);
    }

    public LiveData<Boolean> getIsChangeData() {
        return isChangeData;
    }

    public void setIsChangeData() {
        isChangeData.setValue(!isChangeData.getValue());
    }

    public LiveData<List<Category>> getCategories() {
        return categoryRepository.getCategories();
    }

    public LiveData<String> insertCategory(String name_cate) {
        return categoryRepository.insertCategory(name_cate);
    }

}
