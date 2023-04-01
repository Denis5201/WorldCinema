package com.example.worldcinema.presentation.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worldcinema.R
import com.example.worldcinema.databinding.ItemPosterBinding
import com.example.worldcinema.domain.model.Movie

class MainAdapter(
    private val isNewMovies: Boolean = false,
    private val click: (Movie) -> Unit
) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    var posterList: List<Movie> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemPosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(posterList[position], isNewMovies, click)
    }

    override fun getItemCount(): Int = posterList.size

    class MainViewHolder(private val binding: ItemPosterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie, isNewMovies: Boolean, click: (Movie) -> Unit) {
            if (isNewMovies) {
                binding.imagePoster.layoutParams.width =
                    (NEW_WIDTH * this.itemView.context.resources.displayMetrics.density + 0.5f).toInt()
            }
            Glide.with(this.itemView.context)
                .load(movie.poster)
                .error(R.drawable.logo)
                .into(binding.imagePoster)

            binding.imagePoster.setOnClickListener {
                click(movie)
            }
        }

        companion object {
            const val NEW_WIDTH = 250
        }
    }
}