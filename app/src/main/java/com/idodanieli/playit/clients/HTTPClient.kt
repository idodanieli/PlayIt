package com.idodanieli.playit.clients

import java.net.HttpURLConnection
import java.net.URL

class HTTPClient(private val address: String) {

    companion object {
        private const val METHOD_GET = "GET"
        private const val METHOD_POST = "POST"

        private const val HEADER_CONTENT_TYPE = "Content-Type"
        private const val CONTENT_TYPE_JSON = "application/json"
    }

    // initializes the connection ( we are not connected yet )
    private fun init(url: URL): HttpURLConnection {
        return url.openConnection() as HttpURLConnection
    }

    fun get(uri: String, params: Map<String, String>? = null): String {
        val url = url(uri, params)
        val conn = init(url)
        conn.requestMethod = METHOD_GET

        conn.send()

        return conn.readResponse()
    }

    fun post(uri: String, body: String, params: Map<String, String>? = null): String {
        val url = url(uri, params)
        val conn = init(url)

        conn.requestMethod = METHOD_POST
        conn.setRequestProperty(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON)
        conn.setRequestBody(body)

        conn.send()

        return conn.readResponse()
    }

    fun url(uri: String, queryParams: Map<String, String>?): URL {
        val urlBuilder = StringBuilder(address + uri)

        queryParams?.let {
            if (queryParams.isNotEmpty()) {
                urlBuilder.append("?")
                queryParams.forEach { (key, value) ->
                    urlBuilder.append("$key=$value&")
                }
                urlBuilder.deleteCharAt(urlBuilder.length - 1) // Remove the trailing '&'
            }
        }

        return URL(urlBuilder.toString())
    }
}
