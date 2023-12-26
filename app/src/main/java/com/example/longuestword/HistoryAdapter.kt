package com.example.longuestword

import android.graphics.Color
import android.provider.CalendarContract.Colors
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.longuestword.databinding.ResultRowBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(var binding: ResultRowBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<HistoryData>(){
        override fun areItemsTheSame(oldItem: HistoryData, newItem: HistoryData): Boolean {
            return oldItem.player == newItem.player
        }

        override fun areContentsTheSame(oldItem: HistoryData, newItem: HistoryData): Boolean {
            return oldItem.word == newItem.word
        }
    }

    val differ = AsyncListDiffer(this , differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ResultRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = differ.currentList[position]
        holder.binding.apply {
            playerTv.text = "${data.player} :"
            resultTv.text = "${data.word} :"
            resultMsgTv.text = data.message
            if (data.player == "First Player"){
                playerTv.setTextColor(Color.BLUE)
            }else{
                playerTv.setTextColor(Color.RED)
            }
        }


    }

    override fun getItemCount(): Int = differ.currentList.size
}