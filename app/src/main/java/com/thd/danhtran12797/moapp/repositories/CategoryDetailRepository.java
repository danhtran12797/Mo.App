package com.thd.danhtran12797.moapp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.thd.danhtran12797.moapp.api.ProductApi;
import com.thd.danhtran12797.moapp.api.ProductService;
import com.thd.danhtran12797.moapp.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDetailRepository {
    private ProductApi productApi;

    public CategoryDetailRepository() {
        productApi = ProductService.getProductApi();
    }

    public LiveData<List<Product>> getProducts(String id_cate) {
        MutableLiveData<List<Product>> mutableLiveData = new MutableLiveData<>();
        productApi.GetCategoryDetail(id_cate).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public LiveData<String> updateCategory(String id_cate, String name_cate) {
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        productApi.UpdateCategory(id_cate, name_cate).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("AAA", "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public LiveData<String> deleteCategory(String id_cate) {
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        productApi.DeleteCategory(id_cate).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("AAA", "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }
}
