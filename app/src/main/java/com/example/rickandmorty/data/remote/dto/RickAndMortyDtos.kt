package com.example.rickandmorty.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReferenceDto(
    val name: String,
    val url: String
)

data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: ReferenceDto,
    val location: ReferenceDto,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,
)

data class LocationDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String,
)

data class EpisodeDto(
    val id: Int,
    val name: String,
    @SerializedName("air_date") 
    val airDate: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String,
)

data class PaginatedInfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?,
)

data class PaginatedResponseDto<T>(
    val info: PaginatedInfoDto,
    val results: List<T>,
)
