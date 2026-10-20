package com.example.rickandmorty.presentation.categories

import com.example.rickandmorty.domain.model.ApiCategory
import com.example.rickandmorty.domain.model.Episode
import com.example.rickandmorty.domain.model.Location

data class CategoryUiState(
    val selectedTab: CategoryTab = CategoryTab.OVERVIEW,
    val errorMessage: String? = null,
    val isLoadingEpisodes: Boolean = false,
    val isLoadingLocations: Boolean = false,
    val isLoadingMoreEpisodes: Boolean = false,
    val isLoadingMoreLocations: Boolean = false,
    val locations: List<Location> = emptyList(),
    val episodes: List<Episode> = emptyList(),
    val locationPage: Int = 1,
    val episodesPage: Int = 1,
    val hasMoreLocations: Boolean = false,
    val hasMoreEpisodes: Boolean = false
) {
    val categories : List<ApiCategory> = ApiCategory.entries
}

enum class CategoryTab(
    
) {
    OVERVIEW,
    LOCATIONS,
    EPISODES
}
