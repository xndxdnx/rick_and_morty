package com.example.rickandmorty.domain.usecase.episode

import com.example.rickandmorty.domain.model.Episode
import com.example.rickandmorty.domain.model.PaginatedResult
import com.example.rickandmorty.domain.repository.EpisodeRepository
import javax.inject.Inject

class GetEpisodesUseCase @Inject constructor(
    private val repository: EpisodeRepository
) {
    suspend operator fun invoke (page: Int = 1): Result<PaginatedResult<Episode>> {
        return repository.getEpisodes(page = page)
    }

}