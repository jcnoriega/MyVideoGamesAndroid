package com.example.myvideogames.data.network

data class PagedResponse<T>(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<T>
)