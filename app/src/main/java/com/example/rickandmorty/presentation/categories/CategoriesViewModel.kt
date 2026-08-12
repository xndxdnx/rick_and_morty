package com.example.rickandmorty.presentation.categories

import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.ApiCategory
import com.example.rickandmorty.domain.usecase.episode.GetEpisodesUseCase
import com.example.rickandmorty.domain.usecase.location.GetLocationsUseCase
import com.example.rickandmorty.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getEpisodesUseCase: GetEpisodesUseCase,
    private val getLocationsUseCase: GetLocationsUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState = _uiState.asStateFlow()

    
    fun onRetry() {
        
        when(_uiState.value.selectedTab) {
            CategoryTab.LOCATIONS -> {
                loadLocations(
                    append = false,
                    page = 1
                )
            }
            CategoryTab.EPISODES -> {
                loadEpisodes(
                    append = false,
                    page = 1
                )
            }
            CategoryTab.OVERVIEW -> Unit
        }
    }
    
    
    fun onTabSelected(categoryTab: CategoryTab) {
        _uiState.update { state ->
            state.copy(
                selectedTab = categoryTab
            )
        }
        when (categoryTab) {
            CategoryTab.LOCATIONS -> {
                if (_uiState.value.locations.isEmpty()) {
                     loadLocations(
                         append = false,
                         page = 1
                     )
                }
            }

            CategoryTab.EPISODES -> {
                if (_uiState.value.episodes.isEmpty()) {
                     loadEpisodes(
                         append = false,
                         page = 1
                     )
                }
            }

            CategoryTab.OVERVIEW -> Unit
        }
    }

    fun onCategoryClick(
        apiCategory: ApiCategory
    ) {
        when(apiCategory){
            ApiCategory.Episodes -> {
                onTabSelected(
                    categoryTab = CategoryTab.EPISODES
                )
            }
            ApiCategory.Location -> {
                onTabSelected(
                    categoryTab = CategoryTab.LOCATIONS
                )
            }

            ApiCategory.Characters -> {
                showSnackBar(
                    "Use Character Screen"
                )
            }
        }
    }
    
    fun onLoadMoreEpisodes() {
        val state  = _uiState.value
        
        if (state.isLoadingMoreEpisodes || !state.hasMoreEpisodes || state.isLoadingEpisodes){
            loadEpisodes(
                append = true,
                page = state.episodesPage + 1
            )
        }
    }
    
    fun onLoadMoreLocations() {
        val state = _uiState.value
        
        if (state.isLoadingMoreLocations || state.isLoadingLocations || !state.hasMoreLocations) {
            loadLocations(
                append = true,
                page = state.locationPage + 1
            )
        }
    }
    
    
    private fun loadEpisodes(
        append: Boolean,
        page: Int
    ) {

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoadingEpisodes = !append,
                    isLoadingMoreEpisodes = append
                )
            }

            getEpisodesUseCase(page = page)
                .onSuccess { resultEpisodes ->
                    _uiState.update { state ->
                        state.copy(
                            isLoadingEpisodes = false,
                            isLoadingMoreEpisodes = false,
                            hasMoreEpisodes = resultEpisodes.hasNextPage,
                            episodesPage = resultEpisodes.currentPage,
                            episodes = if (append) state.episodes + resultEpisodes.items
                            else resultEpisodes.items
                        )
                    }
                }
                .onFailure {
                    _uiState.update { state ->
                        state.copy(
                            isLoadingEpisodes = false,
                            isLoadingMoreEpisodes = false,
                            errorMessage = state.errorMessage,
                            )
                    }
                    showError(
                        title = "Portal Malfunction",
                        message = "Failed to load Episodes"
                    )
                }
        }

    }

    private fun loadLocations(
        append: Boolean,
        page: Int
    ) {

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoadingLocations = !append,
                    isLoadingMoreLocations = append
                )
            }

            getLocationsUseCase(page = page)
                .onSuccess { resultLocations ->
                    _uiState.update { state ->
                        state.copy(
                            isLoadingLocations = false,
                            isLoadingMoreLocations = false,
                            hasMoreLocations = resultLocations.hasNextPage,
                            locationPage = resultLocations.currentPage,
                            locations = if (append) state.locations + resultLocations.items
                            else resultLocations.items
                        )
                    }
                }
                .onFailure {
                    _uiState.update { state ->
                        state.copy(
                            isLoadingLocations = false,
                            isLoadingMoreLocations = false,
                            errorMessage = state.errorMessage,
                        )
                    }
                    showError(
                        title = "Portal Malfunction",
                        message = "Failed to load Locations"
                    )
                }
        }

    }


}