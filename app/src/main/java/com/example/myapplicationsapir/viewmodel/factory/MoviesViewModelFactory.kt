package com.example.myapplicationsapir.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplicationsapir.repository.MovieRepository
import com.example.myapplicationsapir.viewmodel.movies.MoviesViewModel

class MoviesViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoviesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}