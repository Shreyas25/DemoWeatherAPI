package com.weatherapidemo.repository

import com.weatherapidemo.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val apiService: ApiService) : BaseRepo() {

    suspend fun getWeatherData(query: String, appID: String) = safeApiCall {
        apiService.getCityData(query, appID)
    }

    /*suspend fun getCityData(query: String, appID: String) = apiCallRequest {
        Log.d("Weather API Demo","Info: Repository")
        apiService.getCityData(query, appID)
    }*/

}