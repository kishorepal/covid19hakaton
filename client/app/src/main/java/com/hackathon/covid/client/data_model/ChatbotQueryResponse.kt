package com.hackathon.covid.client.data_model

import com.google.gson.annotations.SerializedName

data class ChatbotQueryResponse (

    @SerializedName("res_code")
    val res_code : Int,

    @SerializedName("message")
    val message : String,

    @SerializedName("bot_response")
    val bot_response : List<BotResponse>,

    @SerializedName("options")
    val options : List<Option>
)




data class BotResponse(
    @SerializedName("text")
    val text : String,

    @SerializedName("confidence")
    val confidence : Float
)

data class Option(
    @SerializedName ("option")
    val option : String
)

