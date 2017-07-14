package com.expreso.androidapp.androidapp.Direcciones_GPS;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by dev on 23/06/17.
 *
 */

public interface GetTodos {

    @GET("apis/direcciones_gps/")
    Call<Result> all();


}
