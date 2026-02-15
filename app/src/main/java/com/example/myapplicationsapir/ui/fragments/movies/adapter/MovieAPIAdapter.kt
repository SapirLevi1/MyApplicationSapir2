package com.example.myapplicationsapir.ui.fragments.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplicationsapir.Retrofit.dto.OmdbSearchMovieDto
import com.example.myapplicationsapir.databinding.RowMovieApiRecyclerviewBinding

class MovieAPIAdapter(
    private val onItemClick: (OmdbSearchMovieDto) -> Unit
) : ListAdapter<OmdbSearchMovieDto, MovieAPIAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<OmdbSearchMovieDto>() {
        override fun areItemsTheSame(a: OmdbSearchMovieDto, b: OmdbSearchMovieDto) = a.imdbID == b.imdbID
        override fun areContentsTheSame(a: OmdbSearchMovieDto, b: OmdbSearchMovieDto) = a == b
    }

    class VH(val binding: RowMovieApiRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RowMovieApiRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)

        holder.binding.movieTitle.text = item.Title
        holder.binding.movieYear.text = "Year: ${item.Year}"
        holder.binding.movieImage.load(item.Poster)
        holder.binding.movieImdbId.text = "IMDB ID: "+item.imdbID

        holder.itemView.setOnClickListener { onItemClick(item) }
    }
}
