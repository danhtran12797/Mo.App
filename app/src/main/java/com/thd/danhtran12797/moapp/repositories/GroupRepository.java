package com.thd.danhtran12797.moapp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.thd.danhtran12797.moapp.api.ProductApi;
import com.thd.danhtran12797.moapp.api.ProductService;
import com.thd.danhtran12797.moapp.models.Group;
import com.thd.danhtran12797.moapp.models.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupRepository {

    private ProductApi productApi;

    public GroupRepository(){
        productApi= ProductService.getProductApi();
    }

    public MutableLiveData<List<Group>> getGroups(){
        MutableLiveData<List<Group>> groupMutableLiveData = new MutableLiveData<>();
        productApi.GetGroupAll().enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                Log.d("AAA", "onResponse: "+response.body().get(0).getName());
                if(response.isSuccessful()){
                    groupMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                groupMutableLiveData.setValue(null);
                Log.d("AAA", "onFailure: "+t.getMessage());
            }
        });
        Log.d("AAA", "loadGroups: HELLO KAKAK");
        return groupMutableLiveData;
    }

    public LiveData<String> insertGroup(String nameGroup){
        MutableLiveData<String> inserMultableLivaData = new MutableLiveData<>();
        productApi.InsertGroup(nameGroup).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                inserMultableLivaData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("AAA", "onFailure: "+t.getMessage());
                inserMultableLivaData.setValue(null);
            }
        });

        return inserMultableLivaData;
    }

}
