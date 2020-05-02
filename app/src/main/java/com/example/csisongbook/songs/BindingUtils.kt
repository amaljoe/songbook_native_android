package com.example.csisongbook.songs

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.csisongbook.Song

@BindingAdapter("SongNumber")
fun TextView.setSongNumber(item: Song?){
    item?.let {
        text = item.songId.toString()
    }
}