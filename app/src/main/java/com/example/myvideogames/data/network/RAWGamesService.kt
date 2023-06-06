package com.example.myvideogames.data.network

import com.example.myvideogames.data.Game
import com.example.myvideogames.data.GameDetail
import com.example.myvideogames.data.GameTrailer
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RAWGamesService {

    @GET(value = "games")
    suspend fun getGames(@Query("ordering") order: String? = null): PagedResponse<Game>

    @GET(value = "games/{id}")
    suspend fun getGameDetails(@Path("id") id: String): GameDetail

    @GET(value = "games/{id}/movies")
    suspend fun getGameTrailers(@Path("id") id: String): PagedResponse<GameTrailer>


    companion object {
        private const val API_KEY = "33684e37ce134b6a8015bdb047c27c6c"
        private const val BASE_URL = "https://api.rawg.io/api/"

        fun create(): RAWGamesService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(ApiKeyInterceptor(API_KEY))
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .create()
                    )
                )
                .build()
                .create(RAWGamesService::class.java)
        }
    }
}