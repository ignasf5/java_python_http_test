package com.example.demo

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
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
class Applicationstandart

fun main(args: Array<String>) {
    runApplication<Applicationstandart>(*args)
}

@RestController
class ApiController {

    private val logger = LoggerFactory.getLogger(ApiController::class.java)

    // URL of the Flask API running locally
    private val apiUrl = "http://localhost:5000"

    @GetMapping("/test-api")
    fun testApi(): String {
        val startTime = System.currentTimeMillis()
        val currentThread = Thread.currentThread().name
        val startDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        logger.info("Received request to /test-api. Timestamp: {}, Thread: {}", startDateTime, currentThread)

        val restTemplate = RestTemplate()

        val executor = Executors.newFixedThreadPool(5) // Number of threads
        val responses = mutableListOf<String>()

        // URLs to hit in the Flask API
        val urls = listOf("$apiUrl/books/1", "$apiUrl/books/2")

        // StringBuilder to store response details
        val responseBuilder = StringBuilder()

        // Count for each GET method call
        var count = AtomicInteger(0)

        // Loop to execute the request 1000 times
        repeat(1000) {
            // Increment count for each iteration
            val currentCount = count.incrementAndGet()

            // Submit tasks for each URL
            val futures = urls.map { url ->
                executor.submit<String> {
                    try {
                        val start = System.currentTimeMillis()
                        val response = restTemplate.getForObject(url, String::class.java)
                        val end = System.currentTimeMillis()
                        val duration = end - start
                        val thread = Thread.currentThread().name
                        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        responseBuilder.append("$currentCount. Thread: $thread, URL: $url, Duration: $duration ms, Time: $currentDateTime<br>")
                        response ?: "Error fetching $url"
                    } catch (e: Exception) {
                        "Error: ${e.message}"
                    }
                }
            }

            // Retrieve responses
            futures.forEach { future ->
                try {
                    val response = future.get(5, TimeUnit.SECONDS) // Adjust timeout as needed
                    responses.add(response)
                } catch (e: Exception) {
                    responses.add("Error: ${e.message}")
                }
            }
        }

        executor.shutdown()

        val endTime = System.currentTimeMillis()
        val endDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val duration = endTime - startTime
        logger.info("Completed request to /test-api. Duration: {} ms", duration)

        // Append overall duration to the response
        responseBuilder.append("<br>Total duration: $duration ms<br>")
        responseBuilder.append("End time: $endDateTime")

        return responseBuilder.toString()
    }
}
