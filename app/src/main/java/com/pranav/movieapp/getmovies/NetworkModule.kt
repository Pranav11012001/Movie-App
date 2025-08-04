package com.pranav.movieapp.getmovies

import com.pranav.movieapp.getmoviedetail.MovieDetailApiService
import com.pranav.movieapp.getmovies.MovieApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// core/network/NetworkModule.kt
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmZjI2Yzk2ZWNjMDY3YzFkNWUwMmM3Yzk0YzhiMTYxMSIsIm5iZiI6MTc1Mjk0MTM4Mi4yNjMsInN1YiI6IjY4N2JjMzQ2NjU4NWJkNGVkYmQ5ODcyZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.W7nhSoYdAss_QFNvKdNv-v0B-DxSJAokjtJCFOxzy1U"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", AUTH_TOKEN)
                        .build()
                )
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApiService(retrofit: Retrofit): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDetailApiService(retrofit: Retrofit): MovieDetailApiService {
        return retrofit.create(MovieDetailApiService::class.java)
    }
}