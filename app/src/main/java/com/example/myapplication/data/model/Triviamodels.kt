package com.example.myapplication.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ----- Réponse brute de l’API -----

@Serializable
data class TriviaResponse(
    @SerialName("response_code")
    val responseCode: Int,
    val results: List<TriviaQuestionDto>
)

@Serializable
data class TriviaQuestionDto(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    @SerialName("correct_answer")
    val correctAnswer: String,
    @SerialName("incorrect_answers")
    val incorrectAnswers: List<String>
)

// ----- Modèle pour l’UI -----

data class Question(
    val question: String,
    val correctAnswer: String,
    val answers: List<String>
)

fun TriviaQuestionDto.toDomain(): Question =
    Question(
        question = question,
        correctAnswer = correctAnswer,
        answers = (incorrectAnswers + correctAnswer).shuffled()
    )

// ----- Modèle pour l’historique -----
// Supprimé, car le modèle HistoryItem existe déjà dans HistoryItem.kt (Room)
