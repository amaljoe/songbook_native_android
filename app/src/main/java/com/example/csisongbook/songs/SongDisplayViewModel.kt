package com.example.csisongbook.songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.csisongbook.Song
import com.example.csisongbook.SongDatabaseDao

class SongDisplayViewModel(
    database: SongDatabaseDao,
    application: Application
) : AndroidViewModel(application) {
    val songs=database.getAllSongs()
    val currentSong = MutableLiveData<Song>()


    fun songChanged(songnum: Int){
        currentSong.value = songs.value?.get(songnum)
    }
}