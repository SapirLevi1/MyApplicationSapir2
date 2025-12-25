package com.example.myapplicationsapir.ui.all_characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplicationsapir.data.model.MovieEntity
import com.example.myapplicationsapir.databinding.MovieLayoutBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovieAdapter(
    private val callback: MovieListener
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movieEntities: List<MovieEntity> = emptyList()

    interface MovieListener {
        fun onMovieClicked(movie: MovieEntity)
    }

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    inner class MovieViewHolder(private val binding: MovieLayoutBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                callback.onMovieClicked(movieEntities[position])
            }
        }

        fun bind(movie: MovieEntity) {
            binding.movieTitle.text = movie.title
            binding.movieDescription.text = movie.description

            movie.watchedDate?.let {
                binding.movieWatchedDate.text = "Watched: ${formatWatchedDate(movie.watchedDate)}"
            } ?: run {
                binding.movieWatchedDate.visibility = View.GONE
            }

            movie.score?.let {
                binding.movieScore.text = "Score: ${movie.score}/10"
            } ?:run {
                binding.movieScore.visibility = View.GONE
            }


            Glide.with(binding.root)
                .load(movie.imageUri)
                .circleCrop()
                .into(binding.movieImage)
        }

        private fun formatWatchedDate(watchedDateMillis: Long): String {
            return dateFormat.format(Date(watchedDateMillis))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            MovieLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieEntities[position])
    }

    override fun getItemCount(): Int = movieEntities.size

    fun submitList(newList: List<MovieEntity>) {
        movieEntities = newList
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): MovieEntity = movieEntities[position]
}
