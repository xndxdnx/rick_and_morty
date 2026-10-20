package com.example.rickandmorty.presentation.character_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rickandmorty.presentation.common.efects.LazyListPaginationEffect
import com.example.rickandmorty.presentation.components.CharacterCard
import com.example.rickandmorty.presentation.components.EmptyState
import com.example.rickandmorty.presentation.components.LoadingState
import com.example.rickandmorty.presentation.components.RickAndMortySearchBar

@Composable
fun CharacterScreen(
    modifier: Modifier = Modifier,
    viewModel: CharactersScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.charactersUiState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

//    val shouldLoadMore by remember {
//        derivedStateOf {
//            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
//            lastVisible >= uiState.value.characters.lastIndex - 2 && uiState.value.hasNextPage
//        }
//    }
//
//    LaunchedEffect(shouldLoadMore) {
//        if (shouldLoadMore) viewModel.onLoadMore()
//    }

    LazyListPaginationEffect(
        listState = listState,
        itemCount = uiState.value.characters.size,
        hasNextPage = uiState.value.hasNextPage,
        isLoading = uiState.value.isLoading,
        isLoadingMore = uiState.value.isLoadingMore,
        onLoadMore = viewModel::onLoadMore
    )
    

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Characters",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        RickAndMortySearchBar(
            query = uiState.value.searchQuery,
            onQueryChange = viewModel::onSearchQueryChange,
        )
        
        when {
            uiState.value.isLoading -> {
                LoadingState(modifier = Modifier.fillMaxSize())
            }
            uiState.value.errorMessage != null && uiState.value.characters.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { 
                    Text(
                        text = uiState.value.errorMessage.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(
                        onClick = viewModel::onRetry,
                        modifier = Modifier
                            .padding(top = 12.dp)
                    ) { 
                        Text(
                            text = "Retry"
                        )
                    }
                }
            }
            uiState.value.characters.isEmpty() -> {
                EmptyState(
                    message = "No Characters Found",
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                
                // showLoadMoreButton
                
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    
                    items(uiState.value.characters, key = {character ->  character.id}) { character ->
                        CharacterCard(
                            character = character,
                            onItemClick = {viewModel.onCharacterClick(character = character)},
                            isFavorite = character.id in uiState.value.favoriteIds,
                            onFavoriteClick = {
                                viewModel.onFavoriteClick(character = character)
                            }
                        )
                    }
                    if (uiState.value.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ){
                                LoadingState()
                            }
                        }
                    }

                    if (uiState.value.hasNextPage && !uiState.value.isLoadingMore && !listState.canScrollForward) {
                        item {
                            Button(
                                onClick = viewModel::onLoadMore,
                                modifier = Modifier.fillMaxWidth().padding(8.dp)
                            ) { Text(text = "Load More")}
                        }
                    }
                    
                    
                }
            }
        }
        
    }
    


}