package com.example.rickandmorty.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rickandmorty.domain.usecase.character.GetCharacterByIdUseCase
import com.example.rickandmorty.presentation.categories.CategoriesViewModel
import com.example.rickandmorty.presentation.categories.CategoryScreen
import com.example.rickandmorty.presentation.character_screen.CharacterScreen
import com.example.rickandmorty.presentation.character_screen.CharactersScreenViewModel
import com.example.rickandmorty.presentation.favorites_screen.FavoriteScreen
import com.example.rickandmorty.presentation.favorites_screen.FavoritesViewModel
import com.rickmorty.presentation.common.event.ObserveDialogEvents
import com.rickmorty.presentation.common.event.ObserveUiEvents

@Composable
fun RickAndMortyApp(
    getCharacterByIdUseCase: GetCharacterByIdUseCase
) {
    val navController = rememberNavController()

    val snackBarHostState = RememberSnackBarHostState()

    val favoritesViewModel: FavoritesViewModel = hiltViewModel()

    
    
    
    ObserveUiEvents(
        uiEvents = favoritesViewModel.uiEvent,
        snackbarHostState = snackBarHostState
    )

    ObserveDialogEvents(
        dialogEvents = favoritesViewModel.dialogEvent,
        getCharacterByIdUseCase = getCharacterByIdUseCase,
        onConfirmRemoveFavorite = favoritesViewModel::onConfirmRemoteFavorite
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar() {
                BottomBarItems.entries.forEach { destination ->
                    NavigationBarItem(
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        selected = destination.route == currentRoute,
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = destination.title,
                            )
                        }
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->

        NavHost(
            navController,
            startDestination = BottomBarItems.Characters.route,
            modifier = Modifier
                .padding(paddingValues = paddingValues)
        ) {
            
            composable (BottomBarItems.Characters.route) {
                val characterViewModel : CharactersScreenViewModel = hiltViewModel()
                CharacterScreen()
                ObserveUiEvents(
                    uiEvents = characterViewModel.uiEvent,
                    snackbarHostState = snackBarHostState
                    )
                ObserveDialogEvents(
                    dialogEvents = characterViewModel.dialogEvent,
                    getCharacterByIdUseCase = getCharacterByIdUseCase
                )
            }
            composable (BottomBarItems.Favorites.route) {
                FavoriteScreen()
            }
            composable(BottomBarItems.Categories.route) {
                val categoriesViewModel: CategoriesViewModel = hiltViewModel()
                CategoryScreen()
                
                ObserveUiEvents(
                    uiEvents = categoriesViewModel.uiEvent,
                    snackbarHostState = snackBarHostState
                )
                ObserveDialogEvents(
                    dialogEvents = categoriesViewModel.dialogEvent,
                    getCharacterByIdUseCase = getCharacterByIdUseCase
                )
            }
        }
    }
    
}

@Composable
private fun RememberSnackBarHostState(): SnackbarHostState = remember { SnackbarHostState() }

sealed class BottomBarItems(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Characters :
        BottomBarItems(route = "characters", title = "Characters", icon = Icons.Default.Person)

    data object Favorites :
        BottomBarItems(route = "favorites", title = "Favorites", icon = Icons.Default.Favorite)

    data object Categories :
        BottomBarItems(route = "categories", title = "Categories", icon = Icons.Default.List)

    companion object {
        val entries = listOf(Characters, Favorites, Categories)
    }
}