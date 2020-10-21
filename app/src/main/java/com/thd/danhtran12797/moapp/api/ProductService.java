package com.thd.danhtran12797.moapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.thd.danhtran12797.moapp.utils.Constants.BASE_ADDRESS_URL;
import static com.thd.danhtran12797.moapp.utils.Constants.BASE_PRO_URL;
import static com.thd.danhtran12797.moapp.utils.Constants.CONNECTION_TIMEOUT;
import static com.thd.danhtran12797.moapp.utils.Constants.READ_TIMEOUT;
import static com.thd.danhtran12797.moapp.utils.Constants.WRITE_TIMEOUT;

public class ProductService {

    private static OkHttpClient client = new OkHttpClient.Builder()

            // establish connection to server
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)

            // time between each byte read from the server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

            // time between each byte sent to server
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

            .retryOnConnectionFailure(false)

            .build();

    private static Gson gson=new GsonBuilder().setLenient().create();

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(BASE_PRO_URL)
//                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = retrofitBuilder.build();

    private static ProductApi productApi = retrofit.create(ProductApi.class);

    public static ProductApi getProductApi(){
        return productApi;
    }

}
