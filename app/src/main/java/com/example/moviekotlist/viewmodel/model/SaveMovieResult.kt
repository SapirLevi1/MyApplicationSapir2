package com.example.moviekotlist.viewmodel.model

sealed class SaveMovieResult {
    object Success : SaveMovieResult()
    data class Error(val message: String) : SaveMovieResult()
}