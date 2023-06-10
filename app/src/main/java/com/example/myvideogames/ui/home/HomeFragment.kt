package com.example.myvideogames.ui.home

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.addGlidePreloader
import com.airbnb.epoxy.glidePreloader
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myvideogames.databinding.FragmentHomeBinding
import com.example.myvideogames.ui.SimpleGameItem_
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        const val TAG = "HomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()
    private val controller by lazy { HomeFeedEpoxyController() }


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setUpHeaderView()

        val recyclerView = binding.homeEpoxyRecyclerView
        recyclerView.setController(controller)
        controller.setData(UiHomeFeed())
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
            val directions = HomeFragmentDirections.actionNavigationHomeToGameDetailFragment(it)
            findNavController().navigate(directions)
        }

        viewModel.bestGame.observe(viewLifecycleOwner) { bestGame ->
            Glide.with(requireContext()).load(bestGame.backgroundImage).into(binding.headerImage)
            binding.title.text = bestGame.name
        }
        viewModel.feed.observe(viewLifecycleOwner) { feed ->
            controller.setData(feed)
        }

        return binding.root
    }

    private fun setUpHeaderView() {
        binding.scrollView.setOnTouchListener { _, _ -> true }
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val displayMetrics = DisplayMetrics()
                requireActivity().windowManager.defaultDisplay.getRealMetrics(displayMetrics)
                val widthScreen = displayMetrics.widthPixels
                val widthScroll = binding.headerImage.width / 2
                binding.scrollView.scrollTo(widthScroll - (widthScreen / 2), 0)
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        binding.appBar.addOnOffsetChangedListener { _, verticalOffset ->
            //TODO: check 175 value
            val minimumHeight = convertDpToPixel(175f) + binding.toolbar.height
            val scrollRange = convertDpToPixel(350f) //TODO: check maximum height
            val deltaY = 1 - (abs(verticalOffset).toFloat()) / scrollRange
            val translateY = -(verticalOffset).toFloat() * 0.5f
            if (scrollRange + verticalOffset >= minimumHeight.toInt()) {
                binding.headerImage.scaleY = deltaY
                binding.headerImage.scaleX = deltaY
                binding.headerImage.translationY = translateY
            } else {
                binding.headerImage.scaleY = minimumHeight / scrollRange
            }
        }
    }

    private fun convertDpToPixel(dp: Float): Float {
        return dp * (requireContext().resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}