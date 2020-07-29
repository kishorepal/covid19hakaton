package com.hackathon.covid.client.view_models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hackathon.covid.client.data_model.ChatListDataModel

@Database(entities = [ChatListDataModel::class], version = 1)
abstract class MainChatDatabase : RoomDatabase(){

    abstract fun mainChatDao() : MainChatDaoInterface

    companion object {
        @Volatile
        private var instance : MainChatDatabase? = null

        fun getDatabase(context : Context) : MainChatDatabase {
            val tempInstance = instance
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val mInstance = Room.databaseBuilder(
                    context.applicationContext,
                    MainChatDatabase::class.java,
                    "chat_log_table"
                ).build()

                instance = mInstance
                return mInstance
            }
        }
    }
}