package com.example.ComeHome.Interface;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CheckIDApi {
    @POST("/api/users/idCheck")
    Call<Boolean> getResult(@Body String userId);
}
