package com.example.csisongbook.songs

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.csisongbook.R
import com.example.csisongbook.SongDatabase
import com.example.csisongbook.databinding.ActivitySongDisplayBinding
import kotlinx.android.synthetic.main.song_toolbar.view.*

class SongDisplayActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val application = requireNotNull(this).application
        val dataSource = SongDatabase.getInstance(application).songDatabaseDao
        val songDisplayViewModelFactory = SongDisplayViewModelFactory(dataSource, application)
        @RequiresApi(Build.VERSION_CODES.O)
        binding.toolbarSong.searchEdit.focusable = View.NOT_FOCUSABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
        binding.toolbarSong.searchEdit.setOnClickListener {
            val intent = Intent(this, SearchSongActivity::class.java)
            startActivity(intent)
        }
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
