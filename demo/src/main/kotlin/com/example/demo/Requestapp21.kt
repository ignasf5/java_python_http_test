package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@RestController
class ApiController21(
    @Value("\${api.url}")
    private val apiUrl: String,

    @Value("\${num.calls}")
    private val numCalls: Int
) {

    // URL of the Flask API running locally
//    private val apiUrl = "http://localhost:5000"

    @GetMapping("/test-api21")
    fun testApi(): String {
        val startTime = System.currentTimeMillis()
        val currentThread = Thread.currentThread().name
        val startDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        println("Received request to /test-api. Timestamp: $startDateTime, Thread: $currentThread")

        val executor = Executors.newVirtualThreadPerTaskExecutor() // Java 21 virtual threads

        // URLs to hit in the Flask API
        val urls = listOf("$apiUrl/books/1", "$apiUrl/books/2")

        // StringBuilder to store response details
        val responseBuilder = StringBuilder()

        // Count for each GET method call
        val count = AtomicInteger(0)

        // Loop to execute the request 1000 times
        repeat(numCalls) {
            val currentCount = count.incrementAndGet()

            // Submit tasks for each URL
            urls.forEach { url ->
                executor.submit {
                    val start = System.currentTimeMillis()
                    val response = RestTemplate().getForObject(url, String::class.java)
                    val end = System.currentTimeMillis()
                    val duration = end - start
                    val thread = Thread.currentThread().name
                    val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    val responseData = "$currentCount. Thread: $thread, URL: $url, Duration: $duration ms, Time: $currentDateTime"
                    synchronized(responseBuilder) {
                        responseBuilder.append(responseData)
                        responseBuilder.append("\n") // Add new line for each response
                    }
                }
            }
        }

        // Shutdown the executor after all tasks are submitted
        executor.shutdown()

        // Wait for all tasks to complete
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }

        val endTime = System.currentTimeMillis()
        val endDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val duration = endTime - startTime
        println("Completed request to /test-api. Duration: $duration ms")

        // Append overall duration to the response
        synchronized(responseBuilder) {
            responseBuilder.append("Total duration: $duration ms\n")
            responseBuilder.append("End time: $endDateTime")
        }

        return responseBuilder.toString()
    }
}
