package com.weatherapidemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.weatherapidemo.databinding.ActivityMainBinding
import com.weatherapidemo.model.ResponseObject
import com.weatherapidemo.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.weatherViewModel = viewModel

        binding.lifecycleOwner = this

        observeWeatherData()
    }

    private fun observeWeatherData() {
        viewModel.getWeatherDataObserver().observe(this, Observer { response ->
            if (response != null) {
//                updateUI(response)
            } else {
                Toast.makeText(this, "City not found", Toast.LENGTH_SHORT).show()
            }
        })

//        viewModel.makeAPICall("pune")
    }

    /*private fun updateUI(response: ResponseObject) {
        val sunriseValue = response.city.sunrise
        val sunsetValue = response.city.sunset
        val timeZoneValue = response.city.timezone
        var sunriseStr = convertValue(sunriseValue, "hh:mm a", timeZoneValue)
        var sunsetStr = convertValue(sunsetValue, "hh:mm a", timeZoneValue)

        with(response.city) {
            with(binding) {
                cityName.text = name
                countryName.text = country
                sunrise.text = sunriseStr
                sunset.text = sunsetStr
            }

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
    }*/
}