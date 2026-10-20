package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.mappers.toDomain
import com.example.rickandmorty.data.remote.api.RickAndMortyApi
import com.example.rickandmorty.domain.model.Episode
import com.example.rickandmorty.domain.model.PaginatedResult
import com.example.rickandmorty.domain.repository.EpisodeRepository
import javax.inject.Inject


class EpisodeRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi
) : EpisodeRepository{
    override suspend fun getEpisodes(page: Int): Result<PaginatedResult<Episode>> {
        return runCatching { 
            api.getEpisodes(page = page).toDomain(
                page = page
            ) { episodeDto -> 
                episodeDto.toDomain()
            }
        }
    }


}