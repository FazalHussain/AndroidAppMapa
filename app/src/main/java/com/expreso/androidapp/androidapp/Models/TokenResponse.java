package com.expreso.androidapp.androidapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 */

public class TokenResponse {

    @SerializedName("user")
    @Expose
    private Users user;

    @SerializedName("token")
    @Expose
    private String token;


    public String getToken() {
        return token;
    }

    public Users getUser() {
        return user;
    }
}
