package com.example.rickandmorty.domain.usecase.favorite

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(): Flow<List<Character>> {
        return repository.observeFavorites()
    }
}