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
import com.thd.danhtran12797.moapp.models.UploadMultiResponse;
import com.thd.danhtran12797.moapp.utils.ImageResizer;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
                if (response.isSuccessful())
                    productMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                productMutableLiveData.setValue(null);
                Log.d("AAA", "onFailure: " + t.getMessage());
            }
        });
        return productMutableLiveData;
    }

    public MutableLiveData<String> updateProduct(String id_pro, String name_pro, String price,
                                                 String quantity, String spec, String material, String thickness,
                                                 String width, String length, String color, String adh_force, String elas,
                                                 String charac, String unit, String bearing, String exp_date) {

        MutableLiveData<String> inserMultableLivaData = new MutableLiveData<>();
        productApi.UpdateProduct(id_pro, name_pro, price, quantity, spec, material, thickness, width, length, color,
                adh_force, elas, charac, unit, bearing, exp_date)
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

    public LiveData<UploadMultiResponse> uploadFileMultilPart(List<Bitmap> lstBitmap) {
        Log.d("AAA", "uploadFileMultilPart: lstBitmap " + lstBitmap.size());
        MutableLiveData<UploadMultiResponse> mutableLiveData = new MutableLiveData<>();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (int i = 0; i < lstBitmap.size(); i++) {

            Bitmap reduceBitmap = ImageResizer.reduceBitmapSize(lstBitmap.get(i), 480000);

            File reduceFile = ImageResizer.saveBitmap(reduceBitmap);

            // Khởi tạo RequestBody từ những file đã được chọn
//            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
//                    reduceFile);
            // Add thêm request body vào trong builder
            builder.addFormDataPart("uploaded_file[]", reduceFile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), reduceFile));
        }

        MultipartBody requestBody = builder.build();
        productApi.UploadFileMultilPart(requestBody).enqueue(new Callback<UploadMultiResponse>() {
            @Override
            public void onResponse(Call<UploadMultiResponse> call, Response<UploadMultiResponse> response) {
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<UploadMultiResponse> call, Throwable t) {
                Log.d("AAA", "onFailure: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<String> insertImage(String id, String json) {
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        productApi.InsertImage(id, json).enqueue(new Callback<String>() {
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

    public MutableLiveData<String> updateImage(String id_pro, String image) {
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        productApi.UpdateImage(id_pro, image).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful())
                    mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("AAA", "onFailure: updateImage: " + t.getMessage());
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
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
