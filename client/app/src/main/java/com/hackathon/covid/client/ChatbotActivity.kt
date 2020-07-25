package com.hackathon.covid.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ChatbotActivity : AppCompatActivity() {

    private val chattingFragment : ChattingFragment by lazy {
        ChattingFragment().newInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)
    }
}