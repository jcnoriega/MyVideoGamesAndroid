package com.example.myvideogames.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myvideogames.data.Game
import com.example.myvideogames.data.network.RAWGamesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val gamesService: RAWGamesService = RAWGamesService.create()
    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> = _games

    init {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    gamesService.getGames("released")
                }
                _games.value = result.results
            } catch (e: Exception) {
                print(e)
            }

        }
    }
}