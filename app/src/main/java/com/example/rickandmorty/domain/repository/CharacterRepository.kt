package com.example.rickandmorty.domain.repository

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.PaginatedResult

interface CharacterRepository {
    
    suspend fun getCharacters (page: Int): Result<PaginatedResult<Character>>
    
    suspend fun getCharacterById (id: Int) : Result<Character>
    
    suspend fun searchCharacters (name: String, page: Int) : Result<PaginatedResult<Character>>
    
}