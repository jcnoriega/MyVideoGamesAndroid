package com.example.myvideogames.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.myvideogames.R
import com.example.myvideogames.databinding.FragmentMediaPlayerBinding

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

    lateinit var fragmentMediaPlayerBinding: FragmentMediaPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMediaPlayerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_media_player, container, false)
        return fragmentMediaPlayerBinding.root
    }
}