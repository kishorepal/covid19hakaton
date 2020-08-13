package com.hackathon.covid.client.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.covid.client.data_model.CheckListModel
import com.hackathon.covid.client.databinding.ItemCheckListBinding

class CheckListRecyclerViewAdapter : RecyclerView.Adapter<CheckListRecyclerViewAdapter.CheckPointViewHolder>() {

    private var checkPoint: List<CheckListModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckPointViewHolder {
        val binding = ItemCheckListBinding.inflate(LayoutInflater.from(parent.context))
        return CheckPointViewHolder(binding)
    }

    override fun getItemCount() = checkPoint.size

    fun updateList(list: List<CheckListModel>) {
        checkPoint = list
        notifyDataSetChanged()
    }

    inner class CheckPointViewHolder(private val binding: ItemCheckListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CheckListModel) {
            val checkPointName = item.checkPointName
            val checkPointLatLng = item.checkPointLatLng
            binding.itemText.text = "$checkPointName -> $checkPointLatLng"
        }
    }

    override fun onBindViewHolder(holder: CheckPointViewHolder, position: Int) = holder.bind(checkPoint[position])

}

