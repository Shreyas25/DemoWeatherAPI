package com.weatherapidemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapidemo.model.ResponseObject
import com.weatherapidemo.network.ApiService
import com.weatherapidemo.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val APP_ID = "e96378ae33ebef3bc16bac41512f812a"
class WeatherViewModel : ViewModel() {
    var weatherData: MutableLiveData<ResponseObject> = MutableLiveData()

    fun getWeatherDataObserver(): MutableLiveData<ResponseObject> {
        return weatherData
    }

    fun makeAPICall(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val retrofitInstance =
                RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
            try {
                val response = retrofitInstance.getCityData(query, APP_ID)
                if (response.isSuccessful && response.body() != null) {
                    var data = response.body()!!
                    weatherData.postValue(data)
                }else{
                    weatherData.postValue(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}