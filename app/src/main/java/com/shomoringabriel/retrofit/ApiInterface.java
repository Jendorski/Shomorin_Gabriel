package com.shomoringabriel.retrofit;

import com.shomoringabriel.models.filterModel.userModel.FilterUserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("accounts")
    Call<List<FilterUserModel>> getAccounts();

}
