package com.example.rickandmorty.data.mapper

import com.example.rickandmorty.data.mappers.toDomain
import com.example.rickandmorty.data.remote.dto.CharacterDto
import com.example.rickandmorty.data.remote.dto.ReferenceDto
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class CharacterMapperTest {
    @Test
    fun `maps image field from DTO to Domain Image Url` () {
        val dto = sampleCharacterDto(image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg")
        val character = dto.toDomain()
        assertEquals("https://rickandmortyapi.com/api/character/avatar/1.jpeg", character.imageUrl)
        assertTrue(character.imageUrl.startsWith("https://"))
        assertTrue(character.imageUrl.endsWith(".jpeg"))
    }
    
    @Test
    fun `maps all character fields including imageUrl` () {
        val dto = sampleCharacterDto(image = "https://rickandmortyapi.com/api/character/avatar/42.jpeg", id = 42, name = "Rick Sanchez")
        val character = dto.toDomain()
        assertEquals(character.id , 42)
        assertEquals(character.name , "Rick Sanchez")
        assertEquals(character.imageUrl , "https://rickandmortyapi.com/api/character/avatar/42.jpeg")
        assertEquals(2, character.episodeCount)
    }
    
    
    
}

private fun sampleCharacterDto(
    id: Int = 1,
    name: String = "Test Character",
    image: String = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
) = CharacterDto(
    id = id,
    name = name,
    status = "Alive",
    species = "Human",
    type = "",
    gender = "Male",
    origin = ReferenceDto(name = "Earth", url = "https://rickandmortyapi.com/api/location/1"),
    location = ReferenceDto(name = "Earth", url = "https://rickandmortyapi.com/api/location/20"),
    image = image,
    episode = listOf(
        "https://rickandmortyapi.com/api/episode/1",
        "https://rickandmortyapi.com/api/episode/2",
    ),
    url = "https://rickandmortyapi.com/api/character/$id",
    created = "2017-11-04T18:48:46.250Z",
)
