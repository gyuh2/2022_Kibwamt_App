package com.example.ComeHome.Interface;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserData {
    @POST("/api/users/getUsers")
    Call<Map<String,String>> getPosts(@Body String UserId);
}
