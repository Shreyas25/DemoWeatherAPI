package com.weatherapidemo.network

import com.weatherapidemo.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Query

interface ApiHelper {

    fun getCityData(
        @Query("q") query: String,
        @Query("appid") appID: String,
    ): Flow<Response<WeatherResponse>>
}