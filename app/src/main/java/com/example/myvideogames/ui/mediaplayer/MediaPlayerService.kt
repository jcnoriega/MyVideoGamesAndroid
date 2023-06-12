package com.example.myvideogames.ui.mediaplayer

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition


class MediaPlayerService : Service(), Player.Listener {

    companion object {
        const val TAG = "MediaPlayerService"
        const val MEDIA_URI = "media_uri"
        const val MEDIA_IMG = "media_img"
        const val MEDIA_TITLE ="media_title"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_CHANNEL_ID = "MyVideoGames_MediaPlayer"
        const val NOTIFICATION_CHANNEL_NAME = "MyVideoGames_MediaPlayer_Trailers"
    }

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mStateBuilder: PlaybackStateCompat.Builder
    private lateinit var mMetadataBuilder: MediaMetadataCompat.Builder
    private lateinit var notificationManager: NotificationManager

    private var title = ""
    private var imageUri = ""

    override fun onCreate() {
        super.onCreate()
        initializeMediaSession()
        initializePlayer()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onBind(intent: Intent?): IBinder {
        intent?.let { setUpMedia(it) }
        return MediaPlayerBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { setUpMedia(it) }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun setUpMedia(intent: Intent) {
        intent.getStringExtra(MEDIA_URI)?.let { uri -> loadMedia(uri) }
        intent.getStringExtra(MEDIA_TITLE)?.let { title -> this.title = title}
        intent.getStringExtra(MEDIA_IMG)?.let { img ->
            this.imageUri = img
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        if (!player.playWhenReady) {
            notificationManager.cancel(NOTIFICATION_ID)
            stopSelf()
        }
    }

    /**
     * Release ExoPlayer.
     */
    private fun releasePlayer() {
        player.removeListener(this)
        player.stop()
        player.release()
    }

    /**
     * Release the player when the service is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
        mediaSession.isActive = false
    }

    // Player Event Listener Methods
    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        val state =
            if (playWhenReady) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED
        mStateBuilder.setState(state, player.contentPosition, 1f)
        updateNotification()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        updateNotification()
    }

    private fun updateNotification() {
        val playbackStateCompat = mStateBuilder.build()
        mMetadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, player.duration)
        mediaSession.setMetadata(mMetadataBuilder.build());
        mediaSession.setPlaybackState(playbackStateCompat)
        showNotification(playbackStateCompat)
    }

    private fun showNotification(state: PlaybackStateCompat) {
        val builder =
            NotificationCompat.Builder(this, "CHANNEL ID")
        val icon: Int
        val playPause: String
        if (state.state == PlaybackStateCompat.STATE_PLAYING) {
            icon = R.drawable.ic_media_play
            playPause = "Pause"
        } else {
            icon = R.drawable.ic_media_pause
            playPause = "Play"
        }
        val playPauseAction = NotificationCompat.Action(
            icon, playPause,
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                this,
                PlaybackStateCompat.ACTION_PLAY_PAUSE
            )
        )

        builder.setContentTitle(title)
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .addAction(playPauseAction)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1)
                .setMediaSession(mediaSession.sessionToken))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = NOTIFICATION_CHANNEL_ID
            val channel = NotificationChannel(
                channelId,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
            builder.setChannelId(channelId)
        }

        Glide.with(applicationContext)
            .asBitmap()
            .load(imageUri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    builder.setLargeIcon(resource)
                    val notification = builder.build()
                    startForeground(NOTIFICATION_ID, builder.build())
                    notificationManager.notify(NOTIFICATION_ID, notification)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build()
        player.addListener(this)
    }

    private fun initializeMediaSession() {
        mediaSession = MediaSessionCompat(this, TAG)
        mediaSession.setMediaButtonReceiver(null)
        mStateBuilder = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                        PlaybackStateCompat.ACTION_PAUSE or
                        PlaybackStateCompat.ACTION_SEEK_TO
            )

        mediaSession.setPlaybackState(mStateBuilder.build())
        mMetadataBuilder = MediaMetadataCompat.Builder()
        mediaSession.setCallback(MySessionCallback())
        mediaSession.isActive = true
    }

    private fun loadMedia(uri: String) {
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    inner class MediaPlayerBinder : Binder() {

        fun getExoPlayer() = player
    }

    inner class MySessionCallback : MediaSessionCompat.Callback() {
        override fun onPlay() {
            player.playWhenReady = true
        }

        override fun onPause() {
            player.playWhenReady = false
        }

        override fun onSeekTo(pos: Long) {
            player.seekTo(pos)
        }
    }
}