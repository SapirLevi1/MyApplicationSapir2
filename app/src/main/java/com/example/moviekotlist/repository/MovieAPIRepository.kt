package com.example.moviekotlist.repository

import com.example.moviekotlist.Retrofit.client.RetrofitClient

class MovieAPIRepository(
    private val apiKey: String
) {
    suspend fun searchMoviesByTitle(title: String) =
        RetrofitClient.api.searchMoviesByTitle(apiKey = apiKey, title = title)

    suspend fun searchMoviesByTitleAndYear(title: String, year: String) =
        RetrofitClient.api.searchMoviesByTitleAndYear(apiKey = apiKey, title = title, year = year)

    suspend fun getDetailsByImdbId(imdbId: String) =
        RetrofitClient.api.getMovieDetails(apiKey, imdbId, plot = "short")
}