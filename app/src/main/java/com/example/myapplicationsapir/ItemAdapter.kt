package com.example.myapplicationsapir

import android.app.Dialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationsapir.databinding.ItemLayoutBinding

class ItemAdapter(val items: List<Item>, val callBack: ItemListener)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    interface ItemListener {
        fun onItemClicked(index: Int)
        fun onItemLongClicked(index: Int)
    }
    inner class ItemViewHolder(private val binding: ItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

            init {
                binding.root.setOnClickListener(this)
                binding.root.setOnLongClickListener(this)
            }

        override fun onClick(v: View?) {
            callBack.onItemClicked(adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            callBack.onItemLongClicked(adapterPosition)
            return false
        }

        fun bind(item: Item) {
            binding.itemTitle.text = item.title
            binding.itemDescription.text = item.description

            //TODO:Load the image

            if (item.imageUri != null) {
                // If it does, load the image from the URI
                binding.itemImage.setImageURI(item.imageUri)
            } else {
                // Otherwise, show the default placeholder image
                binding.itemImage.setImageResource(R.mipmap.ic_launcher)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() =
        items.size
}