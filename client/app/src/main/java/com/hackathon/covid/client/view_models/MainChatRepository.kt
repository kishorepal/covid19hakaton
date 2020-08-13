package com.hackathon.covid.client.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hackathon.covid.client.data_model.ChatListDataModel
import com.hackathon.covid.client.http_utils.ChatbotInterfaces
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainChatRepository(private val mainChatDao : MainChatDaoInterface) {

    private val TAG = javaClass.simpleName

    private val chatBotInterface by lazy { ChatbotInterfaces.create()  }
    fun requestQuery(query : String) {
        chatBotInterface.queryChatbot(query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = {
                    Log.d(TAG, "[onNext] >> requestQuery result : ${it.res_code}")
                    // todo : insert this to db

                    insertItem(ChatListDataModel(
                        isBot = true,
                        message = it.message.toString(),
                        botResponse = it.botResponse.toString()
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


    fun getChatLog() : LiveData<List<ChatListDataModel>>{
        return mainChatDao.getAllChat()

    }

    fun insertItem(item : ChatListDataModel) {
        mainChatDao.insert(item)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {Log.d(TAG, "[insert] >> onComplete")},
                onError = {Log.e(TAG, "[insert] >> onError")}
            )

    }

}
