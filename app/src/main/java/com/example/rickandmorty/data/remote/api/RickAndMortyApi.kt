package com.example.rickandmorty.data.remote.api


import com.example.rickandmorty.data.remote.dto.CharacterDto
import com.example.rickandmorty.data.remote.dto.EpisodeDto
import com.example.rickandmorty.data.remote.dto.LocationDto
import com.example.rickandmorty.data.remote.dto.PaginatedResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ) : PaginatedResponseDto<CharacterDto>
    
    @GET("character")
    suspend fun searchCharacters(
        @Query("name") name : String,
        @Query("page") page: Int
    ): PaginatedResponseDto<CharacterDto>
    
    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): CharacterDto
    
    @GET("location")
    suspend fun getLocations(
        @Query("page") page: Int 
    ) : PaginatedResponseDto<LocationDto>
    
    @GET("episode")
    suspend fun getEpisodes(
        @Query("page") page: Int
    ): PaginatedResponseDto<EpisodeDto>
    
}