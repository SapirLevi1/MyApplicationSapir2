package com.example.myapplicationsapir

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationsapir.databinding.ItemLayoutBinding

class ItemAdapter(val items: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val binding: ItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.itemTitle.text = item.title
            binding.itemDescription.text = item.description
            //TODO:Load the image
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() =
        items.size
}