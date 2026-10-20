package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.mappers.toDomain
import com.example.rickandmorty.data.remote.api.RickAndMortyApi
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.PaginatedResult
import com.example.rickandmorty.domain.repository.CharacterRepository
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.collections.emptyList


class CharacterRepositoryImpl @Inject constructor (
    private val api: RickAndMortyApi
) : CharacterRepository {
    override suspend fun getCharacters(page: Int): Result<PaginatedResult<Character>> {
         return runCatching {
             api.getCharacters(page = page).toDomain(
                 page = page
             ) { characterDto -> 
                 characterDto.toDomain()
             } 
             
         } 
             .recoverCatching { error ->
             if (error is HttpException && error.code() == 404) {
                 PaginatedResult(
                     items = emptyList(),
                     currentPage = page,
                     totalPages = 0,
                     hasNextPage = false
                 )
             }else {
                 throw error
             }
         }
    }

    override suspend fun getCharacterById(id: Int): Result<Character> {
       return runCatching { 
           api.getCharacterById(id = id).toDomain()
       }
    }

    override suspend fun searchCharacters(
        name: String,
        page: Int
    ): Result<PaginatedResult<Character>> {
       return runCatching { 
           api.searchCharacters(
               name = name,
               page = page
           ).toDomain(
               page = page
           ) { characterDto -> 
               characterDto.toDomain()
           }
       }
           .recoverCatching { error ->
           if (error is HttpException && error.code() == 404) {
               PaginatedResult(
                   items = emptyList(),
                   currentPage = page,
                   totalPages = 0,
                   hasNextPage = false
               )
           }else{
               throw error
           }
       }
    }


}