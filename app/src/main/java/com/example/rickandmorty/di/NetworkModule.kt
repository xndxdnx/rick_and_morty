package com.example.rickandmorty.di

import com.example.rickandmorty.data.remote.api.RickAndMortyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.apply

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"
    
    @Provides
    @Singleton
    fun provideConnectionPool() : ConnectionPool  = OkhttpConfig.apiConnectionPull()
    
    @Provides
    @Singleton
    fun provideOkHttpClient(
        connectionPool: ConnectionPool
    ): OkHttpClient {
        return OkhttpConfig.apiClientBuilder(
            connectionPool = connectionPool
        ).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            
    }

    @Provides
    @Singleton
    fun provideRickAndMortyApi(
        retrofit: Retrofit
    ): RickAndMortyApi {
        return retrofit.create(RickAndMortyApi::class.java)
    }
    

}