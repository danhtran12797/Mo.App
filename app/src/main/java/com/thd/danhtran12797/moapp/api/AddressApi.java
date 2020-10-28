package com.thd.danhtran12797.moapp.api;

import com.thd.danhtran12797.moapp.models.AddressDetail;
import com.thd.danhtran12797.moapp.models.ListProvin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AddressApi {
    @GET("city")
    Call<ListProvin> GetAllProvin();

    @GET("city/{id}/district")
    Call<List<AddressDetail>> GetAllDistrict(@Path("id") int id);

    @GET("district/{id}/ward")
    Call<List<AddressDetail>> GetAllWard(@Path("id") int id);
}
