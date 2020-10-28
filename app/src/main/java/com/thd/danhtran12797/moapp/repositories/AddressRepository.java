package com.thd.danhtran12797.moapp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.thd.danhtran12797.moapp.api.AddressApi;
import com.thd.danhtran12797.moapp.api.AddressService;
import com.thd.danhtran12797.moapp.models.AddressDetail;
import com.thd.danhtran12797.moapp.models.ListProvin;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressRepository {
    private AddressApi addressApi;
    public AddressRepository() {
        addressApi= AddressService.getAddressApi();
    }

    public LiveData<ListProvin> getAllProvin(){
        MutableLiveData<ListProvin> mutableLiveData = new MutableLiveData<>();
        addressApi.GetAllProvin().enqueue(new Callback<ListProvin>() {
            @Override
            public void onResponse(Call<ListProvin> call, Response<ListProvin> response) {
                if(response.isSuccessful())
                    mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ListProvin> call, Throwable t) {
                Log.d("AAA", "onFailure: "+t.getMessage());
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public LiveData<List<AddressDetail>> getAllDistrict(int id) {
        MutableLiveData<List<AddressDetail>> mutableLiveData = new MutableLiveData<>();
        addressApi.GetAllDistrict(id).enqueue(new Callback<List<AddressDetail>>() {
            @Override
            public void onResponse(Call<List<AddressDetail>> call, Response<List<AddressDetail>> response) {
                if(response.isSuccessful())
                    mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<AddressDetail>> call, Throwable t) {
                Log.d("AAA", "onFailure: "+t.getMessage());
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public LiveData<List<AddressDetail>> getAllWard(int id) {
        MutableLiveData<List<AddressDetail>> mutableLiveData = new MutableLiveData<>();
        addressApi.GetAllWard(id).enqueue(new Callback<List<AddressDetail>>() {
            @Override
            public void onResponse(Call<List<AddressDetail>> call, Response<List<AddressDetail>> response) {
                if(response.isSuccessful())
                    mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<AddressDetail>> call, Throwable t) {
                Log.d("AAA", "onFailure: "+t.getMessage());
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }
}
