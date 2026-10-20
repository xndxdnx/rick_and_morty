package com.example.rickandmorty.domain.usecase.favorite

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.FavoriteRepository
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(characterId: Int): Boolean {
        return repository.isFavorite(characterId = characterId)
    }
}