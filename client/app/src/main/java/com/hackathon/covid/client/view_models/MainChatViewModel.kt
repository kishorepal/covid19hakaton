package com.hackathon.covid.client.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hackathon.covid.client.data_model.ChatListDataModel


class MainChatViewModel(context: Context) : ViewModel(){

    private var repository : MainRepository
    var chatList : LiveData<List<ChatListDataModel>>


    init {
        val mainChatDao = MainChatDatabase.getDatabase(context).mainChatDao()

        repository = MainRepository(mainChatDao)
        chatList = repository.getChatLog()
    }


    fun sendMessage(message : String) {
        repository.requestQuery(message)
        repository.insertItem(
            ChatListDataModel(
            isBot = false,
                message = "",
                botResponse = message
        ))

    }


}