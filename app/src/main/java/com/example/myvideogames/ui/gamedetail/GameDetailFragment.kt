package com.example.myvideogames.ui.gamedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.airbnb.epoxy.addGlidePreloader
import com.airbnb.epoxy.glidePreloader
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myvideogames.databinding.FragmentGameDetailBinding
import com.example.myvideogames.ui.SimpleGameItem_
import com.example.myvideogames.ui.helpers.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameDetailFragment : Fragment() {

    private val viewModel: GameDetailViewModel by viewModels()
    private val args: GameDetailFragmentArgs by navArgs()
    private val controller by lazy { GameDetailsEpoxyController() }

    private var _binding: FragmentGameDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        binding.name = args.game.name
        Glide.with(requireContext())
            .loadImage(args.game.backgroundImage, true)
            .into(binding.
            gameDetailLogo)

        val recyclerView = binding.gameDetailRecyclerView
        recyclerView.setController(controller)
        controller.setData(emptyList())
        recyclerView.addGlidePreloader(
            requestManager = Glide.with(requireContext()),
            preloader = glidePreloader { requestManager: RequestManager, model: SimpleGameItem_, _ ->
                val options = RequestOptions
                    .diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC)
                    .dontAnimate()
                requestManager.asBitmap().apply(options).load(model.imageUrl)
            }
        )

        controller.onGameSelected = {
            viewModel.gameTrailerSelected(it)
        }

        viewModel.gameTrailers.observe(viewLifecycleOwner) {
            controller.setData(it)
        }

        return binding.root
    }
}