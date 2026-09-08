package com.example.rickandmorty.presentation.character_screen

import android.content.Context
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.usecase.character.GetCharactersUseCase
import com.example.rickandmorty.domain.usecase.character.SearchCharactersUseCase
import com.example.rickandmorty.domain.usecase.favorite.ObserveFavoriteUseCase
import com.example.rickandmorty.domain.usecase.favorite.ToggleFavoriteUseCase
import com.example.rickandmorty.presentation.common.base.BaseViewModel
import com.example.rickandmorty.presentation.common.efects.prefetchCharacterImages
import com.example.rickandmorty.presentation.common.event.DialogEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersScreenViewModel @Inject constructor(
    val getCharactersUseCase: GetCharactersUseCase,
    val searchCharactersUseCase: SearchCharactersUseCase,
    val observeFavoriteUseCase: ObserveFavoriteUseCase,
    val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    @ApplicationContext
    private val appContext: Context,
    private val imageLoader: ImageLoader
) : BaseViewModel() {
    
    private val _charactersUiState = MutableStateFlow(CharactersUiState())
    val charactersUiState = _charactersUiState.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")

    // механизм защиты от частых запросов загрузки 
    private var lastLoadMoreAtMs = 0L
    
    companion object {
        const val LOAD_MORE_MIN_INTERVAL = 1_000L
    }
    
    init {
        observeFavoriteUseCase()
            .map { favorites -> 
                favorites.map { favorite ->
                    favorite.id
                }.toSet()
            }
            .distinctUntilChanged()
            .onEach { favoriteIds -> 
                _charactersUiState.update { state -> 
                    state.copy(
                        favoriteIds = favoriteIds
                    )
                }
            }
            
//            .onEach { favorite ->
//                _charactersUiState.update { state -> 
//                    state.copy(
//                        favoriteIds = favorite.map { character -> 
//                            character.id
//                        }.toSet()
//                    )
//                }
//            }
            
            .launchIn(
                scope = viewModelScope
            )
        
        searchQueryFlow
            .drop(1) // Оператор пропускает 1 испускаемое значение потока
            .debounce(400)  
            .distinctUntilChanged()
            .onEach { query ->
                // Делаем замену всего списка !!!! Не добавление
               loadCharacters(
                   append = false,
                   query = query,
                   page = 1
               )
            }.launchIn(
                scope = viewModelScope
            )
        
        loadCharacters(
            append = false,
            page = 1,
            query = ""
        )
    }
    
    fun onSearchQueryChange(query: String) {
        _charactersUiState.update { state ->
            state.copy(
                searchQuery = query
            )
        }
        searchQueryFlow.value = query
    }
    
    fun onFavoriteClick(character: Character) {
        viewModelScope.launch { 
            val isFavorite = toggleFavoriteUseCase(character = character)
            
            showSnackBar(
                message = if (isFavorite) "Add to favorites"
                else "Remote from favorites"
            )
        }
    }
    
    fun onLoadMore () {
        val state = _charactersUiState.value
        
        if (state.isLoading || state.isLoadingMore || !state.hasNextPage) return

        val now = System.currentTimeMillis()
        
        if (now - lastLoadMoreAtMs < LOAD_MORE_MIN_INTERVAL) return
        
        lastLoadMoreAtMs = now
        
        _charactersUiState.update { state ->
            state.copy(
                isLoadingMore = true
            )
        }

        loadCharacters(
                append = true,
                page = state.currentPade + 1,
                query = state.searchQuery
            )
        
//        if (state.isLoadingMore || state.isLoading || !state.hasNextPage){
//            loadCharacters(
//                append = true,
//                page = state.currentPade + 1,
//                query = state.searchQuery
//            )
//        }
    }

    fun onCharacterClick (character: Character) {
        sendDialogEvent(
            DialogEvent.ShowCharacterDetails(
                characterId = character.id
            )
        )
    }
    
    fun onRetry() {
        loadCharacters(
            query = _charactersUiState.value.searchQuery,
            page = 1,
            append = false
        )
    }

    private fun loadCharacters(
        append: Boolean,
        page: Int,
        query: String
    ) {
        viewModelScope.launch {
            _charactersUiState.update { charactersUiState ->
                charactersUiState.copy(
                    isLoading = !append && charactersUiState.characters.isEmpty(),
                    isLoadingMore = append
                )
            }
            val result = if (query.isBlank()) {
                getCharactersUseCase(page = page)
            } else {
                searchCharactersUseCase(
                    page = page,
                    name = query
                )
            }

            result
                .onSuccess { paginated ->
                _charactersUiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        errorMessage = null,
                        hasNextPage = paginated.hasNextPage,
                        currentPade = paginated.currentPage,
                        characters =
                            if (append) state.characters + paginated.items
                            else paginated.items
                    )
                }
                    
                    viewModelScope.launch {
                        prefetchCharacterImages(
                            context = appContext,
                            imageLoader = imageLoader,
                            characters = paginated.items
                        )
                    }
                    
            }
                .onFailure { error ->
                    _charactersUiState.update { state ->
                        state.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            errorMessage = state.errorMessage,
                        )
                    }
                    
                    
//                    if (!append) {
//                        showError(
//                            title = "Portal malfunction",
//                            message = error.message ?: "Failed to load characters"
//                        )
//                    }
                }
        }
    }
}