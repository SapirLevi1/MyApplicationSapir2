package com.example.myapplicationsapir.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplicationsapir.repository.MovieAPIRepository
import com.example.myapplicationsapir.viewmodel.model.MovieDetailsAPIViewModel

class MovieDetailsAPIViewModelFactory(
    private val apiKey: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailsAPIViewModel(
            MovieAPIRepository(apiKey)
        ) as T
    }
}
