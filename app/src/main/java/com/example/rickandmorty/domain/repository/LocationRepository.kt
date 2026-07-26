package com.example.rickandmorty.domain.repository

import com.example.rickandmorty.domain.model.Location
import com.example.rickandmorty.domain.model.PaginatedResult

interface LocationRepository {
    suspend fun getLocation (page: Int): Result<PaginatedResult<Location>>
}