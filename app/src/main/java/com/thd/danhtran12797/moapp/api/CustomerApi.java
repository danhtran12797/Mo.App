package com.thd.danhtran12797.moapp.api;

import com.thd.danhtran12797.moapp.models.Customer;
import com.thd.danhtran12797.moapp.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CustomerApi {
    @GET("GetTotalPageCus.php")
    Call<String> GetTotalPageCustomer(@Query("search_name") String search_name);

    @GET("GetCustomer.php")
    Call<List<Customer>> GetCustomer(@Query("search_name") String search_name, @Query("page") int page);

    @FormUrlEncoded
    @POST("InsertCustomer.php")
    Call<String> InsertCustomer(@Field("name") String name, @Field("class") String classCus, @Field("phone") String phone, @Field("address") String address);

    @FormUrlEncoded
    @POST("UpdateCustomer.php")
    Call<String> UpdateCustomer(@Field("id") String id, @Field("name") String name, @Field("class") String classCus, @Field("phone") String phone, @Field("address") String address);

    @FormUrlEncoded
    @POST("DeleteCustomer.php")
    Call<String> DeleteCustomer(@Field("id") String id);
}
