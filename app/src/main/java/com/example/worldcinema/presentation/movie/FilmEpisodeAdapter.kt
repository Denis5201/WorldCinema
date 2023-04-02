package com.example.worldcinema.presentation.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worldcinema.R
import com.example.worldcinema.databinding.ItemEpisodeBinding
import com.example.worldcinema.domain.model.Episode

class FilmEpisodeAdapter(private val click: (String) -> Unit) : RecyclerView.Adapter<FilmEpisodeAdapter.EpisodeViewHolder>() {

    var episodeList: List<Episode> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(binding)
    }

    override fun getItemCount(): Int = episodeList.size

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(episodeList[position], click)
    }

    class EpisodeViewHolder( private val binding: ItemEpisodeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: Episode, click: (String) -> Unit) {

            Glide.with(this.itemView.context)
                .load(episode.preview)
                .error(R.drawable.logo)
                .into(binding.previewEpisodeItem)

            binding.nameEpisodeItem.text = episode.name
            binding.desciptionEpisodeItem.text = episode.description
            binding.yearEpisodeItem.text = episode.year.toString()

            binding.root.setOnClickListener {
                click(episode.episodeId)
            }
        }
    }
}