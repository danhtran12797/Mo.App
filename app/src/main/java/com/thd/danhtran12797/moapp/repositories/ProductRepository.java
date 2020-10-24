package com.thd.danhtran12797.moapp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.thd.danhtran12797.moapp.api.ProductApi;
import com.thd.danhtran12797.moapp.api.ProductService;
import com.thd.danhtran12797.moapp.models.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    private ProductApi productApi;

    public ProductRepository() {
        productApi = ProductService.getProductApi();
    }

    public LiveData<Product> getProduct(String id) {
        MutableLiveData<Product> productMutableLiveData = new MutableLiveData<>();
        productApi.GetProduct(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                productMutableLiveData.setValue(response.body());
                Log.d("AAA", "onResponse: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                productMutableLiveData.setValue(null);
                Log.d("AAA", "onFailure: " + t.getMessage());
            }
        });
        return productMutableLiveData;
    }

    public MutableLiveData<String> updateProduct(String id_pro, String name_pro, String image_pro, String price,
                                                 String quantity, String spec, String material, String thickness,
                                                 String width, String length, String color, String adh_force, String elas,
                                                 String charac, String unit, String bearing, String exp_date) {

        MutableLiveData<String> inserMultableLivaData = new MutableLiveData<>();
        productApi.UpdateProduct(id_pro, name_pro, image_pro, price, quantity, spec, material, thickness, width, length, color,
                adh_force, elas, charac, unit, bearing, exp_date)
                .enqueue(new Callback<String>() {
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

    public MutableLiveData<String> deleteProduct(String productId) {
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        productApi.DeleteProduct(productId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful())
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
