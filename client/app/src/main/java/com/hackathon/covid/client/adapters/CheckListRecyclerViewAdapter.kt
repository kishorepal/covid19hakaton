package com.hackathon.covid.client.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.covid.client.data_model.CheckListModel
import com.hackathon.covid.client.databinding.ItemCheckListBinding

class CheckListRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var checkPoint: List<CheckListModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCheckListBinding.inflate(LayoutInflater.from(parent.context))
        return CheckPointViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // todo : can not use -> holder.bind(checkPoint[position])
//        holder.bind(checkPoint[position])
//        CheckListViewHolder(ItemCheckListBinding()).bind(checkPoint[position])
    }

    override fun getItemCount() = checkPoint.size

    fun updateList(list: List<CheckListModel>) {
        checkPoint = list
        notifyDataSetChanged()
    }

    inner class CheckPointViewHolder(private val binding: ItemCheckListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CheckListModel) {
            binding.itemText.text = item.checkPointInfo
        }
    }

}

