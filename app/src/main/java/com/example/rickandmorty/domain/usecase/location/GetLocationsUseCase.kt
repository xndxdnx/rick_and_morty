package com.example.rickandmorty.domain.usecase.location

import com.example.rickandmorty.domain.model.Location
import com.example.rickandmorty.domain.model.PaginatedResult
import com.example.rickandmorty.domain.repository.LocationRepository
import javax.inject.Inject

class GetLocationsUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(page: Int = 1): Result<PaginatedResult<Location>> {
        return repository.getLocation(page = page)
    }
}