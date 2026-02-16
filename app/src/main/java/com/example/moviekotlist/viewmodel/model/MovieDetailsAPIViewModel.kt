package com.example.moviekotlist.viewmodel.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviekotlist.Retrofit.dto.OmdbMovieDetailsDto
import com.example.moviekotlist.repository.MovieAPIRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MovieDetailsUiState(
    val loading: Boolean = false,
    val item: OmdbMovieDetailsDto? = null,
    val error: String? = null
)

class MovieDetailsAPIViewModel(
    private val repo: MovieAPIRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailsUiState())
    val state: StateFlow<MovieDetailsUiState> = _state

    fun load(imdbId: String) {
        viewModelScope.launch {
            _state.value = MovieDetailsUiState(loading = true)

            try {
                val res = repo.getDetailsByImdbId(imdbId)

                if (res.Response == "True") {
                    _state.value = MovieDetailsUiState(item = res)
                } else {
                    _state.value = MovieDetailsUiState(error = res.Error ?: "Error")
                }
            } catch (e: Exception) {
                _state.value = MovieDetailsUiState(error = e.message ?: "Network error")
            }
        }
    }
}
