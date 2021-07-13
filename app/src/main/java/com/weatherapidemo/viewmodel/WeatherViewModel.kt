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
import java.text.SimpleDateFormat
import java.util.*

const val APP_ID = "e96378ae33ebef3bc16bac41512f812a"

class WeatherViewModel : ViewModel() {
    val edtQueryData: MutableLiveData<String> = MutableLiveData()
    private val weatherData: MutableLiveData<ResponseObject> = MutableLiveData()

    fun getWeatherDataObserver(): LiveData<ResponseObject> {
        return weatherData
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        weatherData.postValue(null)
    }

    init {
        makeAPICall("Pune")
    }

    fun onClickSearch() {
        if ((edtQueryData.value != null) && (edtQueryData.value.toString().isNotEmpty())) {
            makeAPICall(edtQueryData.value.toString())
        } else {
            makeAPICall("Pune")
        }
    }

    fun makeAPICall(query: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val retrofitInstance =
                RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
            val response = retrofitInstance.getCityData(query, APP_ID)
            var data = response.body()!!
            doChange(data)
//            weatherData.postValue(data)
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

    private fun doChange(responseObj: ResponseObject) {
        if (responseObj != null) {
            val sunriseValue = responseObj.city.sunrise
            val sunsetValue = responseObj.city.sunset
            val timeZoneValue = responseObj.city.timezone
            var sunriseStr = convertValue(sunriseValue, "hh:mm a", timeZoneValue)
            var sunsetStr = convertValue(sunsetValue, "hh:mm a", timeZoneValue)

            var response = ResponseObject()
            response.city._sunrise = sunriseStr
            response.city._sunset = sunsetStr
            response.city.name = responseObj.city.name
            response.city.country = responseObj.city.country

            weatherData.postValue(response)
        } else {
            weatherData.postValue(null)
        }

    }

    private fun convertValue(mTimestamp: Long, mDateFormat: String, timeZoneValue: Int): String {
        var date = ""
        try {
            val calTimeStamp = mTimestamp + timeZoneValue
            val sdf = SimpleDateFormat(mDateFormat)
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            date = sdf.format(Date(calTimeStamp * 1000))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return date
    }
}