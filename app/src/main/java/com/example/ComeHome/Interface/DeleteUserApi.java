package com.example.ComeHome.Interface;

import com.example.ComeHome.DTO.Users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DeleteUserApi {
    @POST("api/users/WithdrawUser")
    Call<Boolean> getDeleteResult(@Body Users users);
}
