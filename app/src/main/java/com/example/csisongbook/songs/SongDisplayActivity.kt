package com.example.csisongbook.songs

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.example.csisongbook.SongDatabase
import com.example.csisongbook.databinding.ActivitySongDisplayBinding
import kotlinx.android.synthetic.main.activity_song_display.*

class SongDisplayActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val application = requireNotNull(this).application
        val dataSource = SongDatabase.getInstance(application).songDatabaseDao
        val songDisplayViewModelFactory = SongDisplayViewModelFactory(dataSource, application)

        @Suppress("DEPRECATION")
        val songDisplayViewModel = ViewModelProviders.of(this, songDisplayViewModelFactory)
            .get(SongDisplayViewModel::class.java)
        binding.lifecycleOwner = this
        binding.songDisplayData = songDisplayViewModel

        val songNum = intent.getIntExtra("songSelected", 0).minus(251)
        var hasSong = intent.getBooleanExtra("hasSongSelected", false)
        Log.i("SongPassed", songNum.toString())
        Log.i("hasSong", hasSong.toString())

        val adapter = SongDisplayAdapter(SongDisplayListener { songId ->
            songDisplayViewModel.onItemClick(songId)
        })
        binding.songDisplayPager.setCurrentItem(songNum, false)
        binding.songDisplayPager.adapter = adapter
        Handler().postDelayed({
            binding.songDisplayPager.setCurrentItem(songNum, false)
        }, 100)
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
            }
        })

    }

}
