package com.expreso.androidapp.androidapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 */

public class Users {
    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("is_active")
    @Expose
    private boolean is_active;

    @SerializedName("last_name")
    @Expose

    private String last_name;

    @SerializedName("id")
    @Expose

    private int id;

    @SerializedName("email")
    @Expose
    private String email;

    public String getFirst_name() {
        return first_name;
    }

    public boolean is_active() {
        return is_active;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
