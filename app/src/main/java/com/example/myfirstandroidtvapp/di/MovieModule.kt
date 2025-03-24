package com.example.myfirstandroidtvapp.di

import com.example.myfirstandroidtvapp.data.remote.MovieApiService
import com.example.myfirstandroidtvapp.data.repository.MovieRepositoryImpl
import com.example.myfirstandroidtvapp.domain.repository.MovieRepository
import com.example.myfirstandroidtvapp.domain.usecase.GetTrendingMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//    private const val BASE_URL = "https://api.themoviedb.org/3/"
//
//    @Provides
//    @Singleton
//    fun provideMovieApiService(retrofit: Retrofit): MovieApiService {
//        return retrofit.create(MovieApiService::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideMovieRepository(apiService: MovieApiService): MovieRepository {
//        return MovieRepositoryImpl(apiService)
//    }
//
//    @Provides
//    @Singleton
//    fun provideGetTrendingMoviesUseCase(repository: MovieRepository): GetTrendingMoviesUseCase {
//        return GetTrendingMoviesUseCase(repository)
//    }
//}