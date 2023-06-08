package com.example.myvideogames

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myvideogames.data.GamesRepository
import com.example.myvideogames.data.model.GameTrailer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val gamesRepository: GamesRepository
) : ViewModel() {

    private val _collapsed = MutableLiveData<Boolean>()
    var collapsed: LiveData<Boolean> = _collapsed

    private val _shouldCollapsed = MutableLiveData<Boolean>()
    var shouldCollapse: LiveData<Boolean> = _shouldCollapsed

    private val _transitionProgress = MutableLiveData<Float>()
    var transitionProgress: LiveData<Float> = _transitionProgress

    private val _currentGameTrailer = MutableLiveData<GameTrailer?>(null)
    var currentGameTrailer: LiveData<GameTrailer?> = _currentGameTrailer

    init {
        viewModelScope.launch {
            gamesRepository.currentGameTrailer.collect {
                _currentGameTrailer.value = it
            }
        }
    }

    fun transitionCompleted(isCollapsed: Boolean) {
        _collapsed.value = isCollapsed
    }

    fun setProgress(progress: Float) {
        _transitionProgress.value = progress
    }

    fun setMediaSource(gameTrailer: GameTrailer) {
        _shouldCollapsed.value = false
        gamesRepository.selectGameTrailer(gameTrailer)
    }

    fun onBackPressed() {
        if (_collapsed.value == false) {
            _shouldCollapsed.value = true
        }
    }
}