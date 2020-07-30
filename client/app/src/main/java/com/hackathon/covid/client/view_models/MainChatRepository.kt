package com.hackathon.covid.client.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hackathon.covid.client.data_model.ChatListDataModel
import com.hackathon.covid.client.http_utils.ChatbotInterfaces
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainChatRepository(private val mainChatDao : MainChatDaoInterface) {

    private val TAG = javaClass.simpleName
    private var chatList : MutableList<ChatListDataModel> = mutableListOf()
    private val mutableChatList = MutableLiveData<List<ChatListDataModel>>()


    private val chatBotInterface by lazy { ChatbotInterfaces.create()  }
    fun requestQuery(query : String) {
        chatBotInterface.queryChatbot(query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = {
                    Log.d(TAG, "[onNext] >> requestQuery result : ${it.res_code}")
                    // todo : insert this to db

                    addList(ChatListDataModel(
                        it.res_code,
                        true,
                        it.message.orEmpty(),
                        it.botResponse.orEmpty()
                    ))
                },
                onComplete = {
                    Log.d(TAG, "[onComplete] >> requestQuery")
                },
                onError = {
                    Log.e(TAG, "[onError] >> requestQuery error = ${it.stackTrace}")
                }
            )
    }

    fun addList(item : ChatListDataModel) {
        chatList.add(item)
        mutableChatList.postValue(chatList)
    }


    fun getChatLog() : LiveData<List<ChatListDataModel>>{
//        return mainChatDao.getAllChat()
       return mutableChatList
    }

    fun insertLog(query : String) {

    }
}
