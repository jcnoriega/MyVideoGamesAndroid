package com.example.myvideogames.ui.mediaplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
            ) { }

        })
    }

}