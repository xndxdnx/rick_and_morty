package com.example.rickandmorty.presentation.favorites_screen

import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.repository.FavoriteRepository
import com.example.rickandmorty.domain.usecase.favorite.ObserveFavoriteUseCase
import com.example.rickandmorty.presentation.common.base.BaseViewModel
import com.example.rickandmorty.presentation.common.event.DialogEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    val favoriteRepository: FavoriteRepository,
    val observeFavoriteUseCase: ObserveFavoriteUseCase
): BaseViewModel(){
    
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState = _uiState.asStateFlow()
    
    init {
        observeFavoriteUseCase()
            .onEach { items ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        favorites = items,
                    )
                }
            }.launchIn(
                scope = viewModelScope
            )
    }
    
    fun onCharacterClick(character: Character) {
        sendDialogEvent(
            event = DialogEvent.ShowCharacterDetails(characterId = character.id)
        )
    }
    
    fun onRemote(character: Character) {
        sendDialogEvent(
            DialogEvent.ConfirmRemoveFavorite(
                name = character.name,
                characterId = character.id
            )
        )
    }
    
    
    
}