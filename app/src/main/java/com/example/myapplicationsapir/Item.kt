package com.example.myapplicationsapir
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import android.net.Uri
import java.io.Serializable
@Parcelize
data class Item(val title:String, val description:String, val imageUri: String?) : Parcelable //Uri?


object ItemManager {
    val items : MutableList<Item> = mutableListOf()

    fun add(item: Item) {
        items.add(item)
    }

    fun remove(index: Int) {
        items.removeAt(index)
    }

}