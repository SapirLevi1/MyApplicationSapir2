package com.example.myapplicationsapir.ui.fragments.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplicationsapir.R
import com.example.myapplicationsapir.data.local.entity.MovieEntity
import com.example.myapplicationsapir.databinding.FragmentFavoriteMoviesBinding
import com.example.myapplicationsapir.ui.fragments.movies.adapter.MovieAdapter
import com.example.myapplicationsapir.viewmodel.movies.MoviesViewModel
import com.example.myapplicationsapir.viewmodel.factory.MovieViewModelFactoryProvider

class FavoriteMoviesFragment : Fragment() {

    private var _binding: FragmentFavoriteMoviesBinding? = null
    private val binding get() = _binding!!

    private val moviesViewModel: MoviesViewModel by viewModels {
        MovieViewModelFactoryProvider.getFactory(requireContext())
    }

    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false)

        binding.fabBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieAdapter(object : MovieAdapter.MovieListener {
            override fun onMovieClicked(movie: MovieEntity) {
                findNavController().navigate(
                    R.id.action_favoriteMoviesFragment_to_detailMovieFragment,
                    bundleOf("movieId" to movie.id)
                )
            }
        })

        binding.favoriteRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteRecycler.adapter = movieAdapter

        moviesViewModel.favoriteMovies.observe(viewLifecycleOwner) { favorites ->
            if (favorites.isEmpty()) {
                binding.emptyStateText.visibility = View.VISIBLE
                binding.favoriteRecycler.visibility = View.GONE
            } else {
                binding.emptyStateText.visibility = View.GONE
                binding.favoriteRecycler.visibility = View.VISIBLE
                movieAdapter.submitList(favorites)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
