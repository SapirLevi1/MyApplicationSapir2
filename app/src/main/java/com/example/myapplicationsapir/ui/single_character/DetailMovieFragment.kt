package com.example.myapplicationsapir.ui.single_character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myapplicationsapir.R
import com.example.myapplicationsapir.data.local_db.MovieDatabase
import com.example.myapplicationsapir.data.repository.MovieRepository
import com.example.myapplicationsapir.databinding.DetailMovieLayoutBinding
import com.example.myapplicationsapir.ui.view_models.MoviesViewModel
import com.example.myapplicationsapir.ui.view_models.MoviesViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailMovieFragment : Fragment() {

    private var _binding: DetailMovieLayoutBinding? = null
    private val binding get() = _binding!!

    private val moviesViewModel: MoviesViewModel by viewModels {
        val dao = MovieDatabase.getDatabase(requireContext().applicationContext).movieDao()
        val repo = MovieRepository(dao)
        MoviesViewModelFactory(repo)
    }

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailMovieLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = arguments?.getInt("movieId") ?: return

        moviesViewModel.getMovieById(movieId).observe(viewLifecycleOwner) { movie ->
            binding.movieTitle.text = movie.title
            binding.movieDesc.text = movie.description

            movie.watchedDate?.let {
                binding.detailWatchedDate.text =
                    "Watched: ${dateFormat.format(Date(movie.watchedDate))}"
            } ?: run{
                binding.detailWatchedDate.visibility = View.GONE
            }

            movie.score?.let {
                binding.detailScore.text = "Score: ${movie.score}/10"
            } ?: run {
                binding.detailScore.visibility = View.GONE
            }

            Glide.with(binding.root)
                .load(movie.imageUri)
                .circleCrop()
                .into(binding.movieImage)

            binding.editBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_detailMovieFragment_to_editMovieFragment,
                    bundleOf("movieId" to movieId)
                )
            }

            binding.backBtn.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
