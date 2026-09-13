package com.example.rickandmorty.data.remote.dto

import com.example.rickandmorty.data.mappers.toDomain
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Test

class CharacterDtoJsonParsingTest {
    
    private val gson = Gson()
    
    @Test
    fun `parse image url from api json response` () {
        val json = """
            {
              "id": 1,
              "name": "Rick Sanchez",
              "status": "Alive",
              "species": "Human",
              "type": "",
              "gender": "Male",
              "origin": {
                "name": "Earth (C-137)",
                "url": "https://rickandmortyapi.com/api/location/1"
              },
              "location": {
                "name": "Citadel of Ricks",
                "url": "https://rickandmortyapi.com/api/location/3"
              },
              "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
              "episode": [
                "https://rickandmortyapi.com/api/episode/1"
              ],
              "url": "https://rickandmortyapi.com/api/character/1",
              "created": "2017-11-04T18:48:46.250Z"
            }
        """.trimIndent()
        
        val dto = gson.fromJson(json, CharacterDto::class.java)
        
        val character = dto.toDomain()
        
        assertEquals( "https://rickandmortyapi.com/api/character/avatar/1.jpeg", dto.image)
        
        assertEquals( "https://rickandmortyapi.com/api/character/avatar/1.jpeg", character.imageUrl)
        
        assertFalse(character.imageUrl.isBlank())
        
        
    }
}