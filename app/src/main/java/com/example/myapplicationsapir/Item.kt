package com.example.myapplicationsapir

data class Item(val title:String, val description:String, val image: String?)

object ItemManager {
    val items : MutableList<Item> = mutableListOf()

    fun addItem(item: Item) {
        items.add(item)
    }

    fun remove(index: Int) {
        items.removeAt(index)
    }

}