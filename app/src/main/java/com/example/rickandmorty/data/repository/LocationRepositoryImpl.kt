package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.mappers.toDomain
import com.example.rickandmorty.data.remote.api.RickAndMortyApi
import com.example.rickandmorty.domain.model.Location
import com.example.rickandmorty.domain.model.PaginatedResult
import com.example.rickandmorty.domain.repository.LocationRepository
import javax.inject.Inject


class LocationRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi
) : LocationRepository{
    override suspend fun getLocation(page: Int): Result<PaginatedResult<Location>> {
        return runCatching { 
            api.getLocations(
                page = page
            ).toDomain(
                page = page
            ) { locationDto -> 
                locationDto.toDomain()
            }
        }
    }
}