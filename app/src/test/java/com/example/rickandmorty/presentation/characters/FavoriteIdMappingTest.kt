package com.example.rickandmorty.presentation.characters

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class FavoriteIdMappingTest {
    
    @Test
    fun `distinct until Changed compares favoriteId Sets no list instance` () {
        
        val list1 = listOf(1,2).map { it }.toSet()
        
        val list2 = listOf(1,2).map { it }.toSet()
        
        assertEquals(list1, list2)
        
        assertFalse(list1 === list2)
    }
    
    @Test
    fun `different favorites id's are not equal` () {
        
        val firstList = setOf(1,2)
        val secondList = setOf(2)
        
        assertTrue(firstList != secondList)
    }
    
}