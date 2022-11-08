package com.example.ComeHome.Interface;

import com.example.ComeHome.DTO.Users;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CheckUserApi {
    @POST("/api/users/login")
    Call<Boolean> getUserResult(@Body Users users);

}
