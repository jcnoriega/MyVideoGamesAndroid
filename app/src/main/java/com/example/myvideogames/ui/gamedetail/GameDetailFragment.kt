package com.example.myvideogames.ui.gamedetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myvideogames.databinding.FragmentGameDetailBinding

class GameDetailFragment : Fragment() {

    companion object {
        fun newInstance() = GameDetailFragment()
    }

    private val viewModel: GameDetailViewModel by viewModels()
    private val args: GameDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        binding.name = args.game.name
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(requireContext())
            .load(args.game.backgroundImage)
            .apply(requestOptions)
            .into(binding.gameDetailLogo)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.gameDetail.observe(viewLifecycleOwner) {
            binding.description = it.description
        }
        return binding.root
    }

}