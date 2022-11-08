package com.example.ComeHome.Interface;

import com.example.ComeHome.DTO.Users;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserJoinApi {
    @POST("api/users/signUp")
    Call<ResponseBody> getUserResult(@Body Users users);
}