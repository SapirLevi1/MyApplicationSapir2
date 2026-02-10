package com.example.myapplicationsapir.viewmodel.factory

import android.content.Context
import com.example.myapplicationsapir.data.local.database.MovieDatabase
import com.example.myapplicationsapir.repository.MovieRepository

object MovieViewModelFactoryProvider {

    @Volatile
    private var factory: MoviesViewModelFactory? = null

    fun getFactory(context: Context): MoviesViewModelFactory {
        return factory ?: synchronized(this) {
            factory ?: createFactory(context).also { factory = it }
        }
    }

    private fun createFactory(context: Context): MoviesViewModelFactory {
        val database = MovieDatabase.getDatabase(context.applicationContext)
        val repository = MovieRepository(database.movieDao())
        return MoviesViewModelFactory(repository)
    }
}
