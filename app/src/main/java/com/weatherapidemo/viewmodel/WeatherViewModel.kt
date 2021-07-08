package com.weatherapidemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapidemo.model.ResponseObject
import com.weatherapidemo.network.ApiService
import com.weatherapidemo.network.RetrofitInstance
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val APP_ID = "e96378ae33ebef3bc16bac41512f812a"

class WeatherViewModel : ViewModel() {
    private val weatherData: MutableLiveData<ResponseObject> = MutableLiveData()

    fun getWeatherDataObserver(): LiveData<ResponseObject> {
        return weatherData
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        weatherData.postValue(null)
    }

    fun makeAPICall(query: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val retrofitInstance =
                RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
            val response = retrofitInstance.getCityData(query, APP_ID)
            var data = response.body()!!
            weatherData.postValue(data)
            /*try {
                val response = retrofitInstance.getCityData(query, APP_ID)
                if (response.isSuccessful && response.body() != null) {
                    var data = response.body()!!
                    weatherData.postValue(data)
                }else{
                    weatherData.postValue(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }*/
        }
    }
}