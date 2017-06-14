package com.expreso.androidapp.androidapp;

import com.expreso.androidapp.androidapp.Models.TokenRequest;
import com.expreso.androidapp.androidapp.Models.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 */

public interface IStockService {

    @POST("api-token-auth/")
    public Call<TokenResponse> getLoginDetails(@Body TokenRequest tokenRequest);
}
