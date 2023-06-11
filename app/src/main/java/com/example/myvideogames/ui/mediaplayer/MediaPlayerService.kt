package com.example.myvideogames.ui.mediaplayer

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class MediaPlayerService : Service() {

    companion object {
        const val MEDIA_URI = "media_uri"
    }

    private lateinit var player: ExoPlayer

    override fun onCreate() {
        super.onCreate()
        initializePlayer()
    }

    override fun onBind(intent: Intent?): IBinder {
        intent?.let {
            player.playWhenReady = true
            it.getStringExtra(MEDIA_URI)?.let { uri -> loadMedia(uri) }
        }

        return MediaPlayerBinder()
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build()
    }

    private fun loadMedia(uri: String) {
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    inner class MediaPlayerBinder : Binder() {

        fun getExoPlayer() = player
    }
}