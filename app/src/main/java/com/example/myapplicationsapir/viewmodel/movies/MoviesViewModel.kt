package com.example.myapplicationsapir.viewmodel.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationsapir.data.local.entity.MovieEntity
import com.example.myapplicationsapir.repository.MovieRepository
import com.example.myapplicationsapir.viewmodel.model.SaveMovieResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    val movies: LiveData<List<MovieEntity>> = movieRepository.getAllMovies()
    val favoriteMovies: LiveData<List<MovieEntity>> = movieRepository.getFavoriteMovies()

    fun getMovieById(id: Int): LiveData<MovieEntity> =
        movieRepository.getMovieById(id)

    fun deleteMovie(movie: MovieEntity) {
        viewModelScope.launch {
            movieRepository.deleteMovie(movie)
        }
    }

    fun toggleMovieLike(movieId: Int, isLiked: Boolean) {
        viewModelScope.launch {
            movieRepository.updateMovieLikeStatus(movieId, isLiked)
        }
    }

    fun addMovieWithValidation(
        movie: MovieEntity,
        onResult: (SaveMovieResult) -> Unit
    ) {
        viewModelScope.launch {
            if (movieRepository.movieWithTitleExists(movie.title)) {
                onResult(SaveMovieResult.Error("A movie with this title already exists."))
                return@launch
            }

            validateCommon(movie)?.let { error ->
                onResult(SaveMovieResult.Error(error))
                return@launch
            }

            movieRepository.addMovie(movie)
            onResult(SaveMovieResult.Success)
        }
    }

    fun updateMovieWithValidation(
        movie: MovieEntity,
        onResult: (SaveMovieResult) -> Unit
    ) {
        viewModelScope.launch {
            if (movieRepository.movieWithTitleExistsExcludingId(movie.title, movie.id)) {
                onResult(SaveMovieResult.Error("A movie with this title already exists."))
                return@launch
            }

            validateCommon(movie)?.let { error ->
                onResult(SaveMovieResult.Error(error))
                return@launch
            }

            movieRepository.updateMovie(movie)
            onResult(SaveMovieResult.Success)
        }
    }

    /**
     * Returns an error message if invalid, otherwise null.
     * Handles validations that are shared between Add + Edit.
     */
    private fun validateCommon(movie: MovieEntity): String? {
        // Title required (recommended)
        if (movie.title.isBlank()) {
            return "Title is required."
        }

        // Watched date can't be in the future (only if provided)
        movie.watchedDate?.let { watched ->
            if (watched > System.currentTimeMillis()) {
                return "Watched date can't be in the future."
            }
        }

        // Score must be 1..10 (only if provided)
        movie.score?.let { score ->
            if (score !in 1..10) {
                return "Score must be between 1 and 10."
            }
        }

        return null
    }
}
