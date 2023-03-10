package com.weatherapidemo.data.network

import com.weatherapidemo.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class ApiServiceImpl @Inject constructor(private val apiService: ApiService) {

    suspend fun getCityData(query: String, appID: String): Response<WeatherResponse> =
        apiService.getCityData(query, appID)

}