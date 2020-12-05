package com.thd.danhtran12797.moapp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.thd.danhtran12797.moapp.api.ProductApi;
import com.thd.danhtran12797.moapp.api.ProductService;
import com.thd.danhtran12797.moapp.models.Category;
import com.thd.danhtran12797.moapp.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {

    private ProductApi productApi;

    public CategoryRepository() {
        productApi = ProductService.getProductApi();
    }

    public MutableLiveData<List<Category>> getCategories() {
        MutableLiveData<List<Category>> mutableLiveData = new MutableLiveData<>();
        productApi.GetCategoryAll().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                mutableLiveData.setValue(null);
                Log.d("AAA", "onFailure: " + t.getMessage());
            }
        });
        return mutableLiveData;
    }

    public LiveData<String> insertCategory(String name_cate) {
        MutableLiveData<String> inserMultableLivaData = new MutableLiveData<>();
        productApi.InsertCategory(name_cate).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                inserMultableLivaData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("AAA", "onFailure: " + t.getMessage());
                inserMultableLivaData.setValue(null);
            }
        });

        return inserMultableLivaData;
    }

}
