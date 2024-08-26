package com.artveflina.musicplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder

class MusicService : Service() {
    inner class MusicBinder : Binder() {
        fun getService() = this@MusicService
    }

    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicBinder()
    override fun onBind(intent: Intent?): Binder = binder

    override fun onDestroy() {
        stop()
        super.onDestroy()
    }

    fun play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.fiction_blue)
            mediaPlayer?.isLooping = true
        }
        mediaPlayer?.start()
    }

    fun pause() {
        if (mediaPlayer?.isPlaying == true)
            mediaPlayer?.pause()
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun getDuration() = mediaPlayer?.duration ?: 0

    fun getCurrentPosition() = mediaPlayer?.currentPosition ?: 0

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }
}
