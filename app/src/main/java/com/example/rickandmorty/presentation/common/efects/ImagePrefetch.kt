package com.example.rickandmorty.presentation.common.efects

import android.content.Context
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.rickandmorty.domain.model.Character
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit


// Для предварит. загрузки изображений в кеш coil чтобы сразу отображать картинки без задержек 

suspend fun prefetchCharacterImages (
    imageLoader: ImageLoader,
    characters: List<Character>,
    context: Context
) = coroutineScope {
    //  Для контроля параллельности выполнения задач загрузки
    val semaphore = Semaphore(3)
    
    characters.map { character -> 
        async { 
            // with - получаем разрешение от semaphore на выполнение если разрешения нет - корутина приостанавливается до их освобождения 
            // это гарантирует то что одновременно выполняется не более 3 загрузок 
            semaphore.withPermit {  
                // Выполняет запрос на загрузку изображений через coil
                imageLoader.execute(
                    ImageRequest.Builder(context)
                        .data(character.imageUrl)
                        .memoryCacheKey(character.imageUrl)
                        .diskCacheKey(character.imageUrl)
                        .allowHardware(false)
                        .build()
                )
            }
        }
        
        // приостанавливаем выполнение до завершения всех корутин и получаем лист 
        // гарантированно пока не будут загружены все картинки
    }.awaitAll()
}
