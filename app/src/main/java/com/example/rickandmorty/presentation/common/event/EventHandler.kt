package com.rickmorty.presentation.common.event

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.rickandmorty.R

import com.example.rickandmorty.domain.model.CharacterStatus
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.usecase.character.GetCharacterByIdUseCase
import com.example.rickandmorty.presentation.common.event.DialogEvent
import com.example.rickandmorty.presentation.common.event.UiEvent
import com.example.rickandmorty.presentation.components.CharacterDetailContent
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun ObserveUiEvents(
    uiEvents: Flow<UiEvent>,
    snackbarHostState: SnackbarHostState,
) {
    LaunchedEffect(uiEvents, snackbarHostState) {
        uiEvents.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }
}

@Composable
fun ObserveDialogEvents(
    dialogEvents: Flow<DialogEvent>,
    getCharacterByIdUseCase: GetCharacterByIdUseCase,
    onConfirmRemoveFavorite: (Int) -> Unit = {},
) {
    var activeDialog by remember { mutableStateOf<DialogEvent?>(null) }
    var detailCharacter by remember { mutableStateOf<Character?>(null) }

    LaunchedEffect(dialogEvents) {
        dialogEvents.collect { event ->
            activeDialog = event
            if (event is DialogEvent.ShowCharacterDetails) {
                getCharacterByIdUseCase(event.characterId)
                    .onSuccess { detailCharacter = it }
                    .onFailure {
                        detailCharacter = null
                        activeDialog = DialogEvent.ShowError(
                            title = "Portal malfunction",
                            message = it.message ?: "Character not found",
                        )
                    }
            } else {
                detailCharacter = null
            }
        }
    }

    DialogEventContent(
        dialog = activeDialog,
        detailCharacter = detailCharacter,
        onDismiss = {
            activeDialog = null
            detailCharacter = null
        },
        onConfirmRemoveFavorite = { characterId ->
            onConfirmRemoveFavorite(characterId)
            activeDialog = null
        },
    )
}

@Composable
internal fun DialogEventContent(
    dialog: DialogEvent?,
    detailCharacter: Character?,
    onDismiss: () -> Unit,
    onConfirmRemoveFavorite: (Int) -> Unit,
) {
    when (dialog) {
        is DialogEvent.ShowError -> {
            ErrorDialog(
                title = dialog.title,
                message = dialog.message,
                onDismiss = onDismiss,
            )
        }

        is DialogEvent.ShowCharacterDetails -> {
            detailCharacter?.let { character ->
                CharacterDetailsDialog(
                    character = character,
                    onDismiss = onDismiss,
                )
            }
        }

        is DialogEvent.ConfirmRemoveFavorite -> {
            ConfirmRemoveFavoriteDialog(
                characterId = dialog.characterId,
                characterName = dialog.name,
                onConfirm = onConfirmRemoveFavorite,
                onDismiss = onDismiss,
            )
        }

        null -> Unit
    }
}

@Composable
internal fun ErrorDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Ok")
            }
        },
    )
}

@Composable
internal fun CharacterDetailsDialog(
    character: Character,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = character.name) },
        text = {
            CharacterDetailContent(
                character
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Close")
            }
        },
    )
}

@Composable
internal fun ConfirmRemoveFavoriteDialog(
    characterId: Int,
    characterName: String,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "RemoveFromFavorite") },
        text = {
            Text(
                text = "Remove $characterName from your squad?"
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(characterId) }) {
                Text(
                    text = "Remove"
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
    )
}

@Preview(showBackground = true, name = "Dialog — ShowError")
@Composable
private fun ErrorDialogPreview() {
    RickAndMortyTheme() {
        ErrorDialog(
            title = "Portal malfunction",
            message = "HTTP 429 Too Many Requests",
            onDismiss = {},
        )
    }
}

@Preview(showBackground = true, name = "Dialog — ShowCharacterDetails")
@Composable
private fun CharacterDetailsDialogPreview() {
    RickAndMortyTheme() {
        CharacterDetailsDialog(
            character = previewCharacter(),
            onDismiss = {},
        )
    }
}

@Preview(showBackground = true, name = "Dialog — ConfirmRemoveFavorite")
@Composable
private fun ConfirmRemoveFavoriteDialogPreview() {
    RickAndMortyTheme() {
        ConfirmRemoveFavoriteDialog(
            characterId = 2,
            characterName = "Morty Smith",
            onConfirm = {},
            onDismiss = {},
        )
    }
}

private fun previewCharacter() = Character(
    id = 1,
    name = "Rick Sanchez",
    status = CharacterStatus.ALIVE,
    species = "Human",
    type = "",
    gender = "Male",
    originName = "Earth (C-137)",
    locationName = "Citadel of Ricks",
    imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
    episodeCount = 51,
)

fun CharacterStatus.displayName(): String = when (this) {
    CharacterStatus.ALIVE -> "Alive"
    CharacterStatus.DEAD -> "Dead"
    CharacterStatus.UNKNOWN -> "Unknown"
}
