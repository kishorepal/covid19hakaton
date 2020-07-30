package com.hackathon.covid.client.data_model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_log_table")
data class ChatListDataModel(

    /**
     * Todo : this is modified for the current api response
     * It needs to be implemented as the document
     */

    @ColumnInfo(name = "isBot") val isBot : Boolean,
    @ColumnInfo(name = "message") val message : String,
    @ColumnInfo(name = "botResponse") val botResponse : String
    //val botResponse: BotResponse,
    //val options : Option
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="id") var id : Int = 0
}