package com.example.rickandmorty.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import coil.ImageLoader
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

// централизованная конфигурация okxHttp клиентов для разных задач 
// разделяет настройки API и разных изображений
// оптимизирует использование сетевых ресурсов 
// повышает устойчивость к ошибкам


internal object OkhttpConfig {

    const val API_CONNECTION_TIMEOUT = 30L
    const val API_READ_TIMEOUT = 30L
    const val API_WRITE_TIMEOUT = 30L
    
    const val IMAGE_CONNECT_TIMEOUT = 15L
    const val IMAGE_READ_TIMEOUT = 60L
    const val IMAGE_WRITE_TIMEOUT = 30L

    fun apiConnectionPull(): ConnectionPool = ConnectionPool(
        maxIdleConnections = 5, // Сколько соединений может быть ?: 5 by default
        keepAliveDuration = 5, // Сколько живут одновременно
        timeUnit = TimeUnit.MINUTES // Понятно 
    )

    fun imageConnectionPull(): ConnectionPool = ConnectionPool(
        maxIdleConnections = 8, // Сколько соединений может быть ?: 5 by default
        keepAliveDuration = 5, // Сколько живут одновременно
        timeUnit = TimeUnit.MINUTES // Понятно 
    )
    
    fun apiClientBuilder(
        connectionPool: ConnectionPool
    ) : OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectionPool(connectionPool)
            .connectTimeout(API_CONNECTION_TIMEOUT, unit = TimeUnit.SECONDS)
            .readTimeout(API_READ_TIMEOUT, unit = TimeUnit.SECONDS)
            .writeTimeout(API_WRITE_TIMEOUT, unit = TimeUnit.SECONDS)
            
            .dispatcher(Dispatcher().apply {
                maxRequests = 8
                maxRequestsPerHost = 5
            })
            
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
    }
    
    
    @SuppressLint("SuspiciousIndentation")
    fun imageClientBuilder (
        context: Context
    ) : OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
            .connectionPool(connectionPool = imageConnectionPull())
            .protocols(listOf(Protocol.HTTP_1_1))
            .connectTimeout(IMAGE_CONNECT_TIMEOUT, unit = TimeUnit.SECONDS)
            .readTimeout(IMAGE_READ_TIMEOUT, unit = TimeUnit.SECONDS)
            .writeTimeout(IMAGE_WRITE_TIMEOUT, unit = TimeUnit.SECONDS)
            .dispatcher(Dispatcher().apply {
                maxRequests = 12
                maxRequestsPerHost = 4
            })
            .addInterceptor(RetryInterceptor(3))
        
            if (context.isDebuggable()) {
                builder.addInterceptor (HttpLoggingInterceptor().apply { 
                    level = HttpLoggingInterceptor.Level.BASIC
                })
            }
        return builder
    }
     
    private fun Context.isDebuggable() : Boolean {
        return (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }
    
}

