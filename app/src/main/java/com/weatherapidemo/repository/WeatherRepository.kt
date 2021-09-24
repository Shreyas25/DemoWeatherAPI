package com.weatherapidemo.repository

import com.weatherapidemo.network.ApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiService: ApiService) :BaseRepository() {

    suspend fun getCityData(query: String, appID: String) = apiCallRequest {
        apiService.getCityData(query, appID)
    }

}