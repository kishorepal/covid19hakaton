package com.hackathon.covid.client.common

object UnitConversion {

    private fun celsiusToFahrenheit(celsius : Long) = celsius * 9 / 5 + 32

    fun getTempWithUnit(isCelsius : Boolean, temperature : Long): String {
        val convertedTemp = if (isCelsius) temperature else celsiusToFahrenheit(temperature)

        return if (isCelsius) "$temperature'C" else "$temperature'F"
    }


    fun getHumidityWithUnit(humidity : Long): String {
        return "$humidity%"
    }


}