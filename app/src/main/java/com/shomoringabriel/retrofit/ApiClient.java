package com.shomoringabriel.retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static int REQUEST_TIMEOUT = 30;

    private static final String BASE_URL = "https://android-json-test-api.herokuapp.com/";

    private static Retrofit retrofit = null;

    private static ApiClient mInstance;

    private static OkHttpClient okHttpClient;

    public static Retrofit getClient() {
        if(okHttpClient == null){
            initOkHttp();
        }
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

    public static synchronized ApiClient getInstance(){
        if (mInstance == null){
            mInstance = new ApiClient();
        }
        return mInstance;
    }

    public ApiClient getApi(){
        return retrofit.create(ApiClient.class);
    }

    private static void initOkHttp(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/x-www-form-urlencoded")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded");
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        okHttpClient = httpClient.build();
    }

    public static void resetApiClient(){
        retrofit = null;
        okHttpClient = null;
    }

}

