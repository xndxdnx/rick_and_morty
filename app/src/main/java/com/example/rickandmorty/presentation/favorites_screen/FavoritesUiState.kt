package com.example.rickandmorty.presentation.favorites_screen

import com.example.rickandmorty.domain.model.Character

data class FavoritesUiState(
    val favorites: List<Character> = emptyList(),
    val isLoading: Boolean = true
)
