package com.example.myapplicationsapir.viewmodel.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationsapir.Retrofit.dto.OmdbSearchMovieDto
import com.example.myapplicationsapir.repository.MovieAPIRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class OMDBUiState(
    val loading: Boolean = false,
    val items: List<OmdbSearchMovieDto> = emptyList(),
    val error: String? = null
)

class MovieAPIViewModel(
    private val repo: MovieAPIRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OMDBUiState())
    val state: StateFlow<OMDBUiState> = _state

    fun load(title: String, year: String?) {
        viewModelScope.launch {
            _state.value = OMDBUiState(loading = true)

            try {
                val res = if (year.isNullOrBlank()) {
                    repo.searchMoviesByTitle(title)
                } else {
                    repo.searchMoviesByTitleAndYear(title, year.trim())
                }

                if (res.Response == "True" && !res.Search.isNullOrEmpty()) {
                    _state.value = OMDBUiState(items = res.Search)
                } else {
                    _state.value = OMDBUiState(error = res.Error ?: "No results")
                }
            } catch (e: Exception) {
                _state.value = OMDBUiState(error = e.message ?: "Network error")
            }
        }
    }

}
