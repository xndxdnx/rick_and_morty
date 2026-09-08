package com.example.rickandmorty.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.rickandmorty.data.local.dao.FavoriteDao
import com.example.rickandmorty.data.local.database.RickAndMortyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        app: Context
    ) : RickAndMortyDatabase {
        return Room.databaseBuilder(
            name = "rick_and_morty_database",
            klass = RickAndMortyDatabase::class.java,
            context = app
        ).build()
    }
    
    @Provides
    fun provideFavoriteDao(database: RickAndMortyDatabase) : FavoriteDao {
        return database.favoriteDao()
    }
    
}