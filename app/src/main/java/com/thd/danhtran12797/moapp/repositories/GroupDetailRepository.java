package com.thd.danhtran12797.moapp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.thd.danhtran12797.moapp.api.ProductApi;
import com.thd.danhtran12797.moapp.api.ProductService;
import com.thd.danhtran12797.moapp.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDetailRepository {
    private ProductApi productApi;

    public GroupDetailRepository(){
        productApi = ProductService.getProductApi();
    }

    public LiveData<List<Category>> getCategories(String id){
        MutableLiveData<List<Category>> cateMutableLiveData=new MutableLiveData<>();
        productApi.GetGroupDetail(id).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                cateMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                cateMutableLiveData.setValue(null);
            }
        });
        return cateMutableLiveData;
    }

    public LiveData<String> updateGroup(String idGroup, String nameGroup){
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        productApi.UpdateGroup(idGroup, nameGroup).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("AAA", "onFailure: "+t.getMessage());
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public LiveData<String> deleteGroup(String idGroup){
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        productApi.DeleteGroup(idGroup).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("AAA", "onFailure: "+t.getMessage());
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }
}
