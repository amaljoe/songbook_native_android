package com.example.csisongbook.songs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.csisongbook.R
import com.example.csisongbook.SongDatabase
import com.example.csisongbook.databinding.FragmentSongListBinding


class SongListFragment : Fragment() {

    private lateinit var binding: FragmentSongListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_song_list, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = SongDatabase.getInstance(application).songDatabaseDao
        val songListViewModelFactory = SongListViewModelFactory(dataSource, application)
        @Suppress("DEPRECATION")
        val songListViewModel = ViewModelProviders.of(this, songListViewModelFactory).get(SongListViewModel::class.java)
        binding.lifecycleOwner = this
        binding.songdata = songListViewModel

        val adapter = SongListAdapter(SongListListener { songId ->
            songListViewModel.onItemClick(songId)
        })
        val layoutManager = LinearLayoutManager(this.activity)
        binding.songListContent.layoutManager = layoutManager
        binding.songListContent.adapter = adapter
        songListViewModel.songs.observe(viewLifecycleOwner, Observer {it->
            it?.let {
                adapter.submitList(it)
            }
        })
        return binding.root
    }
}