package com.example.rickandmorty.domain.usecase.character

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterByIdUseCase @Inject constructor(
    private val repository: CharacterRepository
) {

    suspend operator fun invoke(id: Int): Result<Character> {
        return repository.getCharacterById(id = id)
    }
    
}