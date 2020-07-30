package com.hackathon.covid.client.http_utils

import com.hackathon.covid.client.common.Const
import com.hackathon.covid.client.data_model.ChatbotQueryResponse
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatbotInterfaces {

    companion object {

        fun create() : ChatbotInterfaces{
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Const.chatbotBaseUrl)
                .build()


            return retrofit.create(ChatbotInterfaces::class.java)

        }
    }


    // declare here for the api endpoints

    @POST("api/bot_response/")
    fun queryChatbot(@Query("query") query : String) : Observable<ChatbotQueryResponse>
}