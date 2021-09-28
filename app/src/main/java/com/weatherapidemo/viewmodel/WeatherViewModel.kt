package com.weatherapidemo.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.weatherapidemo.model.ResponseObject
import com.weatherapidemo.others.Resource
import com.weatherapidemo.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

const val APP_ID = "e96378ae33ebef3bc16bac41512f812a"

@HiltViewModel
class WeatherViewModel @Inject constructor(private val appRepository: WeatherRepository) :
    ViewModel() {

    val edtQueryData: MutableLiveData<String> = MutableLiveData()
    private var weatherData: MutableLiveData<Resource<ResponseObject>> = MutableLiveData()

    fun getWeatherDataObserver(): LiveData<Resource<ResponseObject>> {
        return weatherData
    }

    private val _sunriseData = MutableLiveData<String>()
    val sunriseData: LiveData<String> = _sunriseData

    private val _sunsetData = MutableLiveData<String>()
    val sunsetData: LiveData<String> = _sunsetData

    private var observer: Observer<Resource<ResponseObject>>? = null

    //Need to handle exception later.
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
        viewModelScope.launch {
            weatherData = appRepository.getCityData(
                query,
                APP_ID
            ) as MutableLiveData<Resource<ResponseObject>>

            observer = Observer { result ->
                if (result.data != null) {
                    getSunriseData(result.data)
                    getSunsetData(result.data)
                }
            }
        }
        weatherData.observeForever(observer!!)

    }

    private fun getSunriseData(data: ResponseObject?) {
        val sunriseValue = data?.city?.sunrise
        val timeZoneValue = data?.city?.timezone
        var sunriseStr = convertValue(sunriseValue, "hh:mm a", timeZoneValue)
        _sunriseData.value = sunriseStr
    }

    private fun getSunsetData(data: ResponseObject?) {
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
        weatherData.removeObserver(observer!!)
    }
}