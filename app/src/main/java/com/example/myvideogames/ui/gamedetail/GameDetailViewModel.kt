package com.example.myvideogames.ui.gamedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myvideogames.data.Game
import com.example.myvideogames.data.GameDetail
import com.example.myvideogames.data.GameTrailer
import com.example.myvideogames.data.network.RAWGamesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class GameDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val apiService = RAWGamesService.create() //TODO: inject with hilt

    private val game: Game? = savedStateHandle["game"]

    private val _gameDetail = MutableLiveData<GameDetail>()
    val gameDetail: LiveData<GameDetail> = _gameDetail

    private val _gameTrailers = MutableLiveData<List<GameTrailer>>()
    val gameTrailers: LiveData<List<GameTrailer>> = _gameTrailers

    init {
        game?.also {
            viewModelScope.launch {
                val gameDetailsDeferred = async(Dispatchers.IO) { apiService.getGameDetails(it.id) }
                val gameTrailersDeferred  = async(Dispatchers.IO) { apiService.getGameTrailers(it.id) }

                _gameDetail.value = gameDetailsDeferred.await()
                _gameTrailers.value = gameTrailersDeferred.await().results
            }
        }

    }

}