package com.example.myapplicationsapir.Retrofit.dto

data class OmdbSearchResponseDto(
    val Search: List<OmdbSearchMovieDto>?,
    val totalResults: String?,
    val Response: String,
    val Error: String? = null
)

data class OmdbSearchMovieDto(
    val Title: String,
    val Year: String,
    val imdbID: String,
    val Type: String,
    val Poster: String
)

data class OmdbMovieDetailsDto(
    val Title: String?,
    val Year: String?,
    val Released: String?,
    val Runtime: String?,
    val Genre: String?,
    val Director: String?,
    val Writer: String?,
    val Actors: String?,
    val Plot: String?,
    val Language: String?,
    val Country: String?,
    val Awards: String?,
    val Poster: String?,
    val imdbRating: String?,
    val imdbID: String?,
    val BoxOffice: String?,
    val Response: String,
    val Error: String? = null
)
