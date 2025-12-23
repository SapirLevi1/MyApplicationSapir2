package com.example.myapplicationsapir

import android.net.Uri

data class Item(val title:String, val description:String, val imageUri: Uri?) //image: String?

object ItemManager {
    val items : MutableList<Item> = mutableListOf()

    fun add(item: Item) {
        items.add(item)
    }

    fun remove(index: Int) {
        items.removeAt(index)
    }

}