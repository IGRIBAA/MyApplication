package com.example.myapplication.data.repository

import com.example.myapplication.data.api.KtorQuizService
import com.example.myapplication.data.db.QuizDao
import com.example.myapplication.data.db.QuizResult
import com.example.myapplication.data.model.HistoryItem
import com.example.myapplication.data.model.Question
import com.example.myapplication.data.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuizRepository(
    private val quizDao: QuizDao
) {

    // 1) Charger les questions depuis l’API OpenTrivia via Ktor
    suspend fun loadQuestions(
        amount: Int,
        category: Int,
        difficulty: String
    ): List<Question> {
        val response = KtorQuizService.fetchQuestions(
            amount = amount,
            category = category,
            difficulty = difficulty
        )
        // Conversion DTO -> modèle Question pour l’UI
        return response.results.map { it.toDomain() }
    }

    // 2) Sauver un résultat dans Room
    suspend fun saveResult(result: QuizResult) {
        quizDao.insert(result)
    }

    // 3) Récupérer l’historique sous forme de HistoryItem pour l’UI
    fun getHistory(): Flow<List<HistoryItem>> =
        quizDao.getAllResults().map { list ->
            list.map { result ->
                HistoryItem(
                    id = result.id,
                    date = result.date,
                    score = result.score,
                    category = result.category,     // nullable, géré dans l’UI
                    difficulty = result.difficulty  // nullable aussi
                )
            }
        }
}
