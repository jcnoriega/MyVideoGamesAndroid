package com.example.myvideogames.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myvideogames.data.GamesRepository
import com.example.myvideogames.data.model.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gamesRepository: GamesRepository
): ViewModel() {

    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> = _games

    init {
        viewModelScope.launch {
            _games.value = gamesRepository.getGames()
        }
    }
}