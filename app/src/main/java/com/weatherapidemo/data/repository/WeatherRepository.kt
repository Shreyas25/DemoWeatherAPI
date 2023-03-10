package com.weatherapidemo.data.repository

import com.weatherapidemo.data.model.WeatherResponse
import com.weatherapidemo.data.network.ApiServiceImpl
import com.weatherapidemo.others.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val apiServiceImpl: ApiServiceImpl) :
    BaseRepo() {

    suspend fun getWeatherData(query: String, appID: String): Flow<Result<WeatherResponse>> =
        safeApiCall {
            apiServiceImpl.getCityData(query, appID)
        }

    /*suspend fun getCityData(query: String, appID: String) = apiCallRequest {
        Log.d("Weather API Demo","Info: Repository")
        apiService.getCityData(query, appID)
    }*/

}