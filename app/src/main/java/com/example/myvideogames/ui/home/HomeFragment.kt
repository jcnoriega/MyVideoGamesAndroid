package com.example.myvideogames.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.airbnb.epoxy.EpoxyRecyclerView
import com.example.myvideogames.databinding.FragmentHomeBinding
import com.example.myvideogames.header
import com.example.myvideogames.simpleItem

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: EpoxyRecyclerView = binding.root
        root.withModels {
            header {
                id("header")
                title("Grand Theft Auto V")
                onClick { _ ->
                    Toast.makeText(requireContext(), "Header", Toast.LENGTH_SHORT).show()
                }
            }
            for (i in 1 until 11) {
                simpleItem {
                    id("item{$i}")
                    text("Number $i")
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}