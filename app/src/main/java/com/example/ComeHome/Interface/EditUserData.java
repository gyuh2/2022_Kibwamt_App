package com.example.ComeHome.Interface;

import com.example.ComeHome.DTO.ControlDataInfo;
import com.example.ComeHome.DTO.Users;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EditUserData {
    @POST("api/users/setUsers")
    Call<Boolean> getEditResult(@Body Users users);
}
