package com.example.myvideogames

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myvideogames.data.GameTrailer


class MainViewModel: ViewModel() {

    private val _collapsed = MutableLiveData(false)
    var collapsed: LiveData<Boolean> = _collapsed

    private val _shouldCollapsed = MutableLiveData<Boolean>()
    var shouldCollapse: LiveData<Boolean> = _shouldCollapsed

    private val _transitionProgress = MutableLiveData<Float>()
    var transitionProgress: LiveData<Float> = _transitionProgress


    fun transitionCompleted(isCollapsed: Boolean) {
        _collapsed.value = isCollapsed
        Log.e("MainViewModel", "Is collapsed: $isCollapsed")
    }

    fun setProgress(progress: Float) {
        _transitionProgress.value = progress
    }

    fun setMediaSource(gameTrailer: GameTrailer) {
        _shouldCollapsed.value = false
    }

    fun onBackPressed() {
        if (_collapsed.value == false) {
            _shouldCollapsed.value = true
        }
    }
}