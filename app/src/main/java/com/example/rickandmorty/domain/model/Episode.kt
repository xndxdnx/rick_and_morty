package com.example.rickandmorty.domain.model

data class Episode(
    val id: Int,
    val name: String,
    val airDate: String,
    val code: String,
    val charactersCount: Int,
)
