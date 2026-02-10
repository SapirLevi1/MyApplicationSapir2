package com.example.myapplicationsapir.ui.fragments.movies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplicationsapir.R
import com.example.myapplicationsapir.data.local.entity.MovieEntity
import com.example.myapplicationsapir.databinding.RowMovieRecyclerviewBinding
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

    inner class MovieViewHolder(private val binding: RowMovieRecyclerviewBinding) :
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
            val ctx = binding.root.context

            binding.movieTitle.text = movie.title
            binding.movieDescription.text = movie.description

            movie.watchedDate?.let { watchedMillis ->
                val formatted = dateFormat.format(Date(watchedMillis))
                binding.movieWatchedDate.text = ctx.getString(R.string.watched_format, formatted)
                binding.movieWatchedDate.visibility = View.VISIBLE
            } ?: run {
                binding.movieWatchedDate.visibility = View.GONE
            }

            movie.score?.let { score ->
                binding.movieScore.text = ctx.getString(R.string.score_format, score)
                binding.movieScore.visibility = View.VISIBLE
            } ?: run {
                binding.movieScore.visibility = View.GONE
            }

            Glide.with(binding.root)
                .load(movie.imageUri)
                .circleCrop()
                .into(binding.movieImage)

            // Update heart indicator
            if (movie.isLiked) {
                binding.heartIndicator.setImageResource(R.drawable.ic_heart_filled)
                binding.heartIndicator.setColorFilter(ctx.getColor(android.R.color.holo_red_light))
            } else {
                binding.heartIndicator.setImageResource(R.drawable.ic_heart_outline)
                binding.heartIndicator.clearColorFilter()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            RowMovieRecyclerviewBinding.inflate(
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