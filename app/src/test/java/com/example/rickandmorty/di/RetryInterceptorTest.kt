package com.example.rickandmorty.di

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class RetryInterceptorTest {
    
    private lateinit var server: MockWebServer
    
    @Before
    fun setUp(){
        server = MockWebServer()
        server.start()
    }
    
    @After
    fun tearDown () {
        server.shutdown()
    }
    
    @Test
    fun `retries get request after socket value` () {
        
        server.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        )
        
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("ok")
        )
        
        val client = OkHttpClient.Builder()
            .addInterceptor (RetryInterceptor(2))
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
        
//        val response = client.newCall(Request.Builder().url("/avatar/1.jpeg").get().build() ).execute()  

        val response = client.newCall(
            Request.Builder().url(server.url("/avatar/1.jpeg")).get().build(),
        ).execute()
        
        assertTrue(response.isSuccessful)
        assertEquals("ok", response.body?.string())
        assertEquals(2 , server.requestCount)
        
    } 
    
}