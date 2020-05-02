package com.example.csisongbook.songs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.csisongbook.Song
import com.example.csisongbook.databinding.SongPageItemBinding

class SongDisplayAdapter(val clickListener: SongDisplayListener) : ListAdapter<Song,
        SongDisplayAdapter.ViewHolder>(SongDisplayDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(clickListener,item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: SongPageItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: SongDisplayListener, item: Song) {
            binding.songDisplayItem = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SongPageItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class SongDisplayDiffCallback : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.songId == newItem.songId
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }
}

class SongDisplayListener(val clickListener: (songId: Int) -> Unit) {
    fun onClick(song: Song) = clickListener(song.songId)
}
