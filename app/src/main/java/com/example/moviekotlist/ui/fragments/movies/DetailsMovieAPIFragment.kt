package com.example.moviekotlist.ui.fragments.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.moviekotlist.BuildConfig
import com.example.moviekotlist.R
import com.example.moviekotlist.Retrofit.dto.OmdbMovieDetailsDto
import com.example.moviekotlist.data.local.entity.MovieEntity
import com.example.moviekotlist.databinding.FragmentDetailsMovieApiBinding
import com.example.moviekotlist.viewmodel.factory.MovieDetailsAPIViewModelFactory
import com.example.moviekotlist.viewmodel.factory.MovieViewModelFactoryProvider
import com.example.moviekotlist.viewmodel.model.MovieDetailsAPIViewModel
import com.example.moviekotlist.viewmodel.model.SaveMovieResult
import com.example.moviekotlist.viewmodel.movies.MoviesViewModel
import kotlinx.coroutines.launch

class DetailsMovieAPIFragment : Fragment() {
    private var _binding: FragmentDetailsMovieApiBinding? = null
    private val binding get() = _binding!!

    private val movieDetailsAPIViewModel: MovieDetailsAPIViewModel by viewModels {
        MovieDetailsAPIViewModelFactory(BuildConfig.OMDB_API_KEY)
    }

    private var currentDetails: OmdbMovieDetailsDto? = null

    private val moviesViewModel: MoviesViewModel by viewModels {
        MovieViewModelFactoryProvider.getFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsMovieApiBinding.inflate(inflater, container, false)

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addBtn.setOnClickListener {
            val d = currentDetails
            if (d == null) {
                Toast.makeText(requireContext(), getString(R.string.omdb_movie_details_not_loaded), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            fun clean(x: String?) = x?.takeIf { it.isNotBlank() && it != "N/A" } ?: ""

            val movie = MovieEntity(
                title = clean(d.Title),
                description = clean(d.Plot),
                imageUri = clean(d.Poster),
                watchedDate = null,
                score = null
            )

            moviesViewModel.addMovieWithValidation(movie) { result ->
                when (result) {
                    is SaveMovieResult.Success -> {
                        Toast.makeText(requireContext(), getString(R.string.omdb_movie_added), Toast.LENGTH_SHORT).show()
                    }
                    is SaveMovieResult.Error -> {
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imdbId = requireArguments().getString("imdbId") ?: return
        movieDetailsAPIViewModel.load(imdbId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieDetailsAPIViewModel.state.collect { state ->
                    state.item?.let {
                        currentDetails = it
                        bindDetails(it)
                    }
                }
            }
        }
    }

    private fun bindDetails(d: OmdbMovieDetailsDto) = with(binding) {
        fun clean(x: String?) = x?.takeIf { it.isNotBlank() && it != "N/A" } ?: ""

        movieTitle.text = clean(d.Title)
        movieYear.text = "Year: ${clean(d.Year)}"
        movieReleased.text = "Released: ${clean(d.Released)}"
        movieRuntime.text = "Runtime: ${clean(d.Runtime)}"
        movieGenre.text = "Genre: ${clean(d.Genre)}"
        movieDirector.text = "Director: ${clean(d.Director)}"
        movieWriter.text = "Writer: ${clean(d.Writer)}"
        movieActors.text = "Actors: ${clean(d.Actors)}"
        moviePlot.text = clean(d.Plot)
        movieLanguage.text = "Language: ${clean(d.Language)}"
        movieCountry.text = "Country: ${clean(d.Country)}"
        movieAwards.text = "Awards: ${clean(d.Awards)}"
        movieImdbRating.text = "IMDb Rating: ${clean(d.imdbRating)}"
        movieImdbId.text = "IMDb ID: ${clean(d.imdbID)}"
        movieBoxoffice.text = "BoxOffice: ${clean(d.BoxOffice)}"

        movieImage.load(d.Poster)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
