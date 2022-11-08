package com.example.ComeHome.Interface;

import com.example.ComeHome.DTO.ControlDataInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PostApi {
    @POST("api/home/Control")
    Call<ResponseBody> getControlResult(@Body ControlDataInfo controlData);
}
