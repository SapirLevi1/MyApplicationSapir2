package com.example.myapplicationsapir.data.model
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "movies",
    indices = [Index(value = ["title"], unique = true)]
)
data class MovieEntity(
    @ColumnInfo(name = "title") //title
    val title:String,
    @ColumnInfo(name = "movie_desc") //description
    val description:String,
    @ColumnInfo(name = "watched_date")
    val watchedDate: Long?, // timestamp
    @ColumnInfo(name = "score")
    val score: Int?,
    @ColumnInfo(name = "image") //imageUri
    val imageUri: String?) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}