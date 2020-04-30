package com.example.csisongbook.songs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.example.csisongbook.R
import com.example.csisongbook.SongDatabase


class SongListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<SongListViewModelBinding>(inflater, R.layout.fragment_song_list, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = SongDatabase.getInstance(application).songDatabaseDao
        val songViewModelFactory = SongListViewModelFactory(dataSource, application)
        val viewModel = ViewModelProviders.of(this, songViewModelFactory).get(SongListViewModel::class.java)
        binding.lifecycleOwner = this
        binding.songdata = viewModel
        return binding.root
    }
}
