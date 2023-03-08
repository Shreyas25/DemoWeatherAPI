package com.weatherapidemo.network

import com.weatherapidemo.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override fun getCityData(query: String, appID: String): Flow<Response<WeatherResponse>> =
        flow { emit(apiService.getCityData(query, appID)) }

}