package com.example.myvideogames.ui.gamedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myvideogames.data.GamesRepository
import com.example.myvideogames.data.model.Game
import com.example.myvideogames.data.model.GameDetail
import com.example.myvideogames.data.model.GameTrailer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    private val gamesRepository: GamesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val game: Game? = savedStateHandle["game"]

    private val _gameDetail = MutableLiveData<GameDetail>()
    val gameDetail: LiveData<GameDetail> = _gameDetail

    private val _gameTrailers = MutableLiveData<List<GameTrailer>>()
    val gameTrailers: LiveData<List<GameTrailer>> = _gameTrailers

    init {
        game?.also {
            viewModelScope.launch {
                val gameDetailsDeferred = async { gamesRepository.getGameDetails(it.id) }
                val gameTrailersDeferred = async { gamesRepository.getGameTrailers(it.id) }

                _gameDetail.value = gameDetailsDeferred.await()
                _gameTrailers.value = gameTrailersDeferred.await()
            }
        }

    }

}