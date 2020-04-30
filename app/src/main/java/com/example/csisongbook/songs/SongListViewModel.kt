package com.example.csisongbook.songs

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.csisongbook.SongDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class SongListViewModel(
    val database: SongDatabaseDao,
    application: Application
) : AndroidViewModel(application) {
    fun onItemClick(songId: Int) {
        Log.i("songClicked",songId.toString())
    }

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val songs=database.getAllSongs()

}