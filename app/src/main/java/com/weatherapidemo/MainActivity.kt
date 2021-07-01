package com.weatherapidemo

import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.weatherapidemo.databinding.ActivityMainBinding
import com.weatherapidemo.model.ResponseObject
import com.weatherapidemo.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        observeWeatherData()
        setClickListener()
    }

    private fun setClickListener() {
        binding.btnSearch.setOnClickListener {
            val queryStr = binding.edtQueryCity.text.toString()
            if (queryStr.isEmpty()) {
                viewModel.makeAPICall("pune")
            } else {
                viewModel.makeAPICall(query = queryStr)
            }
        }
    }

    private fun observeWeatherData() {
        viewModel.getWeatherDataObserver().observe(this, Observer { response ->
            if (response != null) {
                updateUI(response)
            } else {
                Toast.makeText(this, "City not found", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.makeAPICall("pune")
    }

    private fun updateUI(response: ResponseObject) {
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
    }
}