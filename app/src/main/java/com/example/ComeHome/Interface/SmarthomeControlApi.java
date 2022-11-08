package com.example.ComeHome.Interface;

import com.example.ComeHome.DTO.Users;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SmarthomeControlApi {
    @POST("api/users/setControlDevices")
    Call<Boolean> getControlResult(@Body Map<String, String> controlDevices);
}
