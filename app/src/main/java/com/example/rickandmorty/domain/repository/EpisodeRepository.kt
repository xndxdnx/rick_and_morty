package com.example.rickandmorty.domain.repository

import com.example.rickandmorty.domain.model.Episode
import com.example.rickandmorty.domain.model.PaginatedResult

interface EpisodeRepository {
    
    suspend fun getEpisodes (page: Int) : Result<PaginatedResult<Episode>> 
    
}