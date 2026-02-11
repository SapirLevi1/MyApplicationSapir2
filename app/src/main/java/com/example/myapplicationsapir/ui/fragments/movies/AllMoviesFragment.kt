package com.example.myapplicationsapir.ui.fragments.movies

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationsapir.R
import com.example.myapplicationsapir.data.local.entity.MovieEntity
import com.example.myapplicationsapir.databinding.FragmentAllMoviesBinding
import com.example.myapplicationsapir.ui.fragments.movies.adapter.MovieAdapter
import com.example.myapplicationsapir.viewmodel.movies.MoviesViewModel
import com.example.myapplicationsapir.viewmodel.factory.MovieViewModelFactoryProvider

class AllMoviesFragment : Fragment() {

    private var _binding: FragmentAllMoviesBinding? = null
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
        _binding = FragmentAllMoviesBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_allMoviesFragment_to_addMoviesFragment)
        }

        binding.omdbBtn.setOnClickListener {
            findNavController().navigate(R.id.action_allMoviesFragment_to_omdbFragment)
        }

        binding.favoritesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_allMoviesFragment_to_favoriteMoviesFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieAdapter(object : MovieAdapter.MovieListener {

            override fun onMovieClicked(movie: MovieEntity) {
                findNavController().navigate(
                    R.id.action_allMoviesFragment_to_detailMovieFragment,
                    bundleOf("movieId" to movie.id)
                )
            }
        })

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = movieAdapter

        moviesViewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (movies.isEmpty()) {
                binding.emptyStateText.visibility = View.VISIBLE
                binding.recycler.visibility = View.GONE
            } else {
                binding.emptyStateText.visibility = View.GONE
                binding.recycler.visibility = View.VISIBLE
            }
            movieAdapter.submitList(movies)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return

                val movie = movieAdapter.getItemAt(position)

                showDeleteConfirmationDialog(movie, position)
            }
        }).attachToRecyclerView(binding.recycler)

    }


    private fun showDeleteConfirmationDialog(movie: MovieEntity, position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_delete_title))
            .setMessage(getString(R.string.dialog_delete_message, movie.title))
            .setPositiveButton(getString(R.string.action_delete)) { _, _ ->
                moviesViewModel.deleteMovie(movie)
            }
            .setNegativeButton(getString(R.string.action_cancel)) { _, _ ->
                movieAdapter.notifyItemChanged(position)
            }
            .setOnCancelListener {
                // Handles back press / outside tap
                movieAdapter.notifyItemChanged(position)
            }
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}