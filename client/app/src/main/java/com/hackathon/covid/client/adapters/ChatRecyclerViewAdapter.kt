package com.hackathon.covid.client.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.covid.client.data_model.ChatListDataModel
import com.hackathon.covid.client.data_model.ChatbotQueryResponse
import com.hackathon.covid.client.databinding.ChattingFragmentBinding
import com.hackathon.covid.client.databinding.ItemChatIncomingBubbleBinding
import com.hackathon.covid.client.databinding.ItemChatOutgoingBubbleBinding

class ChatRecyclerViewAdapter(context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    private var chatList : List<ChatListDataModel> = mutableListOf()

    private val VIEW_TYPE_LEFT_TEXT = 0
    private val VIEW_TYPE_LEFT_BUTTON = 1
    private val VIEW_TYPE_RIGHT_TEXT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(viewType) {
            VIEW_TYPE_LEFT_TEXT -> {
                val binding = ItemChatIncomingBubbleBinding.inflate(LayoutInflater.from(parent.context))
                /**
                 * to be added for the click listner
                 * binding.listener = EventHandlers(activity)
                 */
                return LeftViewHolder(binding)
            }
            VIEW_TYPE_RIGHT_TEXT -> {
                val binding = ItemChatOutgoingBubbleBinding.inflate(LayoutInflater.from(parent.context))
                return RightViewHolder(binding)
            }
            else -> {
                val binding = ItemChatIncomingBubbleBinding.inflate(LayoutInflater.from(parent.context))
                /**
                 * to be added for the click listner
                 * binding.listener = EventHandlers(activity)
                 */
                return LeftViewHolder(binding)
            }
        }
    }

    override fun getItemCount() = chatList.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LeftViewHolder -> {
                holder.bind(chatList[position])
            }
            is RightViewHolder -> {
                holder.bind(chatList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].isBot)
            VIEW_TYPE_LEFT_TEXT
        else VIEW_TYPE_RIGHT_TEXT
    }

    fun updateList(list : List<ChatListDataModel>) {
        chatList = list
        notifyDataSetChanged()
    }

    inner class LeftViewHolder(private val binding : ItemChatIncomingBubbleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ChatListDataModel) {
            binding.tvText.text = item.botResponse
            binding.rvOptions.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    inner class RightViewHolder(private val binding : ItemChatOutgoingBubbleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatListDataModel) {
            binding.tvText.text = item.botResponse
        }
    }



}
