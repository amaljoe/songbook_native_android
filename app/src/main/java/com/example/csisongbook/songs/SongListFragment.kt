package com.example.csisongbook.songs

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.csisongbook.R
import com.example.csisongbook.SongDatabase
import com.example.csisongbook.databinding.FragmentSongListBinding
import kotlinx.android.synthetic.main.song_toolbar.view.*


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
        val songListViewModel = ViewModelProvider(this, songListViewModelFactory).get(SongListViewModel::class.java)
        binding.lifecycleOwner = this
        binding.songdata = songListViewModel
        @RequiresApi(Build.VERSION_CODES.O)
        binding.toolbarSong.searchEdit.focusable = View.NOT_FOCUSABLE
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
        binding.toolbarSong.searchEdit.setOnClickListener {
            val intent = Intent(this.activity, SearchSongActivity::class.java)
            startActivity(intent)
        }
        songListViewModel.itemClicked.observe(viewLifecycleOwner, Observer {
            (AnimatorInflater.loadAnimator(this.activity, R.animator.circle_masking) as AnimatorSet).apply {
                setTarget(binding.circleMask)
                duration = 400
                interpolator = AccelerateInterpolator()
                start()
                binding.circleMask.visibility = View.VISIBLE
                addListener(object: AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        Handler().postDelayed({
                            binding.circleMask.visibility = View.GONE
                        }, 100)
                    }
                })
                Handler().postDelayed({
                    val intent = Intent(context, SongDisplayActivity::class.java)
                    intent.putExtra("songSelected",it)
                    startActivity(intent)
                }, 450)
            }
        })
        return binding.root
    }
}