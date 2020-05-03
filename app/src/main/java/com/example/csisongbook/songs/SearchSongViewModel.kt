package com.example.csisongbook.songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.csisongbook.SongDatabaseDao

class SearchSongViewModel(
    private val database: SongDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    val itemClicked = MutableLiveData<Int>()

    fun onItemClick(songId: Int) {
        itemClicked.value = songId
    }



}