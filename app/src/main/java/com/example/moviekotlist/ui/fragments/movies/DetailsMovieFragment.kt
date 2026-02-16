package com.example.moviekotlist.ui.fragments.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.moviekotlist.R
import com.example.moviekotlist.databinding.FragmentDetailsMovieBinding
import com.example.moviekotlist.viewmodel.movies.MoviesViewModel
import com.example.moviekotlist.viewmodel.factory.MovieViewModelFactoryProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailsMovieFragment : Fragment() {

    private var _binding: FragmentDetailsMovieBinding? = null
    private val binding get() = _binding!!

    private val moviesViewModel: MoviesViewModel by viewModels {
        MovieViewModelFactoryProvider.getFactory(requireContext())
    }

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = arguments?.getInt("movieId") ?: return


        binding.editBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_detailMovieFragment_to_editMovieFragment,
                bundleOf("movieId" to movieId)
            )
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        moviesViewModel.getMovieById(movieId).observe(viewLifecycleOwner) { movie ->
            binding.movieTitle.text = movie.title
            binding.movieDesc.text = movie.description

            // Update favorite button
            updateFavoriteButton(movie.isLiked)

            // Set click listener for favorite button
            binding.favoriteBtn.setOnClickListener {
                moviesViewModel.toggleMovieLike(movieId, !movie.isLiked)
            }

            movie.watchedDate?.let { watchedMillis ->
                val formattedDate = dateFormat.format(Date(watchedMillis))
                binding.detailWatchedDate.text =
                    getString(R.string.watched_format, formattedDate)
                binding.detailWatchedDate.visibility = View.VISIBLE
            } ?: run {
                binding.detailWatchedDate.visibility = View.GONE
            }


            movie.score?.let { score ->
                binding.detailScore.text =
                    getString(R.string.score_format, score)
                binding.detailScore.visibility = View.VISIBLE
            } ?: run {
                binding.detailScore.visibility = View.GONE
            }

            Glide.with(binding.root)
                .load(movie.imageUri)
                .centerCrop()
                .into(binding.movieImage)
        }
    }

    private fun updateFavoriteButton(isLiked: Boolean) {
        if (isLiked) {
            binding.favoriteBtn.text = getString(R.string.action_remove_from_favorites)
            binding.favoriteBtn.setIconResource(R.drawable.ic_heart_filled)
            binding.favoriteBtn.setIconTintResource(android.R.color.holo_red_light)
        } else {
            binding.favoriteBtn.text = getString(R.string.action_add_to_favorites)
            binding.favoriteBtn.setIconResource(R.drawable.ic_heart_outline)
            binding.favoriteBtn.setIconTintResource(R.color.app_text_secondary)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}