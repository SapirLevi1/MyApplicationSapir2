package com.example.myapplicationsapir.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplicationsapir.data.local.entity.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun addMovie(movieEntity: MovieEntity)

    @Delete
    suspend fun deleteMovie(vararg movieEntity: MovieEntity)

    @Update
    suspend fun updateMovie(movieEntity: MovieEntity)

    @Query("SELECT * FROM movies ORDER BY title ASC")
    fun getAllMovies() : LiveData<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    fun getMovieById(id:Int) : LiveData<MovieEntity>

    @Query("SELECT COUNT(*) FROM movies WHERE title = :title")
    suspend fun countMoviesWithTitle(title: String): Int

    @Query("SELECT COUNT(*) FROM movies WHERE title = :title AND id != :movieId")
    suspend fun countMoviesWithTitleExcludingId(title: String, movieId: Int): Int

    @Query("SELECT * FROM movies WHERE is_liked = 1 ORDER BY title ASC")
    fun getFavoriteMovies(): LiveData<List<MovieEntity>>

    @Query("UPDATE movies SET is_liked = :isLiked WHERE id = :movieId")
    suspend fun updateMovieLikeStatus(movieId: Int, isLiked: Boolean)

    @Query("SELECT COUNT(*) FROM movies WHERE score IS NULL")
    suspend fun countUnratedMovies(): Int

}