package com.hackathon.covid.client.view_models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hackathon.covid.client.common.Const
import com.hackathon.covid.client.data_model.ChatListDataModel
import com.hackathon.covid.client.data_model.EnvironmentModel

@Database(entities = [ChatListDataModel::class, EnvironmentModel::class], version = 3)
public abstract class MainChatDatabase : RoomDatabase(){

    abstract fun mainChatDao() : MainChatDaoInterface
    abstract fun mainEnvironmentDao() : MainEnvironmentDaoInterface

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
                    Const.dbName // DB name ref : https://developer.android.com/reference/androidx/room/Room#databaseBuilder(android.content.Context,%20java.lang.Class%3CT%3E,%20java.lang.String)
                ).build()

                instance = mInstance
                return mInstance
            }
        }
    }
}