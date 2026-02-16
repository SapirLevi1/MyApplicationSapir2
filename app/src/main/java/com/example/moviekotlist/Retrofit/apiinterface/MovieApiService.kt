package com.example.moviekotlist.Retrofit.apiinterface

import com.example.moviekotlist.Retrofit.dto.OmdbMovieDetailsDto
import com.example.moviekotlist.Retrofit.dto.OmdbSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET(".")
    suspend fun searchMoviesByTitle(
        @Query("apikey") apiKey: String,
        @Query("s") title: String,
        @Query("type") type: String = "movie",
        @Query("page") page: Int = 1
    ): OmdbSearchResponseDto

    @GET(".")
    suspend fun searchMoviesByTitleAndYear(
        @Query("apikey") apiKey: String,
        @Query("s") title: String,
        @Query("y") year: String,
        @Query("type") type: String = "movie",
        @Query("page") page: Int = 1
    ): OmdbSearchResponseDto

    @GET(".")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") imdbId: String,
        @Query("plot") plot: String = "short"
    ): OmdbMovieDetailsDto
}