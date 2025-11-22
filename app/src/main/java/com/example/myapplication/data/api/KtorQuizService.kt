package com.example.myapplication.data.api

import com.example.myapplication.data.model.TriviaResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorQuizService {

    private val client = HttpClient(Android) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    suspend fun fetchQuestions(
        amount: Int,
        category: Int,
        difficulty: String
    ): TriviaResponse {
        val url =
            "https://opentdb.com/api.php?amount=$amount&category=$category&difficulty=$difficulty&type=multiple"
        return client.get(url).body()
    }
}
