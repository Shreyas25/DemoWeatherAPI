package com.weatherapidemo.data.model

data class WeatherResponse(
    val city: City = City(),
    val cnt: Int = 0,
    val cod: String = "",
    var message: Any = ""
)