package com.example.rickandmorty.presentation.favorites_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rickandmorty.presentation.components.CharacterCard
import com.example.rickandmorty.presentation.components.EmptyState
import com.example.rickandmorty.presentation.components.LoadingState

@Composable
fun FavoriteScreen (
    viewModel: FavoritesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Favorites",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        
        when {
            uiState.value.isLoading -> {
                LoadingState(
                    modifier = Modifier.fillMaxSize()
                )
            }
            uiState.value.favorites.isEmpty() -> {
                EmptyState(
                    message = "No Favorites yet",
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    
                    items( items = uiState.value.favorites, key = {favorite -> favorite.id}) { favoriteCharacter ->
                        CharacterCard(
                            character = favoriteCharacter,
                            isFavorite = true,
                            onItemClick = {
                                viewModel.onCharacterClick(character = favoriteCharacter)
                            },
                            onFavoriteClick = {
                                viewModel.onRemote(character = favoriteCharacter)
                            }
                        )
                    }
                }
            }
        }
        
        
    }
   
    
    
}