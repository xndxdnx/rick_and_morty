package com.example.rickandmorty

import android.app.Application
import coil.ImageLoader
// Это интерфейс для реализ NewImageLoader
import coil.ImageLoaderFactory
import coil.imageLoader
import com.example.rickandmorty.di.ImageLoaderEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RickAndMortyApplication : Application(), ImageLoaderFactory  {
    override fun onCreate() {
        super.onCreate()
        // вызываем расширение у контекста для создания экземпляра ImageLoader
        // для ускорения 
        applicationContext.imageLoader
    }
    // Это специальный утилитный класс Hilt для доступа к EntryPoin из кода который не является компонентом Hilt
    override fun newImageLoader(): ImageLoader {
        return EntryPointAccessors.fromApplication(
            // fromApplication метод для получения
            applicationContext,
            ImageLoaderEntryPoint::class.java
        ).imageLoader()
        //вызов метода entry point который запрашивает у HILT готовый экземпляр нашего ImageLoader
        // и DH находит Provider в koil модуле и возвращает Singleton
    }
}