package com.thd.danhtran12797.moapp.api;

import com.thd.danhtran12797.moapp.models.Category;
import com.thd.danhtran12797.moapp.models.Product;
import com.thd.danhtran12797.moapp.models.UploadMultiResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ProductApi {

    @GET("GetCategoryAll.php")
    Call<List<Category>> GetCategoryAll();

    @GET("GetCategoryDetail.php")
    Call<List<Product>> GetCategoryDetail(@Query("id_cate") String idCate);

//    @GET("GetCategory.php")
//    Call<List<Product>> GetCategory(@Query("id_cate") String id_category);

    @GET("GetProduct.php")
    Call<Product> GetProduct(@Query("id_product") String id_product);

    @FormUrlEncoded
    @POST("InsertCategory.php")
    Call<String> InsertCategory(@Field("name") String name_cate);

    @Multipart
    @POST("UploadImage.php")
    Call<String> UploadImage(@Part MultipartBody.Part photo, @Part("to") RequestBody to);

//    @FormUrlEncoded
//    @POST("InsertCategory.php")
//    Call<String> InsertCategory(@Field("id_group") String id_group, @Field("name_cate") String name_cate, @Field("image_cate") String image_cate);

    @FormUrlEncoded
    @POST("UpdateCategory.php")
    Call<String> UpdateCategory(@Field("id_cate") String id_cate, @Field("name_cate") String name_cate);

    @FormUrlEncoded
    @POST("DeleteCategory.php")
    Call<String> DeleteCategory(@Field("id_cate") String id_cate);

//    @FormUrlEncoded
//    @POST("UpdateCategory.php")
//    Call<String> UpdateCategory(@Field("id_cate") String id_cate, @Field("name_cate") String name_cate, @Field("image_cate") String image_cate);

    @FormUrlEncoded
    @POST("InsertProduct.php")
    Call<String> InsertProduct(@Field("id_cate") String id_cate, @Field("name_pro") String name_pro, @Field("price") String price,
                               @Field("quantity") String quantity, @Field("spec") String spec, @Field("material") String material,
                               @Field("thickness") String thickness, @Field("width") String width, @Field("length") String length, @Field("color") String color,
                               @Field("adh_force") String adh_force, @Field("elas") String elas, @Field("charac") String charac, @Field("unit") String unit,
                               @Field("bearing") String bearing, @Field("exp_date") String exp_date, @Field("json_images") String json_images);

    @FormUrlEncoded
    @POST("UpdateProduct.php")
    Call<String> UpdateProduct(@Field("id_pro") String id_pro, @Field("name_pro") String name_pro, @Field("price") String price,
                               @Field("quantity") String quantity, @Field("spec") String spec, @Field("material") String material,
                               @Field("thickness") String thickness, @Field("width") String width, @Field("length") String length, @Field("color") String color,
                               @Field("adh_force") String adh_force, @Field("elas") String elas, @Field("charac") String charac, @Field("unit") String unit,
                               @Field("bearing") String bearing, @Field("exp_date") String exp_date);

//    @FormUrlEncoded
//    @POST("DeleteCategory.php")
//    Call<String> DeleteCategory(@Field("id_cate") String id_cate);

    @FormUrlEncoded
    @POST("DeleteProduct.php")
    Call<String> DeleteProduct(@Field("id_pro") String id_pro);

    @GET("GetTotalPage.php")
    Call<String> GetTotalPage(@Query("search_name") String search_name);

    @GET("GetProductFromSearch.php")
    Call<List<Product>> GetProductFromSearch(@Query("search_name") String search_name, @Query("view_type") int view_type, @Query("page") int page);

    @POST("UploadFileMultilPart.php")
    Call<UploadMultiResponse> UploadFileMultilPart(@Body RequestBody files);

    @FormUrlEncoded
    @POST("UpdateImage.php")
    Call<String> UpdateImage(@Field("id") String id, @Field("image") String image);

    @FormUrlEncoded
    @POST("InsertImage.php")
    Call<String> InsertImage(@Field("id_pro") String id_pro, @Field("json_images") String json);
}

