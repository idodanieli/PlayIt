package com.idodanieli.playit

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
    private fun init(uri: String): HttpURLConnection {
        return URL(address + uri).openConnection() as HttpURLConnection
    }

    fun get(uri: String): String {
        val conn = init(uri)
        conn.requestMethod = METHOD_GET

        val responseCode = conn.send()
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return conn.readResponse()
        } else {
            throw Exception("HTTP GET request failed with status code: $responseCode")
        }
    }

    fun post(uri: String, body: String): String {
        val conn = init(uri)

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