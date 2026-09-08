package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.rickandmorty.domain.usecase.character.GetCharacterByIdUseCase
import com.example.rickandmorty.navigation.RickAndMortyApp
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var getCharacterByIdUseCase: GetCharacterByIdUseCase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            RickAndMortyTheme {
                RickAndMortyApp(
                    getCharacterByIdUseCase = getCharacterByIdUseCase
                )
            }
        }
    }
}

