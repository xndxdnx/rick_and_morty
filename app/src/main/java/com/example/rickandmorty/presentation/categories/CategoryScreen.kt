package com.example.rickandmorty.presentation.categories

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.rickandmorty.domain.model.ApiCategory
import com.example.rickandmorty.domain.model.Episode
import com.example.rickandmorty.domain.model.Location
import com.example.rickandmorty.presentation.components.EmptyState
import com.example.rickandmorty.presentation.components.LoadingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)

    ) {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )

        PrimaryTabRow(
            //ordinal - enum индекс
            selectedTabIndex = uiState.value.selectedTab.ordinal,
        ) {
            Tab(
                selected = uiState.value.selectedTab == CategoryTab.OVERVIEW,
                onClick = {
                    viewModel.onTabSelected(
                        categoryTab = CategoryTab.OVERVIEW
                    )
                },
                text = {
                    Text(
                        text = "Categories",
                    )
                }
            )
            Tab(
                selected = uiState.value.selectedTab == CategoryTab.LOCATIONS,
                onClick = {
                    viewModel.onTabSelected(
                        categoryTab = CategoryTab.LOCATIONS
                    )
                },
                text = {
                    Text(
                        text = "Locations",
                    )
                }
            )
            Tab(
                selected = uiState.value.selectedTab == CategoryTab.EPISODES,
                onClick = {
                    viewModel.onTabSelected(
                        categoryTab = CategoryTab.EPISODES
                    )
                },
                text = {
                    Text(
                        text = "Episodes",
                    )
                }
            )

        }

        when (uiState.value.selectedTab) {
            CategoryTab.OVERVIEW -> {
                CategoryOverview(
                    categories = uiState.value.categories,
                    onCategoryClick = viewModel::onCategoryClick
                )
            }

            CategoryTab.LOCATIONS -> {
                LocationList(
                    locations = uiState.value.locations,
                    isLoading = uiState.value.isLoadingLocations,
                    isLoadingMore = uiState.value.isLoadingMoreLocations,
                    hasMore = uiState.value.hasMoreLocations,
                    errorMessage = uiState.value.errorMessage,
                    onRetry = viewModel::onRetry,
                    onLoadMore = viewModel::onLoadMoreLocations
                )
            }

            CategoryTab.EPISODES -> {
                EpisodesList(
                    episodes = uiState.value.episodes,
                    isLoading = uiState.value.isLoadingEpisodes,
                    isLoadingMore = uiState.value.isLoadingMoreEpisodes,
                    hasMore = uiState.value.hasMoreEpisodes,
                    errorMessage = uiState.value.errorMessage,
                    onRetry = viewModel::onRetry,
                    onLoadMore = viewModel::onLoadMoreEpisodes
                )
            }
        }
    }
}

@Composable
private fun CategoryOverview(
    categories: List<ApiCategory>,
    onCategoryClick: (ApiCategory) -> Unit
) {
    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories) { category ->
            CategoryCard(
                category = category,
                onClick = { onCategoryClick(category) }
            )
        }
    }

}

@Composable
private fun CategoryCard(
    category: ApiCategory,
    onClick: () -> Unit
) {
    val description = when (category) {
        ApiCategory.Characters -> {
            Text(
                text = ""
            )
        }

        ApiCategory.Location -> {
            Text(
                text = ""
            )
        }

        ApiCategory.Episodes -> {
            Text(
                text = ""
            )
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "$description",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}


@Composable
private fun EpisodesList(
    episodes: List<Episode>,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    hasMore: Boolean,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    errorMessage: String?
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        // -> Вычисляет производное состояние онон изменяется только при изменении зависимых значений (listState, locations)
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= episodes.lastIndex - 2 && hasMore
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()
    }

    when {
        isLoading -> {
            LoadingState(
                modifier = Modifier.fillMaxSize()
            )
        }

        errorMessage != null || episodes.isEmpty() -> {
            ErrorRetry(
                message = errorMessage,
                onRetry = onRetry
            )
        }

        episodes.isEmpty() -> {
            EmptyState(
                message = "No Episodes",
                modifier = Modifier.fillMaxSize()
            )
        }

        else -> {
            LazyColumn(
                state = listState,
                modifier = Modifier,
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(episodes, key = { it.id }) { episode ->
                    EpisodesCard(
                        episode = episode
                    )
                }

                if (isLoadingMore) {
                    item {
                        Box(
                            Modifier.fillMaxWidth()
                        ) {
                            LoadingState()
                        }

                    }
                }

            }
        }
    }
}

@Composable
private fun EpisodesCard(
    episode: Episode
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            Modifier.padding(16.dp)
        ) {
            Text(
                text = episode.name,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "${episode.code} • ${episode.airDate}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(top = 4.dp)
            )
            Text(
                text = "Character count : ${episode.charactersCount}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
        }
    }
}




        @Composable
private fun LocationList(
    locations: List<Location>,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    hasMore: Boolean,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    errorMessage: String?
) {

    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        // -> Вычисляет производное состояние онон изменяется только при изменении зависимых значений (listState, locations)
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= locations.lastIndex - 2 && hasMore
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()

    }

    when {
        isLoading -> {
            LoadingState(
                modifier = Modifier.fillMaxSize()
            )
        }

        errorMessage != null || locations.isEmpty() -> {
            ErrorRetry(
                message = errorMessage,
                onRetry = onRetry
            )
        }

        locations.isEmpty() -> {
            EmptyState(
                message = "No Location",
                modifier = Modifier.fillMaxSize()
            )
        }

        else -> {
            LazyColumn(
                state = listState,
                modifier = Modifier,
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(locations, key = { it.id }) { location ->
                    LocationCard(
                        location = location
                    )
                }

                if (isLoadingMore) {
                    item {
                        Box(
                            Modifier.fillMaxWidth()
                        ) {
                            LoadingState(

                            )
                        }

                    }
                }

            }
        }
    }

}

@Composable
private fun ErrorRetry(
    message: String?,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$message",
            color = MaterialTheme.colorScheme.error
        )
        Button(
            onClick = onRetry,
            modifier = Modifier
                .padding(top = 12.dp)
        ) {
            Text(
                text = "Retry"
            )
        }
    }
}

@Composable
private fun LocationCard(
    location: Location,

    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            Modifier.padding(16.dp)
        ) {
            Text(
                text = location.name,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "${location.type} • ${location.dimension}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(top = 4.dp)
            )
            Text(
                text = "Residents count : ${location.residentsCount}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
        }
    }
}