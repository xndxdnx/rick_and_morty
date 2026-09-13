package com.example.rickandmorty.data.remote

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer
import org.junit.After
import org.junit.Before
import org.junit.Test

class ImageNetworkFetcherTest {
    
    lateinit private var server : MockWebServer
    lateinit private var client : OkHttpClient
    
    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        client = OkHttpClient.Builder().build()
    }
    
    @After
    fun tearDown () {
        server.shutdown()    
    }
    
    @Test
    fun `OkHttp downloads jpeg avatar bytes` () {
        val jpegHeader = byteArrayOf(
            0xFF.toByte(),
            0XD8.toByte(),
            0xFF.toByte()            
        )
        val body = Buffer().write(
            jpegHeader + ByteArray(100)
        )
        
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "image/jpeg")
                .setBody(body)
        )
        
        val response = client.newCall(
            Request.Builder()
                .url(server.url("/avatar/1.jpeg"))
                .build()
        ).execute()
        
        assertTrue(response.isSuccessful)
        
        assertEquals("image/jpeg", response.header(name = "Content-Type"))
        
        assertTrue(response.body!!.bytes().size > jpegHeader.size)
    } 
    
}




