package com.example.rickandmorty.presentation.characters

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CharactersSearchFlowTest {
    
    val searchQueryFlow = MutableStateFlow("")
    
    val triggeredQueries = mutableListOf<String>()
    
    @Test   
    fun `drop prevents startup debounce reload that cancels image request` () = runTest { 
        
        val job = launch { 
            searchQueryFlow
                // выбрасываем значение
                .drop(1)
                .debounce(400)
                .distinctUntilChanged()
                .collect { 
                    triggeredQueries.add(it)
                }       
        }
        
        advanceTimeBy(500)
        
        assertEquals(emptyList<String>(), triggeredQueries)
        
        searchQueryFlow.value = "Rick"
        advanceTimeBy(402)
        
        assertEquals(listOf("Rick"), triggeredQueries)
        
        job.cancel()
        
    }
    
    @Test
    fun `without drop flow reload startup empty query` () = runTest {
        val job = launch {
            searchQueryFlow
                .debounce(400)
                .distinctUntilChanged()
                .collect {
                    triggeredQueries.add(it)
                }
        }
        advanceTimeBy(402)
        job.cancel()
        
        assertEquals(listOf(""), triggeredQueries)
        
    } 
    
}