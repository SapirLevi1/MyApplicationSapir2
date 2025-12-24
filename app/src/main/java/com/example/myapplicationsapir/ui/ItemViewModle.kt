package com.example.myapplicationsapir.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.myapplicationsapir.data.model.Item
import com.example.myapplicationsapir.data.repository.ItemRepository

class ItemViewModle(application: Application) : AndroidViewModel(application) {

    private val repository = ItemRepository(application)

    val items : LiveData<List<Item>>? = repository.getItems()

    fun addItem(item: Item) {
        repository.addItem(item)
    }

    fun deleteItem(item: Item) {
        repository.deletItem(item)

    }
}