package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.local.dao.FavoriteDao
import com.example.rickandmorty.data.mappers.toDomain
import com.example.rickandmorty.data.mappers.toFavoriteEntity
import com.example.rickandmorty.data.remote.api.RickAndMortyApi
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao
) : FavoriteRepository {
    override suspend fun addToFavorite(character: Character) {
        dao.insertFavorite(
            item = character.toFavoriteEntity()
        )
    }

    override suspend fun removeFromFavorite(characterId: Int) {
        dao.deleteFromFavorites(
            id = characterId
        )
    }

    override fun observeFavorites(): Flow<List<Character>> {
        return dao.observeFavorites().map { characterEntities -> 
            characterEntities.map { characterEntity -> 
                characterEntity.toDomain()
            }
        }
    }

    override suspend fun isFavorite(characterId: Int): Boolean {
        return dao.isFavorite(id = characterId)
    }

    override suspend fun toggleFavorite(character: Character): Boolean {
        val isCurrentFavorite = dao.isFavorite(id = character.id)
        if (isCurrentFavorite) {
            dao.deleteFromFavorites(character.id)
            return false
        }else{
            dao.insertFavorite(item = character.toFavoriteEntity())
            return true
        }
        
    }


}