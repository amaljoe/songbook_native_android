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

    private lateinit var binding: SongListFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_song_list, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = SongDatabase.getInstance(application).songDatabaseDao
        val songViewModelFactory = SongListViewModelFactory(dataSource, application)
        val viewModel = ViewModelProviders.of(this, songViewModelFactory).get(SongListViewModel::class.java)
        binding.lifecycleOwner = this
        binding.songdata = viewModel
        return binding.root
    }
}

data class MyName(var name: String = "", var nickname: String = "")