package com.example.myapplicationsapir.ui.add_character

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
import com.example.myapplicationsapir.R
import com.example.myapplicationsapir.data.local_db.MovieDatabase
import com.example.myapplicationsapir.data.model.MovieEntity
import com.example.myapplicationsapir.data.repository.MovieRepository
import com.example.myapplicationsapir.databinding.AddMovieLayoutBinding
import com.example.myapplicationsapir.ui.view_models.MoviesViewModel
import com.example.myapplicationsapir.ui.view_models.MoviesViewModelFactory
import com.example.myapplicationsapir.ui.view_models.SaveMovieResult
import java.util.Calendar

class AddMovieFragment : Fragment() {

    private var _binding: AddMovieLayoutBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null
    private var selectedWatchedDateMillis: Long? = null

    private val moviesViewModel: MoviesViewModel by viewModels {
        val dao = MovieDatabase.getDatabase(requireContext().applicationContext).movieDao()
        val repo = MovieRepository(dao)
        MoviesViewModelFactory(repo)
    }

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddMovieLayoutBinding.inflate(inflater, container, false)

        binding.datePickerEdittext.setOnClickListener {
            showWatchedDatePicker()
        }

        binding.datePickerLayout.setOnClickListener {
            showWatchedDatePicker()
        }

        binding.imageBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        binding.finishBtn.setOnClickListener {
            onSaveClicked()
        }

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun showWatchedDatePicker() {
        val now = Calendar.getInstance()

        DatePickerDialog(
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
        ).show()
    }

    private fun onSaveClicked() {
        val title = binding.movieTitle.text.toString().trim()
        val desc = binding.movieDescription.text.toString().trim()
        val scoreStr = binding.scoreInput.text.toString().trim()
        val watchedMillis = selectedWatchedDateMillis

        if (title.isEmpty()) {
            binding.movieTitle.error = "Title is required"
            return
        }

        val now = System.currentTimeMillis()
        if (watchedMillis != null && watchedMillis > now) {
            binding.datePickerEdittext.error = "Watched date can't be in the future"
            Toast.makeText(requireContext(),
                "Watched date can't be in the future",
                Toast.LENGTH_SHORT).show()
            return
        }

        val score = scoreStr.toIntOrNull()
        if (score != null && score !in 1..10) {
            binding.scoreInput.error = "Grade must be between 1-10"
            return
        }

        val movie = MovieEntity(
            title = title,
            description = desc,
            imageUri = imageUri?.toString(),
            watchedDate = watchedMillis,
            score = score
        )

        moviesViewModel.addMovieWithValidation(movie) { result ->
            when (result) {
                is SaveMovieResult.Success -> {
                    findNavController().navigate(R.id.action_addMovieFragment_to_allMoviesFragment)
                }
                is SaveMovieResult.Error -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
