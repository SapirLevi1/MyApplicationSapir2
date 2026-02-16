package com.example.moviekotlist.ui.fragments.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviekotlist.BuildConfig
import com.example.moviekotlist.R
import com.example.moviekotlist.databinding.FragmentOmdbBinding
import com.example.moviekotlist.ui.fragments.movies.adapter.MovieAPIAdapter
import com.example.moviekotlist.viewmodel.factory.MovieAPIViewModelFactory
import com.example.moviekotlist.viewmodel.model.MovieAPIViewModel
import kotlinx.coroutines.launch

class OMDBFragment : Fragment() {

    private var _binding: FragmentOmdbBinding? = null
    private val binding get() = _binding!!

    private val movieAPIViewModel: MovieAPIViewModel by viewModels {
        MovieAPIViewModelFactory(BuildConfig.OMDB_API_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOmdbBinding.inflate(inflater, container, false)

        binding.fabBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MovieAPIAdapter { movie ->
            val bundle = Bundle().apply {
                putString("imdbId", movie.imdbID)
            }
            findNavController().navigate(R.id.detailsMovieAPIFragment, bundle)
        }
        binding.omdbRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.omdbRecycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieAPIViewModel.state.collect { state ->

                    when {
                        state.loading -> {
                            binding.emptyStateText.visibility = View.VISIBLE
                            binding.emptyStateText.text =
                                getString(R.string.loading_omdb_message)

                            binding.omdbRecycler.visibility = View.GONE
                        }

                        state.items.isEmpty() -> {
                            binding.emptyStateText.visibility = View.VISIBLE
                            binding.emptyStateText.text =
                                state.error ?: getString(R.string.empty_omdb_message)

                            binding.omdbRecycler.visibility = View.GONE
                        }

                        else -> {
                            binding.emptyStateText.visibility = View.GONE
                            binding.omdbRecycler.visibility = View.VISIBLE
                        }
                    }

                    adapter.submitList(state.items)
                }
            }
        }

        binding.searchButton.setOnClickListener {
            val title = binding.searchNameEditText.text?.toString()?.trim().orEmpty()
            val year = binding.searchYearEditText.text?.toString()?.trim()

            if (title.isNotEmpty()) {
                movieAPIViewModel.load(title, year)
            }
        }


        super.onViewCreated(view, savedInstanceState)
    }
}