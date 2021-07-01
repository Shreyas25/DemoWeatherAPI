package com.weatherapidemo.network

import com.weatherapidemo.model.ResponseObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("forecast")
    suspend fun getCityData(
        @Query("q") query: String,
        @Query("appid") appID: String,
    ): Response<ResponseObject>


}

