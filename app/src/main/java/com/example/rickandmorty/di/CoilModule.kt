package com.example.rickandmorty.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoilModule {
    
    @Provides
    @ImageOkHttpClient
    fun provideImageOkHttpClient(
        @ApplicationContext
        context: Context
    ) : OkHttpClient {
        return OkhttpConfig.imageClientBuilder(context).build()
    }
    
    @Provides
    @Singleton
    fun provideImageLoader(
        @ImageOkHttpClient
        okHttpClient: OkHttpClient,
        @ApplicationContext
        context: Context
    ) : ImageLoader {
        return ImageLoader.Builder(context)
            .okHttpClient(okHttpClient)
            .crossfade(true) // Включение анимации плавного перехода между старым и новым изображением 
            .allowHardware(false) // Отключение аппаратного ускорения для нашего изображения
            .respectCacheHeaders(false) // игнорирование http заголовков кеширования  
            .apply {
                if ((context.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0 ) {
                    logger(DebugLogger())
                }
            }
            .memoryCache(  ) {
                MemoryCache.Builder(context).maxSizePercent(0.25).build()
            } // часть из api принимает лямбда выражение которое и возвращает и устанавливает размер кеша для изображений 
            .diskCache () {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizeBytes(50L * 1024 * 1024).build()
            }  // часть из api Image loader который создаёт конфиг для кеша 
            .build()
    }
}