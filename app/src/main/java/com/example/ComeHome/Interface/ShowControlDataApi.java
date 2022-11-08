package com.example.ComeHome.Interface;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ShowControlDataApi {
    @GET("/api/control/getDatas")
    Call<Map<String,String>> getPosts();
}
