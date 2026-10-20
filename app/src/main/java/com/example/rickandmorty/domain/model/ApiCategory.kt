package com.example.rickandmorty.domain.model

enum class ApiCategory (
    val title: String
){
    Characters(
        title = "Characters"
    ),
    Location(
        title = "Location"
    ),
    Episodes(
        title = "Episodes"
    )
}

