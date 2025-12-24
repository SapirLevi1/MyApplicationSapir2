package com.example.myapplicationsapir.data.repository

import android.app.Application
import com.example.myapplicationsapir.data.local_db.ItemDao
import com.example.myapplicationsapir.data.local_db.ItemDataBase
import com.example.myapplicationsapir.data.model.Item

class ItemRepository(application: Application) {

    private val itemDao: ItemDao?

    init {
        val db = ItemDataBase.getDatabase(application.applicationContext)
        itemDao = db?.itemsDao()
    }

    fun getItems() = itemDao?.getItems()

    fun addItem(item: Item) {
        itemDao?.addItem(item)
    }

    fun deletItem(item: Item) {
        itemDao?.deleteItem(item)
    }

    fun getItem(id: Int) = itemDao?.getItem(id)

}