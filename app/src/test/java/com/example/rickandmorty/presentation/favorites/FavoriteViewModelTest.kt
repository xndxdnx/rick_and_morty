package com.example.rickandmorty.presentation.favorites

import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharacterStatus
import com.example.rickandmorty.domain.repository.FavoriteRepository
import com.example.rickandmorty.domain.usecase.favorite.ObserveFavoriteUseCase
import com.example.rickandmorty.presentation.common.event.DialogEvent
import com.example.rickandmorty.presentation.favorites_screen.FavoritesViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.collections.listOf

class FavoriteViewModelTest {
    
    val dispatcher = UnconfinedTestDispatcher()
    
    
    @Before
    fun setUp () {
        Dispatchers.setMain(dispatcher)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `on remote click emits confirm remote dialog event` () = runTest (dispatcher){

        val repository = FakeFavoriteRepository()
        
        val vm = createViewModel(repository)
        
        val character = sampleCharacter(
            id = 5,
            name = "Rick"
        )
        
        val dialogEvents = mutableListOf<DialogEvent>()
        
        val collectJob = launch { 
            vm.dialogEvent.collect { event ->  
                dialogEvents.add(
                    event
                )
            }
        }
        //  Даёт корутинам выполнить всю работу
        advanceUntilIdle()
        
        vm.onRemote(character)
        
        assertEquals(DialogEvent.ConfirmRemoveFavorite(characterId = 5, name = "Rick"), dialogEvents.single())
        
        collectJob.cancel()
        
    }
    
    @Test
    fun `confirm remote favorite character from repository and UI state` () = runTest (dispatcher) {
        val repository = FakeFavoriteRepository()
        val rick = sampleCharacter(
            name = "Rick",
            id = 1
        )
        val morty = sampleCharacter(
            name = "Morty",
            id = 2
        )
        repository.setFavorites(listOf(rick, morty))
        val vm = createViewModel(repository = repository)
        advanceUntilIdle()
        assertEquals(listOf(rick, morty), vm.uiState.value.favorites)
        vm.onConfirmRemoteFavorite(1)
        advanceUntilIdle()
        assertEquals(listOf(1), repository.removedCharacterIds)
        assertEquals(listOf(morty), vm.uiState.value.favorites)
        
        
        
    }
    
    
    @Test
    fun `favorites list become empty after removing last favorite` () = runTest (dispatcher){
        val repository = FakeFavoriteRepository()
        val rick = sampleCharacter(
            name = "Rick",
            id = 1
        )
        repository.setFavorites(listOf(rick))
        val vm = createViewModel(repository = repository)
        advanceUntilIdle()
        assertEquals(listOf(rick), vm.uiState.value.favorites)
        vm.onConfirmRemoteFavorite(1)
        advanceUntilIdle()
        assertEquals(listOf(1), repository.removedCharacterIds)
        assertTrue(vm.uiState.value.favorites.isEmpty())
    }
    
    private fun sampleCharacter(
        id: Int,
        name: String,
    ) = Character(
        id = id,
        name = name,
        status = CharacterStatus.ALIVE,
        species = "Human",
        type = "",
        gender = "Male",
        originName = "Earth",
        locationName = "Earth",
        imageUrl = "https://rickandmortyapi.com/api/character/avatar/$id.jpeg",
        episodeCount = 1,
    )

    private fun createViewModel(repository: FakeFavoriteRepository): FavoritesViewModel {
        return FavoritesViewModel(
            observeFavoriteUseCase = ObserveFavoriteUseCase(repository),
            favoriteRepository = repository
        )
    }


}


private class FakeFavoriteRepository() : FavoriteRepository {

    fun setFavorites (list: List<Character>) {
        favorites.value = list
    }
    
    private val favorites = MutableStateFlow<List<Character>>(emptyList())
    
    val removedCharacterIds = mutableListOf<Int>()
    
    override suspend fun addToFavorite(character: Character) {
        favorites.value += character
    }

    
    
    
    override suspend fun removeFromFavorite(characterId: Int) {
        removedCharacterIds.add(characterId)
        favorites.value = favorites.value.filterNot { it.id == characterId }
    }

    override fun observeFavorites(): Flow<List<Character>> {
        return favorites.asStateFlow()
    }

    override suspend fun isFavorite(characterId: Int): Boolean {
        return favorites.value.any { character ->
            character.id  == characterId
        }
    }

    override suspend fun toggleFavorite(character: Character): Boolean {
        return if (isFavorite(character.id)) {
            removeFromFavorite(character.id)
            false
        }else {
            addToFavorite(character)
            true
        }
    }

}