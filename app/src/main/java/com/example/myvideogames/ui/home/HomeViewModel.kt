package com.example.myvideogames.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myvideogames.data.GamesRepository
import com.example.myvideogames.data.model.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gamesRepository: GamesRepository
): ViewModel() {

    private val _bestGame = MutableLiveData<Game>()
    val bestGame: LiveData<Game> = _bestGame

    private val _feed = MutableLiveData<UiHomeFeed>()
    val feed: LiveData<UiHomeFeed> = _feed

    init {
        viewModelScope.launch {
            val topGamesDeferred = async { gamesRepository.getTopGames() }
            val latestGamesDeferred = async { gamesRepository.getLatestGames() }

            val topGames = topGamesDeferred.await()
            val latestGames = latestGamesDeferred.await()

            _bestGame.value = topGames[0]
            _feed.value = UiHomeFeed(
                topGames.drop(1),
                latestGames
            )
        }
    }
}