package com.hackathon.covid.client.view_models

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainEnvironmentViewModel(context : Context) : ViewModel() {

    private val TAG = javaClass.simpleName


    // todo : utilize repository for main environment factors
    //private var repository : MainEnvironmentRepository

    /**
     * todo : needed value for this fragment
     * 1. Room Temp : Int ('C) conversion needed for 'F
     * 2. Humidity : Int %
     * 3. Risk Factor : TBD
     * 4. App Status (Boolean)
     * 5. Infected Contacts : Int
     * 6. Short Description : String
     */

    private var repository : MainEnvironmentRepository =
        MainEnvironmentRepository()

    private lateinit var roomTempLiveData : MutableLiveData<Int>
    private lateinit var humidityLiveData : MutableLiveData<Int>
    private lateinit var riskFactorLiveData : MutableLiveData<String>
    private lateinit var appStatusLiveData : MutableLiveData<Boolean>
    private lateinit var infectedContactsLiveData : MutableLiveData<Int>
    private lateinit var shortDescriptionLiveData : MutableLiveData<String>

    init {

        /// todo : remove this dummy data after demo
        roomTempLiveData.value = 32
        humidityLiveData.value = 26
        riskFactorLiveData.value = "LOW"
        appStatusLiveData.value = true
        infectedContactsLiveData.value = 0
        shortDescriptionLiveData.value = "SHORT DESCRIPTION"
    }


    fun getRoomTemp() {

    }

    fun getRoomHumidity() {

    }

    fun getRiskFactor() {

    }

    fun getAppStatus() {

    }

    fun getInfectedContacts() {

    }

    fun getShortDescription() {

    }


    /**
     * This function is only for the testing function.
     */
    fun postDummyValue(roomTemp : Int, humidity : Int, riskFactor : String, appStatus : Boolean, infectedContacts : Int, shortDescription : String) {
        roomTempLiveData.value = roomTemp
        humidityLiveData.value = humidity
        riskFactorLiveData.value = riskFactor
        appStatusLiveData.value = appStatus
        infectedContactsLiveData.value = infectedContacts
        shortDescriptionLiveData.value = shortDescription

    }


}