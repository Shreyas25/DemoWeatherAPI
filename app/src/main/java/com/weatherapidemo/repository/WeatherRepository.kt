package com.weatherapidemo.repository

import android.util.Log
import com.weatherapidemo.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val apiService: ApiService) :BaseRepository() {

    suspend fun getCityData(query: String, appID: String) = apiCallRequest {
        Log.d("Weather API Demo","Info: Repository")
        apiService.getCityData(query, appID)
    }

}