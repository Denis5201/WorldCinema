package com.example.worldcinema.presentation.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worldcinema.databinding.ItemCompilationBinding
import com.example.worldcinema.domain.model.Movie

class CompilationAdapter : RecyclerView.Adapter<CompilationAdapter.CardViewHolder>() {

    var movieList: MutableList<Movie> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCompilationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    class CardViewHolder(private val binding: ItemCompilationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            Glide.with(this.itemView.context)
                .load(movie.poster)
                .into(binding.compilationPoster)
        }
    }

    /*fun removeElement(index: Int) {
        movieList.removeAt(index)
        notifyItemRemoved(index)
    }*/
}