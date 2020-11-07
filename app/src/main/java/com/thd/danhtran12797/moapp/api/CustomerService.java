package com.thd.danhtran12797.moapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.thd.danhtran12797.moapp.utils.Constants.BASE_PRO_URL;
import static com.thd.danhtran12797.moapp.utils.Constants.CONNECTION_TIMEOUT;
import static com.thd.danhtran12797.moapp.utils.Constants.READ_TIMEOUT;
import static com.thd.danhtran12797.moapp.utils.Constants.WRITE_TIMEOUT;

public class CustomerService {

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()

            // establish connection to server
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)

            // time between each byte read from the server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

            // time between each byte sent to server
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

            .retryOnConnectionFailure(true)
            .protocols(Arrays.asList(Protocol.HTTP_1_1))
            .build();

    private static Gson gson = new GsonBuilder().setLenient().create();

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(BASE_PRO_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson));
//                    .addConverterFactory(ScalarsConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static CustomerApi customerApi = retrofit.create(CustomerApi.class);

    public static CustomerApi getCustomerApi() {
        return customerApi;
    }

}
