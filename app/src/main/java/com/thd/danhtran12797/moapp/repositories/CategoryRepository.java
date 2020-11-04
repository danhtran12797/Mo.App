package com.thd.danhtran12797.moapp.repositories;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.thd.danhtran12797.moapp.api.ProductApi;
import com.thd.danhtran12797.moapp.api.ProductService;
import com.thd.danhtran12797.moapp.models.Product;
import com.thd.danhtran12797.moapp.utils.ImageResizer;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private ProductApi productApi;

    public CategoryRepository() {
        productApi = ProductService.getProductApi();
    }

    public LiveData<List<Product>> getProducts(String id) {
        MutableLiveData<List<Product>> producMutableLiveData = new MutableLiveData<>();
        productApi.GetCategory(id).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                // volley, retrofit, new Asyntask
                producMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                producMutableLiveData.setValue(null);
            }
        });
        return producMutableLiveData;
    }

    public LiveData<String> uploadImage(Uri uri, String to) {
        MutableLiveData<String> nameMutableLiveData = new MutableLiveData<>();

        Bitmap fullSizeBitmap = BitmapFactory.decodeFile(uri.getPath());

        Bitmap reduceBitmap = ImageResizer.reduceBitmapSize(fullSizeBitmap, 480000);

        File reduceFile = ImageResizer.saveBitmap(reduceBitmap);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), reduceFile);
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("text/plain"), to);

        MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file", reduceFile.getName(), requestBody);
        productApi.UploadImage(body, requestBody1).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful())
                    nameMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("AAA", "onFailure: uploadImage: " + t.getMessage());
                nameMutableLiveData.setValue(null);
            }
        });

        return nameMutableLiveData;
    }

    public MutableLiveData<String> insertCategory(String groupId, String nameCate, String imageCate) {
        MutableLiveData<String> inserMultableLivaData = new MutableLiveData<>();
        productApi.InsertCategory(groupId, nameCate, imageCate).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                inserMultableLivaData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                inserMultableLivaData.setValue(null);
                Log.d("AAA", "onFailure: " + t.getMessage());
            }
        });

        return inserMultableLivaData;
    }

    public MutableLiveData<String> updateCategory(String id, String nameCate, String imageCate) {
        MutableLiveData<String> updateMutableLiveData = new MutableLiveData<>();
        productApi.UpdateCategory(id, nameCate, imageCate).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                updateMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("AAA", "onFailure: " + t.getMessage());
                updateMutableLiveData.setValue(null);
            }
        });
        return updateMutableLiveData;
    }

    public MutableLiveData<String> insertProduct(String id_cate, String name_pro, String price,
                                                 String quantity, String spec, String material, String thickness,
                                                 String width, String length, String color, String adh_force, String elas,
                                                 String charac, String unit, String bearing, String exp_date, String json_images) {

        MutableLiveData<String> inserMultableLivaData = new MutableLiveData<>();
        productApi.InsertProduct(id_cate, name_pro, price, quantity, spec, material, thickness, width, length, color,
                adh_force, elas, charac, unit, bearing, exp_date, json_images)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful())
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

    public MutableLiveData<String> deleteCategory(String categoryId) {
        MutableLiveData<String> deleteMutableLiveData = new MutableLiveData<>();
        productApi.DeleteCategory(categoryId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful())
                    deleteMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("AAA", "onFailure: " + t.getMessage());
                deleteMutableLiveData.setValue(null);
            }
        });
        return deleteMutableLiveData;
    }

    public LiveData<String> getTotalPage(String search_name) {
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        productApi.GetTotalPage(search_name).enqueue(new Callback<String>() {
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

    // search_name, view_type, page
    public LiveData<List<Product>> getProductFromSearch(String search_name, int view_type, int page) {
        MutableLiveData<List<Product>> mutableLiveData = new MutableLiveData<>();
        productApi.GetProductFromSearch(search_name, view_type, page).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful())
                    mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("AAA", "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }
}
