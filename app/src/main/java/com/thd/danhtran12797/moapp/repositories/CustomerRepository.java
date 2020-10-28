package com.thd.danhtran12797.moapp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.thd.danhtran12797.moapp.api.CustomerApi;
import com.thd.danhtran12797.moapp.api.CustomerService;
import com.thd.danhtran12797.moapp.models.Customer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerRepository {
    private CustomerApi customerApi;

    public CustomerRepository(){
        customerApi = CustomerService.getCustomerApi();
    }

    public LiveData<String> getTotalPageCus(String search_name) {
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        customerApi.GetTotalPageCustomer(search_name).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful())
                    mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("AAA", "onFailure Customer: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public LiveData<List<Customer>> getCustomers(String search_name, int page){
        MutableLiveData<List<Customer>> mutableLiveData = new MutableLiveData<>();
        customerApi.GetCustomer(search_name, page).enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful())
                    mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Log.d("AAA", "onFailure Customer: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public LiveData<String> updateCustomer(String id, String name, String phone, String address, String classCus){
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        customerApi.UpdateCustomer(id, name, classCus, phone, address).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful())
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

    public LiveData<String> insertCustomer(String name, String phone, String address, String classCus){
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        customerApi.InsertCustomer(name, classCus, phone, address).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful())
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

    public LiveData<String> deleteCustomer(String id){
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        customerApi.DeleteCustomer(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful())
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
