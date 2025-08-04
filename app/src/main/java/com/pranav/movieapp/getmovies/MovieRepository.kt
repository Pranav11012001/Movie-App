package com.pranav.movieapp.getmovies

import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: MovieApiService
) {
    suspend fun getHomeScreenMovies() = apiService.getHomeScreenMovies()
}