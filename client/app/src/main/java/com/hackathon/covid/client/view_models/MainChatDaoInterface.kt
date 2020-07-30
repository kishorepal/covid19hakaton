package com.hackathon.covid.client.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackathon.covid.client.data_model.ChatListDataModel

@Dao
interface MainChatDaoInterface {

    @Query("SELECT * FROM chat_log_table")
    fun getAllChat() : LiveData<List<ChatListDataModel>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item : ChatListDataModel)
}