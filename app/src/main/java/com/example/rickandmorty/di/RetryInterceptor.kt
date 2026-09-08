package com.example.rickandmorty.di

// мы реализуем интерфейс, который будет нам помогать с помощью своего метода который мы override 
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

internal class RetryInterceptor (
    private val maxRetries: Int,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request  = chain.request()
        var attempt = 0
        var lastException : IOException? = null
        
        while (attempt <= maxRetries) {
            try {
                // передаём далее по цепочке 
                return chain.proceed(request)
                
            }catch (e: IOException) {
                lastException = e
                attempt++ 
                if (attempt > maxRetries || request.method != "GET") {
                    break
                }
                
            }
        }
            
        throw lastException ?: IOException("Request failed after $maxRetries retries")
        
    }
}