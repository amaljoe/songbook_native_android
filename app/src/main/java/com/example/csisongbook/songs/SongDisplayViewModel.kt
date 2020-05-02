package com.example.csisongbook.songs

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.csisongbook.Song
import com.example.csisongbook.SongDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class SongDisplayViewModel(
    val database: SongDatabaseDao,
    application: Application
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val songs=database.getAllSongs()
    val currentSong = MutableLiveData<Song>()

    fun onItemClick(songId: Int) {
        Log.i("songClicked", songId.toString())
    }

    fun songChanged(songnum: Int){
        currentSong.value = songs.value?.get(songnum)
    }
}