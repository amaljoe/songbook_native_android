package com.example.csisongbook.songs

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.csisongbook.SongDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class SongListViewModel(
    val database: SongDatabaseDao,
    application: Application
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val songs=database.getAllSongs()
    val itemClicked = MutableLiveData<Int>()

    fun onItemClick(songId: Int) {
        itemClicked.value = songId
    }

}