package com.hackathon.covid.client.common

import androidx.room.TypeConverter
import com.hackathon.covid.client.data_model.BotResponse
import com.hackathon.covid.client.data_model.Option

class DataTypeConverter {
    @TypeConverter
    fun botResponseToStrings(botResponse: BotResponse) : String{
        return botResponse.text + ", ${botResponse.confidence}"
    }

    @TypeConverter
    fun stringToBotResponse(textAndConfidence : String) : BotResponse {
        return BotResponse(textAndConfidence.split(",")[0].trim(), textAndConfidence.split(",")[1].trim().toFloat())
    }

    @TypeConverter
    fun optionsToStrings(options : List<Option>) : String{
        var option : String = ""
        for (item in options) {
            if (option.isNullOrBlank()) {
                option = item.option.toString()
            }
            else {
                option = ",${item.option.toString()}"
            }
        }

        return option
    }

    @TypeConverter
    fun stringsToOptions(optionsText : String) : List<Option> {
        val stringArray = optionsText.split(",")
        var optionsArray : List<Option> = mutableListOf()
        for (item in stringArray) {
            optionsArray.
        }
    }

}