package com.example.rickandmorty.domain.usecase.character

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.PaginatedResult
import com.example.rickandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

class SearchCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke (name: String, page: Int = 1) :  Result<PaginatedResult<Character>> {
        return repository.searchCharacters(name = name.trim(), page = page)
    } 
}