package com.idodanieli.playit.clients

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
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

        val responseCode = conn.send()
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return conn.readResponse()
        } else {
            throw Exception("HTTP GET request failed with status code: $responseCode")
        }
    }

    fun post(uri: String, body: String, params: Map<String, String>? = null): String {
        val url = url(uri, params)
        val conn = init(url)

        conn.requestMethod = METHOD_POST
        conn.setRequestProperty(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON)
        conn.setRequestBody(body)

        val responseCode = conn.send()
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return conn.readResponse()
        } else {
            throw Exception("HTTP POST request failed with status code: $responseCode")
        }
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

// OutputStream object that allows you to write data
// to the server when making a request with methods like POST or PUT.
private fun HttpURLConnection.setRequestBody(body: String) {
    doOutput = true

    val outputStream = DataOutputStream(outputStream)
    outputStream.writeBytes(body)
    outputStream.flush()
    outputStream.close()
}

// send the request in the connection ; returns status-code
private fun HttpURLConnection.send(): Int {
    return responseCode
}

// readResponse from a connection ( must be after a request has be sent )
private fun HttpURLConnection.readResponse(): String {
    val reader = BufferedReader(InputStreamReader(inputStream))
    val response = StringBuilder()

    var line: String?
    while (reader.readLine().also { line = it } != null) {
        response.append(line)
    }
    reader.close()

    return response.toString()
}