package com.weatherapidemo.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.weatherapidemo.data.model.WeatherResponse
import com.weatherapidemo.others.Resource
import com.weatherapidemo.data.repository.WeatherRepository
import com.weatherapidemo.others.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

const val APP_ID = "e96378ae33ebef3bc16bac41512f812a"

@HiltViewModel
class WeatherViewModel @Inject constructor(private val appRepository: WeatherRepository) :
    ViewModel() {

    val edtQueryData: MutableLiveData<String> = MutableLiveData()

    private val _sunriseData = MutableLiveData<String>()
    val sunriseData: LiveData<String> = _sunriseData

    private val _sunsetData = MutableLiveData<String>()
    val sunsetData: LiveData<String> = _sunsetData

    //STATE FLOW
    private val _cityData: MutableStateFlow<Result<WeatherResponse>> =
        MutableStateFlow(Result.Loading)
    val cityData: StateFlow<Result<WeatherResponse>> = _cityData

    init {
        getData("Pune")
    }

    private fun getData(city: String) {
        viewModelScope.launch {
            _cityData.value = Result.Loading
            appRepository.getWeatherData(city, APP_ID)
                .catch { e ->
                    _cityData.value = Result.Failure(e)
                }.collect { data ->
                    _cityData.value = data
                    Log.d("RESPONSE", "" + data)
                }
        }
    }

    fun onClickSearch() {
        /*if ((edtQueryData.value != null) && (edtQueryData.value.toString().isNotEmpty())) {
            makeAPICall(edtQueryData.value.toString())
        } else {
            makeAPICall("Pune")
        }*/
    }

    fun getSunriseData(data: WeatherResponse?) {
        val sunriseValue = data?.city?.sunrise
        val timeZoneValue = data?.city?.timezone
        var sunriseStr = convertValue(sunriseValue, "hh:mm a", timeZoneValue)
        _sunriseData.value = sunriseStr
    }

    fun getSunsetData(data: WeatherResponse?) {
        val sunsetValue = data?.city?.sunset
        val timeZoneValue = data?.city?.timezone
        var sunsetStr = convertValue(sunsetValue, "hh:mm a", timeZoneValue)
        _sunsetData.value = sunsetStr
    }

    private fun convertValue(mTimestamp: Long?, mDateFormat: String, timeZoneValue: Int?): String {
        var date = ""
        try {
            val calTimeStamp = mTimestamp!! + timeZoneValue!!
            val sdf = SimpleDateFormat(mDateFormat)
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            date = sdf.format(Date(calTimeStamp * 1000))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return date
    }

    fun tearDown() {
//        weatherData.removeObserver(observer!!)
    }
}