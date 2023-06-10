package com.example.myvideogames.ui.mediaplayer

import android.os.Bundle
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

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var fragmentMediaPlayerBinding: FragmentMediaPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMediaPlayerBinding = FragmentMediaPlayerBinding.inflate(inflater, container, false)

        setTransitionListener()
        val player = ExoPlayer.Builder(requireContext()).build()
        fragmentMediaPlayerBinding.playerView.player = player
        viewModel.currentGameTrailer.observe(viewLifecycleOwner) {
            it?.let { trailer ->
                fragmentMediaPlayerBinding.audioNameTextView.text = trailer.name
                fragmentMediaPlayerBinding.audioNameTextViewMin.text = trailer.name

                trailer.data.small?.let { uri ->
                    val mediaItem = MediaItem.fromUri(uri)
                    player.setMediaItem(mediaItem)
                    player.prepare()
                    player.play()
                }

            }
        }

        viewModel.playVideo.observe(viewLifecycleOwner) {
            player.stop()
        }

        fragmentMediaPlayerBinding.cancelButton.setOnClickListener { viewModel.cancelButtonPressed() }


        return fragmentMediaPlayerBinding.root
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