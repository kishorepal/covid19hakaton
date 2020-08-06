package com.hackathon.covid.client.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hackathon.covid.client.data_model.ChatListDataModel


class MainChatViewModel(context: Context) : ViewModel(){

    private var chatRepository : MainChatRepository
    var chatList : LiveData<List<ChatListDataModel>>


    init {
        val mainChatDao = MainDatabase.getDatabase(context).mainChatDao()

        chatRepository = MainChatRepository(mainChatDao)
        chatList = chatRepository.getChatLog()
    }


    fun sendMessage(message : String) {
        chatRepository.requestQuery(message)
        chatRepository.insertItem(
            ChatListDataModel(
            isBot = false,
                message = "",
                botResponse = message
        ))

    }


}