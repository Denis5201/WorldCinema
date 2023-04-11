package com.example.worldcinema.presentation.collections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.worldcinema.R
import com.example.worldcinema.databinding.ItemFilmCollectionBinding
import com.example.worldcinema.domain.model.Movie

class FilmsCollectionAdapter(
    private val click: (Movie) -> Unit
) : RecyclerView.Adapter<FilmsCollectionAdapter.FilmsViewHolder>() {

    var movieList: MutableList<Movie> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsViewHolder {
        val binding = ItemFilmCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmsViewHolder(binding)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: FilmsViewHolder, position: Int) {
        holder.bind(movieList[position], click)
    }

    class FilmsViewHolder(private val binding: ItemFilmCollectionBinding) : ViewHolder(binding.root) {

        fun bind(movie: Movie, click: (Movie) -> Unit) {

            Glide.with(this.itemView.context)
                .load(movie.poster)
                .error(R.drawable.logo)
                .into(binding.posterFilmCollectionItem)

            binding.nameFilmCollectionItem.text = movie.name
            binding.descriptionFilmCollectionItem.text = movie.description

            binding.root.setOnClickListener {
                click(movie)
            }
        }
    }

    fun deleteFilm(position: Int) {
        movieList.removeAt(position)
        notifyItemRemoved(position)
    }
}