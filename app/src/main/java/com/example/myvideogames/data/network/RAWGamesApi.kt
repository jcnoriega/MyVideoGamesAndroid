package com.example.myvideogames.data.network

import com.example.myvideogames.data.model.Game
import com.example.myvideogames.data.model.GameDetail
import com.example.myvideogames.data.model.GameTrailer
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RAWGamesApi {

    @GET(value = "games")
    suspend fun getGames(@Query("ordering") order: String? = null): PagedResponse<Game>

    @GET(value = "games/{id}")
    suspend fun getGameDetails(@Path("id") id: String): GameDetail

    @GET(value = "games/{id}/movies")
    suspend fun getGameTrailers(@Path("id") id: String): PagedResponse<GameTrailer>
}