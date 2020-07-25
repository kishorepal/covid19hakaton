package com.hackathon.covid.client.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.covid.client.data_model.ChatbotQueryResponse
import com.hackathon.covid.client.databinding.ChattingFragmentBinding
import com.hackathon.covid.client.databinding.ItemChatIncomingBubbleBinding

class ChatRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    private var chatList = mutableListOf<ChatbotQueryResponse>()

    private val VIEW_TYPE_LEFT_TEXT = 0
    private val VIEW_TYPE_LEFT_BUTTON = 1
    private val VIEW_TYPE_RIGHT_TEXT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // TODO : condition to be changed view type
        when(chatList[position].res_code) {
            VIEW_TYPE_LEFT_TEXT -> {

                return LeftViewHolder()
            }
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    inner class LeftViewHolder(private val binding : ItemChatIncomingBubbleBinding) : RecyclerView.ViewHolder(binding.root) {

    }


}
