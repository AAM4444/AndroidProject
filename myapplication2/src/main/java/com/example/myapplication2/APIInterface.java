package com.example.myapplication2;

//import
import com.example.myapplication2.pojo.UserList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("/api/users?")
    Call<UserList> doGetUserList(@Query("page") String page);
}
