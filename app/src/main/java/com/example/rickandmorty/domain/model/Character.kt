package com.example.rickandmorty.domain.model

data class Character(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: String,
    val originName: String,
    val locationName: String,
    val imageUrl: String,
    val episodeCount: Int,
)

enum class CharacterStatus {
    ALIVE,
    DEAD,
    UNKNOWN;

    companion object {
        fun fromApiValue(value: String): CharacterStatus = when (value.lowercase()) {
            "alive" -> ALIVE
            "dead" -> DEAD
            else -> UNKNOWN
        }
    }
}
