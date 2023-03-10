package com.weatherapidemo.data.model

data class City(
    var country: String = "",
    val id: Int = 0,
    var name: String = "",
    val population: Int = 0,
    var sunrise: Long = 0,
    var sunset: Long = 0,
    val timezone: Int = 0,
    var _sunrise: String = "",
    var _sunset: String = "",
)