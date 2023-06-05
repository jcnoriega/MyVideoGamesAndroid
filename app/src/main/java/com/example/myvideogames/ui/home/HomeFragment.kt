package com.example.myvideogames.ui.home

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.airbnb.epoxy.EpoxyRecyclerView
import com.example.myvideogames.databinding.FragmentHomeBinding
import com.example.myvideogames.header
import com.example.myvideogames.simpleItem
import com.example.myvideogames.ui.helpers.carouselBuilder
import kotlin.math.abs


class HomeFragment : Fragment() {

    companion object {
        const val TAG = "HomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()

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

        val recyclerView: EpoxyRecyclerView = binding.homeEpoxyRecyclerView
        recyclerView.withModels {
            header {
                id("header_latest")
                title("Latest Releases")
            }
            carouselBuilder {
                id("carousel_latest")
                for (i in 1 until 11) {
                    simpleItem {
                        id("item{$i}")
                        text("Number $i")
                    }
                }
            }
            header {
                id("header_upcoming")
                title("Upcoming")
            }
            carouselBuilder {
                id("carousel_upcoming")
                for (i in 1 until 11) {
                    simpleItem {
                        id("item{$i}")
                        text("Number $i")
                    }
                }
            }
            header {
                id("top")
                title("Top 10")
            }
            carouselBuilder {
                id("carousel_top")
                for (i in 1 until 11) {
                    simpleItem {
                        id("item{$i}")
                        text("Number $i")
                    }
                }
            }
            header {
                id("favourites_header")
                title("Favourites")
            }
            carouselBuilder {
                id("carousel_favourites")
                for (i in 1 until 11) {
                    simpleItem {
                        id("item{$i}")
                        text("Number $i")
                    }
                }
            }
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
            val scrollRange = binding.headerImage.height
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