package com.example.rickandmorty.domain.repository

import com.example.rickandmorty.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun addToFavorite(character: Character)

    suspend fun removeFromFavorite(characterId: Int)

    fun observeFavorites(): Flow<List<Character>>

    suspend fun isFavorite(characterId: Int): Boolean

    suspend fun toggleFavorite(character: Character): Boolean

}