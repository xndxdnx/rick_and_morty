package com.example.rickandmorty.presentation.character_screen

import com.example.rickandmorty.domain.model.Character

data class CharactersUiState (
    val isLoading: Boolean = false,
    val favoriteIds: Set<Int> = emptySet(),
    val errorMessage: String? = null,
    val characters: List<Character> = emptyList(),
    val currentPade : Int = 1,
    val hasNextPage: Boolean = false,
    val isLoadingMore: Boolean = false,
    val searchQuery: String = ""
)
