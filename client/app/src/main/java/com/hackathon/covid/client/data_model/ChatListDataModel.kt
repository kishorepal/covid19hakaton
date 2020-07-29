package com.hackathon.covid.client.data_model

data class ChatListDataModel(
    val isBot : Boolean,
    val message : String,
    val botResponse: BotResponse,
    val options : Option
)