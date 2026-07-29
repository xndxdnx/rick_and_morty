package com.example.rickandmorty.data.mappers

import com.example.rickandmorty.data.local.entity.FavoriteCharacterEntity
import com.example.rickandmorty.data.remote.dto.CharacterDto
import com.example.rickandmorty.data.remote.dto.EpisodeDto
import com.example.rickandmorty.data.remote.dto.LocationDto
import com.example.rickandmorty.data.remote.dto.PaginatedInfoDto
import com.example.rickandmorty.data.remote.dto.PaginatedResponseDto
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharacterStatus
import com.example.rickandmorty.domain.model.Episode
import com.example.rickandmorty.domain.model.Location
import com.example.rickandmorty.domain.model.PaginatedResult
import kotlin.Int


fun CharacterDto.toDomain(): Character = Character(
    id = id,
    name = name,
    status = CharacterStatus.fromApiValue(status),
    species = species,
    type = type.ifBlank { "—" },
    gender = gender,
    originName = origin.name,
    locationName = location.name,
    imageUrl = image,
    episodeCount = episode.size
)

fun LocationDto.toDomain(): Location = Location(
    id = id,
    name = name,
    type = type.ifBlank { "Unknown" },
    dimension = dimension,
    residentsCount = residents.size
)

fun EpisodeDto.toDomain(): Episode = Episode(
    id = id,
    name = name,
    airDate = airDate,
    code = episode,
    charactersCount = characters.size
)

fun <T, R> PaginatedResponseDto<T>.toDomain(
    page: Int,
    mapper: (T) -> R
) : PaginatedResult<R> = info.toDomain (page, results.map(mapper))


fun <R> PaginatedInfoDto.toDomain (
    page: Int,
    items: List<R>
) : PaginatedResult <R> = PaginatedResult(
    items = items,
    currentPage = page,
    totalPages = pages,
    hasNextPage = next != null
)

fun Character.toFavoriteEntity () : FavoriteCharacterEntity = FavoriteCharacterEntity(
    id = id,
    name = name,
    status = status.name,
    species = species,
    type = type,
    gender = gender,
    originName = originName,
    locationName = locationName,
    imageUrl = imageUrl,
    episodeCount = episodeCount
)

fun FavoriteCharacterEntity.toDomain() : Character = Character(
    id = id,
    name = name,
    status = CharacterStatus.valueOf(status),
    species = species,
    type = type,
    gender = gender,
    originName = originName,
    locationName = locationName,
    imageUrl = imageUrl,
    episodeCount = episodeCount
)