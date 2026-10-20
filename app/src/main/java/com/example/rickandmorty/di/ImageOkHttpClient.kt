package com.example.rickandmorty.di

import javax.inject.Qualifier

// объявление пользовательской аннотации в котлине (не содержит логики)
// позволяет управлять 

@Qualifier
// помечает класс как квалификатор, позволяет различать зависимости одного типа, но с разным значением
@Retention(AnnotationRetention.BINARY) // означает что она сохраняется в скомпилированном байт-коде только на этапе компиляции 
// Задаёт - область видимости аннотации 
annotation class ImageOkHttpClient




//@Retention(AnnotationRetention.BINARY) // означает что она сохраняется в скомпилированном байт-коде только на этапе компиляции 
// SOURCE, RUNTIME во время выполнения