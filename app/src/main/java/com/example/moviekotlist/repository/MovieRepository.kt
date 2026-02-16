package com.example.moviekotlist.repository

import androidx.lifecycle.LiveData
import com.example.moviekotlist.data.local.dao.MovieDao
import com.example.moviekotlist.data.local.entity.MovieEntity

class MovieRepository(
    private val movieDao: MovieDao
) {

    suspend fun addMovie(movie: MovieEntity) = movieDao.addMovie(movie)
    suspend fun updateMovie(movie: MovieEntity) = movieDao.updateMovie(movie)
    suspend fun deleteMovie(movie: MovieEntity) = movieDao.deleteMovie(movie)
    fun getAllMovies(): LiveData<List<MovieEntity>> = movieDao.getAllMovies()

    fun getMovieById(id: Int): LiveData<MovieEntity> = movieDao.getMovieById(id)

    suspend fun movieWithTitleExists(title: String): Boolean =
        movieDao.countMoviesWithTitle(title) > 0

    suspend fun movieWithTitleExistsExcludingId(title: String, movieId: Int): Boolean =
        movieDao.countMoviesWithTitleExcludingId(title, movieId) > 0

    fun getFavoriteMovies(): LiveData<List<MovieEntity>> = movieDao.getFavoriteMovies()

    suspend fun updateMovieLikeStatus(movieId: Int, isLiked: Boolean) =
        movieDao.updateMovieLikeStatus(movieId, isLiked)
}