package com.example.csisongbook.songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.csisongbook.SongDatabaseDao

class SongListViewModel(
    database: SongDatabaseDao,
    application: Application
) : AndroidViewModel(application) {
    val songs=database.getAllSongs()
    val itemClicked = MutableLiveData<Int>()

    fun onItemClick(songId: Int) {
        itemClicked.value = songId
    }

}