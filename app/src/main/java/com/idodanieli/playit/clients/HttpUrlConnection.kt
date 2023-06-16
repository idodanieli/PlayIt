package com.idodanieli.playit.clients

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.HttpURLConnection

private const val DEFAULT_MAX_RETRIES = 3
private const val DEFAULT_SLEEP_INTERVAL_MILLISECONDS = 500L

class HTTPSendingException(message: String) : Exception(message)

fun HttpURLConnection.send(maxRetries: Int = DEFAULT_MAX_RETRIES): Int {
    var retries = 0
    var code = 0
    var err: HTTPSendingException? = null

    Thread {
        while (retries < maxRetries) {
            try {
                code = _send()
                err = null
                break
            } catch (e: HTTPSendingException) {
                println(e.toString())
                retries++
                Thread.sleep(DEFAULT_SLEEP_INTERVAL_MILLISECONDS)
                err = e
            }
        }
    }.runBlocking()

    err?.let {
        throw it
    }

    return code
}

private fun HttpURLConnection._send(): Int {
    val code: Int

    try {
        code = responseCode
    } catch (e: ConnectException) {
        throw HTTPSendingException("Failed to connect: ${e.message}")
    }

    if (code != HttpURLConnection.HTTP_OK) {
        throw HTTPSendingException("HTTP request failed with status code: $responseCode")
    }

    return code
}

fun HttpURLConnection.setRequestBody(body: String) {
    val thread = Thread {
        doOutput = true

        // OutputStream object that allows you to write data
        // to the server when making a request with methods like POST or PUT.
        val outputStream = DataOutputStream(outputStream)
        outputStream.writeBytes(body)
        outputStream.flush()
        outputStream.close()
    }

    thread.start()
    thread.join()
}

fun HttpURLConnection.readResponse(): String {
    val response = StringBuilder()

    val thread = Thread { // To avoid NetworkOnMainThreadException
        val reader = BufferedReader(InputStreamReader(inputStream))

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        reader.close()
    }

    thread.start()
    thread.join() // wait for thread to finish

    return response.toString()
}

fun Thread.runBlocking() {
    start()
    join() // wait for thread to finish
}