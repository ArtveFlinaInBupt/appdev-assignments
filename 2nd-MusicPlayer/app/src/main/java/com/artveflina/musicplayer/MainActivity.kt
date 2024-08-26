package com.artveflina.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("DefaultLocale")
private fun positionToTimeStr(position: Int, canBeEmpty: Boolean): String {
    if (position == 0 && canBeEmpty) return "--:--"

    val seconds = position / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
}

class MainActivity : AppCompatActivity() {
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    private lateinit var exitButton: Button
    private lateinit var seekBar: SeekBar
    private lateinit var progressTextView: TextView
    private lateinit var statusTextView: TextView

    private var musicService: MusicService? = null
    private var isBound = false

    private var seekBarHandler = Handler(Looper.getMainLooper())

    private val connection = object : ServiceConnection {
        @SuppressLint("SetTextI18n")
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true
            constantlyUpdateSeekBar()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    private val updateSeekBarTask = Runnable { constantlyUpdateSeekBar() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        stopButton = findViewById(R.id.stopButton)
        exitButton = findViewById(R.id.exitButton)
        seekBar = findViewById(R.id.seekBar)
        progressTextView = findViewById(R.id.progressTextView)
        statusTextView = findViewById(R.id.statusTextView)

        playButton.setOnClickListener {
            musicService?.play()
            statusTextView.text = "正在播放"
        }

        pauseButton.setOnClickListener {
            musicService?.pause()
            statusTextView.text = "已暂停播放"
        }

        stopButton.setOnClickListener {
            musicService?.stop()
            statusTextView.text = "已停止播放"
        }

        exitButton.setOnClickListener {
            unbindService(connection)
            finish()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicService?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                seekBarHandler.removeCallbacks(updateSeekBarTask)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                constantlyUpdateSeekBar()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        Intent(this, MusicService::class.java).also {
            bindService(it, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
        seekBarHandler.removeCallbacks(updateSeekBarTask)
    }

    @SuppressLint("SetTextI18n")
    private fun instantlyUpdateSeekBar() {
        seekBar.max = musicService?.getDuration() ?: 0
        seekBar.progress = musicService?.getCurrentPosition() ?: 0
        val totalText = positionToTimeStr(seekBar.max, true)
        val currentText = positionToTimeStr(seekBar.progress, totalText == "--:--")
        progressTextView.text = "$currentText / $totalText"
    }

    private fun constantlyUpdateSeekBar() {
        instantlyUpdateSeekBar()
        seekBarHandler.postDelayed(updateSeekBarTask, 1000)
    }
}
