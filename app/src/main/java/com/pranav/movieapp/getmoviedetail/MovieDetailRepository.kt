package com.pranav.movieapp.getmoviedetail

import com.google.gson.Gson
import com.pranav.movieapp.getmovies.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject


class MovieDetailRepository @Inject constructor(
    private val movieDetailApiService: MovieDetailApiService
) {
    suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return movieDetailApiService.getMovieDetails(movieId)
    }
}
