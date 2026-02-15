package com.example.myapplicationsapir.ui.fragments.movies

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myapplicationsapir.R
import com.example.myapplicationsapir.data.local.entity.MovieEntity
import com.example.myapplicationsapir.databinding.FragmentEditMovieBinding
import com.example.myapplicationsapir.viewmodel.factory.MovieViewModelFactoryProvider
import com.example.myapplicationsapir.viewmodel.model.SaveMovieResult
import com.example.myapplicationsapir.viewmodel.movies.MoviesViewModel
import java.util.Calendar

class EditMovieFragment : Fragment() {

    private var _binding: FragmentEditMovieBinding? = null
    private val binding get() = _binding!!

    private val moviesViewModel: MoviesViewModel by viewModels {
        MovieViewModelFactoryProvider.getFactory(requireContext())
    }

    private var movieId: Int = -1
    private var selectedWatchedDateMillis: Long? = null
    private var imageUri: Uri? = null

    private val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                binding.resultImage.setImageURI(uri)
                requireActivity().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imageUri = uri
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = arguments?.getInt("movieId") ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditMovieBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.datePickerEdittext.setOnClickListener { showWatchedDatePicker() }
        binding.datePickerLayout.setEndIconOnClickListener { showWatchedDatePicker() }

        binding.imageBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        binding.saveBtn.setOnClickListener {
            onSaveClicked()
        }

        binding.scoreInput.filters = arrayOf(android.text.InputFilter { source, start, end, dest, dstart, dend ->
            val newText = dest.toString()
                .substring(0, dstart) + source.subSequence(start, end) + dest.toString().substring(dend)

            if (newText.isBlank()) return@InputFilter null

            val value = newText.toIntOrNull() ?: return@InputFilter ""
            if (value in 1..10) null else ""
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (movieId == -1) {
            Toast.makeText(
                requireContext(),
                getString(R.string.error_invalid_movie_id),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigateUp()
            return
        }

        // Prefill fields from DB
        moviesViewModel.getMovieById(movieId).observe(viewLifecycleOwner) { movie ->
            binding.movieTitle.setText(movie.title)
            binding.movieDescription.setText(movie.description)

            selectedWatchedDateMillis = movie.watchedDate
            binding.datePickerEdittext.setText(movie.watchedDate?.let { formatDate(it) } ?: "")

            binding.scoreInput.setText(movie.score?.toString() ?: "")

            imageUri = movie.imageUri?.let { Uri.parse(it) }
            if (!movie.imageUri.isNullOrBlank()) {
                Glide.with(binding.root).load(movie.imageUri).circleCrop().into(binding.resultImage)
            } else {
                binding.resultImage.setImageResource(R.mipmap.ic_launcher)
            }
        }
    }

    private fun showWatchedDatePicker() {
        val cal = Calendar.getInstance()

        selectedWatchedDateMillis?.let { cal.timeInMillis = it }

        val now = Calendar.getInstance()

        val picker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val chosen = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                selectedWatchedDateMillis = chosen.timeInMillis
                binding.datePickerEdittext.setText("${dayOfMonth}/${month + 1}/$year")
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        )

        picker.datePicker.maxDate = System.currentTimeMillis()

        picker.show()
    }

    private fun onSaveClicked() {
        val title = binding.movieTitle.text?.toString()?.trim().orEmpty()
        val desc = binding.movieDescription.text?.toString()?.trim().orEmpty()
        val scoreStr = binding.scoreInput.text?.toString()?.trim().orEmpty()

        val score: Int? = scoreStr.toIntOrNull()
        val watchedDate: Long? = selectedWatchedDateMillis

        if (title.isBlank()) {
            binding.movieTitle.error = getString(R.string.error_title_required)
            return
        }

        val updatedMovie = MovieEntity(
            title = title,
            description = desc,
            watchedDate = watchedDate,
            score = score,
            imageUri = imageUri?.toString()
        ).apply {
            id = movieId
        }

        moviesViewModel.updateMovieWithValidation(updatedMovie) { result ->
            when (result) {
                is SaveMovieResult.Success -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_movie_updated),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                }

                is SaveMovieResult.Error -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun formatDate(millis: Long): String {
        val cal = Calendar.getInstance().apply { timeInMillis = millis }
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH) + 1
        val year = cal.get(Calendar.YEAR)
        return "$day/$month/$year"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}