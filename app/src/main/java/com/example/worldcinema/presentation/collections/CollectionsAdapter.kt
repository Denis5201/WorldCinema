package com.example.worldcinema.presentation.collections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.worldcinema.R
import com.example.worldcinema.databinding.ItemCollectionBinding
import com.example.worldcinema.domain.model.CollectionInfo

class CollectionsAdapter(
    private val click: (CollectionInfo) -> Unit
) : RecyclerView.Adapter<CollectionsAdapter.CollectionViewHolder>() {

    var collectionList: List<CollectionInfo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding = ItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(collectionList[position], click)
    }

    override fun getItemCount(): Int = collectionList.size

    class CollectionViewHolder(private val binding: ItemCollectionBinding) : ViewHolder(binding.root) {

        fun bind(collection: CollectionInfo, click: (CollectionInfo) -> Unit) {
            binding.nameCollectionItem.text = collection.name

            if (collection.image == null) {
                binding.imageCollectionItem.setImageResource(R.drawable.collection_icon_1)
            } else {
                val context = this.itemView.context
                val iconId = context.resources.getIdentifier(collection.image, "drawable", context.packageName)
                binding.imageCollectionItem.setImageResource(iconId)
            }

            binding.root.setOnClickListener {
                click(collection)
            }
        }
    }
}