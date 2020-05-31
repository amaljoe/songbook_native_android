package com.example.csisongbook.songs

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.csisongbook.R
import com.example.csisongbook.Song
import com.example.csisongbook.SongDatabase
import com.example.csisongbook.SongDatabaseDao
import com.example.csisongbook.databinding.ActivitySearchBinding
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
        searchSongViewModel = ViewModelProvider(this, searchSongViewModelFactory).get(SearchSongViewModel::class.java)
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
                val query = s.toString()
                if (query.isDigitsOnly()) {
                    getNumItemsFromDb(query)
                    if (query.length == 3) {
                        Log.i("item clicked","item $query")
                        searchSongViewModel.onItemClick(query.toInt())
                    }
                }
                else {
                    getItemsFromDb(query)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        searchSongViewModel.itemClicked.observe(this, Observer {
            (AnimatorInflater.loadAnimator(this, R.animator.circle_masking) as AnimatorSet).apply {
                setTarget(binding.circleMask)
                duration = 300
                interpolator = AccelerateInterpolator()
                start()
                binding.circleMask.visibility = View.VISIBLE
                addListener(object: AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        val intent = Intent(this@SearchSongActivity, SongDisplayActivity::class.java)
                        intent.putExtra("songSelected",it)
                        startActivity(intent)
                        finishAndRemoveTask()
                        Handler().postDelayed({
                            binding.circleMask.visibility = View.GONE
                        }, 100)
                    }
                })
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
    private fun getItemsFromDb(searchTxt: String) {
        var searchText = searchTxt
        searchText = "%$searchText%"

        searchForItems(desc = searchText).observe(this, Observer { list ->
            list?.let {
                adapter.submitList(it)
            }

        })

    }

    private fun searchForNumItems(desc: String) : LiveData<List<Song>> {
        return dataSource.getNumSearchResults(desc)
    }
    private fun getNumItemsFromDb(searchTxt: String) {
        var searchText = searchTxt
        searchText = "%$searchText%"

        searchForNumItems(desc = searchText).observe(this, Observer { list ->
            list?.let {
                adapter.submitList(it)
            }

        })

    }
}
