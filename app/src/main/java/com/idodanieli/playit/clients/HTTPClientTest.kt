package com.idodanieli.playit.clients

import org.junit.Test

class HTTPClientTest {

    @Test
    fun testGet() {
        val client = HTTPClient("http://192.168.1.33:5000")

        client.get("/blah")
    }
}