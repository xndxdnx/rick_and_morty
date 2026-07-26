package com.example.rickandmorty.domain.usecase.character

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.PaginatedResult
import com.example.rickandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
   private val repository: CharacterRepository
) {
    
    suspend operator fun invoke(page: Int = 1) : Result<PaginatedResult<Character>> {
        return repository.getCharacters(page = page)
    }
    
}