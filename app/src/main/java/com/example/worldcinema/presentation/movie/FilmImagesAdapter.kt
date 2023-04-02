package com.example.worldcinema.presentation.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worldcinema.R
import com.example.worldcinema.databinding.ItemPosterBinding

class FilmImagesAdapter() : RecyclerView.Adapter<FilmImagesAdapter.ImagesViewHolder>()  {

    var imagesList: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding = ItemPosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImagesViewHolder(binding)
    }

    override fun getItemCount(): Int = imagesList.size

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.bind(imagesList[position])
    }

    class ImagesViewHolder(
        private val binding: ItemPosterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: String) {
            binding.imagePoster.layoutParams.width =
                (IMAGE_WIDTH * this.itemView.context.resources.displayMetrics.density + 0.5f).toInt()
            binding.imagePoster.layoutParams.height =
                (IMAGE_HEIGHT * this.itemView.context.resources.displayMetrics.density + 0.5f).toInt()

            Glide.with(this.itemView.context)
                .load(image)
                .error(R.drawable.logo)
                .into(binding.imagePoster)
        }

        companion object {
            const val IMAGE_WIDTH = 128
            const val IMAGE_HEIGHT = 72
        }
    }
}