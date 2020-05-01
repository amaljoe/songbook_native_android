package com.example.csisongbook.songs

import android.app.Application
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csisongbook.R
import com.example.csisongbook.Song
import com.example.csisongbook.SongDatabase
import com.example.csisongbook.SongDatabaseDao
import com.example.csisongbook.databinding.ActivitySearchBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_toolbar.view.*


class SearchSongActivity : AppCompatActivity() {

    private lateinit var dataSource: SongDatabaseDao
    private lateinit var searchSongViewModel: SearchSongViewModel
    private lateinit var adapter: SearchSongAdapter
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val application = requireNotNull(this).application
        dataSource = SongDatabase.getInstance(application).songDatabaseDao
        val searchSongViewModelFactory = SearchSongViewModelFactory(dataSource, application)
        @Suppress("DEPRECATION")
        searchSongViewModel = ViewModelProviders.of(this, searchSongViewModelFactory).get(SearchSongViewModel::class.java)
        binding.lifecycleOwner = this
        binding.searchData =searchSongViewModel

        adapter = SearchSongAdapter(SearchSongListener { songId ->
            searchSongViewModel.onItemClick(songId)
        })
        val layoutManager = LinearLayoutManager(this)
        binding.searchSongList.layoutManager = layoutManager
        binding.searchSongList.adapter = adapter
        Log.i("working","search activity started")
        binding.toolbarSong.searchEdit.requestFocus()
        binding.toolbarSong.searchEdit.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                getItemsFromDb(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    override fun onPause() {
        super.onPause()
        Log.i("working","search activity over")
    }

    private fun searchForItems(desc: String) : LiveData<List<Song>> {
        return dataSource.getSearchResults(desc)
    }
    private fun getItemsFromDb(searchText: String) {
        var searchText = searchText
        searchText = "%$searchText%"

        searchForItems(desc = searchText).observe(this, Observer { list ->
            list?.let {
                adapter.submitList(it)
            }

        })

    }
}
