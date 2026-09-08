package com.example.rickandmorty.di

import coil.ImageLoader
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// Аннотация @EntryPoint для получения зависимостей из графа зависимостей Hilt
// Позволяет вручную запросить зависимости из контейнера DI

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ImageLoaderEntryPoint {
    
    // Image Loader указывает какую именно зависимость нужно предоставить 
    // Понадобится потом когда мы будем создавать NetworkModule
    fun imageLoader(): ImageLoader
    
}