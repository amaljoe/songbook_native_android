package com.example.csisongbook.songs

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.csisongbook.R
import com.example.csisongbook.SongDatabase
import com.example.csisongbook.databinding.ActivitySongDisplayBinding

class SongDisplayActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val application = requireNotNull(this).application
        val dataSource = SongDatabase.getInstance(application).songDatabaseDao
        val songDisplayViewModelFactory = SongDisplayViewModelFactory(dataSource, application)
        val toolbarExtAnim = (AnimatorInflater.loadAnimator(this, R.animator.slide_in_top) as AnimatorSet).apply {
            setTarget(binding.toolbarExtension)
        }
        val toolbarAnim = (AnimatorInflater.loadAnimator(this, R.animator.slide_in_top) as AnimatorSet).apply {
            setTarget(binding.toolbarSong)
        }
        val songDisplayAnim = (AnimatorInflater.loadAnimator(this, R.animator.slide_in_right) as AnimatorSet).apply {
            setTarget(binding.songDisplayPager)
        }
        AnimatorSet().apply {
            play(toolbarExtAnim).with(toolbarAnim).with(songDisplayAnim)
            start()
        }
        val songDisplayViewModel = ViewModelProvider(this, songDisplayViewModelFactory)
            .get(SongDisplayViewModel::class.java)
        binding.lifecycleOwner = this
        binding.songDisplayData = songDisplayViewModel

        val songNum = intent.getIntExtra("songSelected", 0).minus(251)
        Log.i("SongPassed", songNum.toString())

        val adapter = SongDisplayAdapter()
        binding.songDisplayPager.adapter = adapter
        binding.songDisplayPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                songDisplayViewModel.songChanged(position)
            }

        })
        songDisplayViewModel.currentSong.observe(this, Observer {
            it?.let {
                binding.songNum.text = it.songId.toString()
                binding.songTitle.text = it.title
            }
        })
        songDisplayViewModel.songs.observe(this, Observer { it ->
            it?.let {
                adapter.submitList(it)
                binding.songDisplayPager.setCurrentItem(songNum, false)
            }
        })

    }

}
