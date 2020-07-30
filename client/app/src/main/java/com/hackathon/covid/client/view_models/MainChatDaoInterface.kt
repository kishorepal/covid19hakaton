package com.hackathon.covid.client.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackathon.covid.client.data_model.ChatListDataModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface MainChatDaoInterface {

    @Query("SELECT * FROM chat_log_table")
    fun getAllChat() : LiveData<List<ChatListDataModel>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item : ChatListDataModel) : Completable
}