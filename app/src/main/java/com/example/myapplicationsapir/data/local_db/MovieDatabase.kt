package com.example.myapplicationsapir.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplicationsapir.data.model.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 3,
    exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao() : MovieDao
     companion object{

         @Volatile
         private var INSTANCE: MovieDatabase? = null

         fun getDatabase(context: Context): MovieDatabase {
             return INSTANCE ?: synchronized(this) {
                 val instance = Room.databaseBuilder(
                                  context.applicationContext,
                                  MovieDatabase::class.java,
                                  "movie_database"
                              )
                     .fallbackToDestructiveMigration(true)
                     .build()
                 INSTANCE = instance
                 instance
             }
         }

     }
}