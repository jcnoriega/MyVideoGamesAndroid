package com.example.myvideogames.ui.mediaplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.myvideogames.MainViewModel
import com.example.myvideogames.R
import com.example.myvideogames.data.model.GameTrailer
import com.example.myvideogames.databinding.FragmentMediaPlayerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaPlayerFragment : Fragment() {
    companion object {
        const val TAG = "MediaPlayerFragment"
        fun newInstance(): MediaPlayerFragment {
            val args = Bundle()
            val playScreenFragment = MediaPlayerFragment()
            playScreenFragment.arguments = args
            return playScreenFragment
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service is MediaPlayerService.MediaPlayerBinder) {
                fragmentMediaPlayerBinding.playerView.player = service.getExoPlayer()
            }
        }
    }

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var fragmentMediaPlayerBinding: FragmentMediaPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMediaPlayerBinding = FragmentMediaPlayerBinding.inflate(inflater, container, false)

        setTransitionListener()
        viewModel.currentGameTrailer.observe(viewLifecycleOwner) {
            it?.let { trailer ->
                fragmentMediaPlayerBinding.audioNameTextView.text = trailer.name
                fragmentMediaPlayerBinding.audioNameTextViewMin.text = trailer.name
                startMediaPlayerService(trailer)
            }
        }

        fragmentMediaPlayerBinding.cancelButton.setOnClickListener {
            viewModel.cancelButtonPressed()
            requireActivity().unbindService(serviceConnection)
        }

        return fragmentMediaPlayerBinding.root
    }

    private fun startMediaPlayerService(gameTrailer: GameTrailer) {
        gameTrailer.data.small?.let { uri->
            val intent = Intent(requireContext(), MediaPlayerService::class.java)
            intent.putExtra(MediaPlayerService.MEDIA_URI, uri)
            intent.putExtra(MediaPlayerService.MEDIA_IMG, gameTrailer.preview)
            intent.putExtra(MediaPlayerService.MEDIA_TITLE, gameTrailer.name)
            requireActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            requireActivity().startService(intent)
        }
    }

    private fun setTransitionListener() {
        val motionLayout = fragmentMediaPlayerBinding.mediaPlayerMotionLayout
        viewModel.shouldCollapse.observe(viewLifecycleOwner) { shouldCollapse ->
            if (shouldCollapse) {
                motionLayout.transitionToEnd()
            } else {
                motionLayout.transitionToStart()
            }
        }
        motionLayout.setTransitionListener(object : TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                viewModel.setProgress(progress)
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                viewModel.transitionCompleted(currentId == R.id.play_screen_minimized)
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }

        })
    }

}