package com.example.rickandmorty.presentation.common.event

sealed interface UiEvent{
    data class ShowSnackBar(val message: String) : UiEvent
}

sealed interface DialogEvent {
    data class ShowError(val message: String, val title: String): DialogEvent
    data class ShowCharacterDetails(val characterId : Int) : DialogEvent
    data class ConfirmRemoveFavorite(val name: String, val characterId: Int) : DialogEvent
}

