package com.example.myvideogames.ui.home

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.airbnb.epoxy.EpoxyRecyclerView
import com.example.myvideogames.databinding.FragmentHomeBinding
import com.example.myvideogames.simpleItem
import com.google.android.material.appbar.AppBarLayout
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
        val recyclerView: EpoxyRecyclerView = binding.homeEpoxyRecyclerView

        recyclerView.withModels {
            for (i in 1 until 11) {
                simpleItem {
                    id("item{$i}")
                    text("Number $i")
                }
            }
        }
        binding.scrollView.setOnTouchListener { _, _ ->  true}
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object: OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val displayMetrics = DisplayMetrics()
                requireActivity().windowManager.defaultDisplay.getRealMetrics(displayMetrics)
                val widthScreen = displayMetrics.widthPixels
                val widthScroll = binding.headerImage.width/2
                Log.e(TAG, "screen = $widthScreen, widthScroll = $widthScroll")
                binding.scrollView.scrollTo(widthScroll - (widthScreen/2), 0)
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        binding.appBar.addOnOffsetChangedListener { _, verticalOffset ->
            val minimumHeight = convertDpToPixel(175f) + binding.toolbar.height
            val scrollRange = binding.headerImage.height
            val delta = 1.0f - abs(verticalOffset).toFloat() / (scrollRange - minimumHeight)
            val scalingFactor = 0.2f + delta * 0.8f
            val scrollOffset = scrollRange + verticalOffset
            val deltaY = 1 - (abs(verticalOffset).toFloat()) / scrollRange
            val translateY = -(verticalOffset).toFloat()*0.5f//+ minimumHeight*0.5f
            Log.e(TAG, "VerticalOffset: $verticalOffset, minimumHeight=$minimumHeight, scrollOffset: $scrollOffset, deltaY:$deltaY, translateY = $translateY")
            if (scrollRange + verticalOffset >= minimumHeight.toInt()) {
                binding.headerImage.scaleY = deltaY
                binding.headerImage.scaleX = deltaY
                binding.headerImage.translationY = translateY//delta * 0.5f * abs(verticalOffset)
            } else {
                binding.headerImage.scaleY = minimumHeight / scrollRange
            }
                //0.2f + delta * 0.8f
            //binding.headerImage.scaleY = 0.2f + delta * 0.8f

        }
        return binding.root
    }

    private fun convertDpToPixel(dp: Float): Float {
        return dp * (requireContext().resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}