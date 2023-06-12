package com.example.myvideogames.data

import com.example.myvideogames.data.model.GameTrailer
import com.example.myvideogames.data.network.RAWGamesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamesRepository @Inject constructor(
    private val gamesService: RAWGamesApi
) {

    // We could put this in a Dao
    private val _currentGameTrailer = MutableStateFlow<GameTrailer?>(null)
    val currentGameTrailer: StateFlow<GameTrailer?> get() = _currentGameTrailer

    suspend fun getTopGames() = withContext(Dispatchers.IO) {
        val pagedResult = gamesService.getGames()
        pagedResult.results
    }

    suspend fun getLatestGames() = withContext(Dispatchers.IO) {
        val pagedResult = gamesService.getGames("released")
        pagedResult.results
    }

    suspend fun getGameAdditions(id: String) = withContext(Dispatchers.IO) {
        gamesService.getGameAdditions(id).results
    }

    suspend fun getGameDetails(id: String) = withContext(Dispatchers.IO) {
        gamesService.getGameDetails(id)
    }

    fun selectGameTrailer(gameTrailer: GameTrailer) {
        _currentGameTrailer.value = gameTrailer
    }

    fun removeCurrentGameTrailer() {
        _currentGameTrailer.value = null
    }


    suspend fun getGameTrailers(id: String): List<GameTrailer> {
        return withContext(Dispatchers.IO) {
            gamesService.getGameTrailers(id).results
        }
    }
}