package com.hackathon.covid.client.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hackathon.covid.client.data_model.ChatListDataModel


class MainChatViewModel(context: Context) : ViewModel(){

    private var repository : MainChatRepository
    var chatList : LiveData<List<ChatListDataModel>>


    init {
        val mainChatDao = MainChatDatabase.getDatabase(context).mainChatDao()

        repository = MainChatRepository(mainChatDao)
        chatList = repository.getChatLog()
    }


    fun sendMessage(message : String) {
        repository.addList(ChatListDataModel(1, false, "", message))
        repository.requestQuery(message)
        // todo : repository needs to store user's reponse in the log as well
    }


}