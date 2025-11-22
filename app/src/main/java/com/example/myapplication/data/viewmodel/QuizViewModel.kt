package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.db.QuizResult
import com.example.myapplication.data.model.HistoryItem
import com.example.myapplication.data.model.Question
import com.example.myapplication.data.repository.QuizRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class QuizUiState(
    val isLoading: Boolean = false,
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val score: Int = 0,
    val error: String? = null,
    val currentCategory: String? = null,
    val currentDifficulty: String? = null
)

class QuizViewModel(
    private val repository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    // 🔥 Historique exposé automatiquement via stateIn()
    val history: StateFlow<List<HistoryItem>> = repository
        .getHistory()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // -------------------------------------------------
    //                Charger un quiz
    // -------------------------------------------------
    fun loadQuiz(amount: Int, category: Int, difficulty: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

                val questions = repository.loadQuestions(amount, category, difficulty)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    questions = questions,
                    currentIndex = 0,
                    score = 0,
                    currentCategory = category.toString(),
                    currentDifficulty = difficulty
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erreur inconnue"
                )
            }
        }
    }

    // -------------------------------------------------
    //            Répondre à une question
    // -------------------------------------------------
    fun answer(isCorrect: Boolean) {
        val state = _uiState.value
        if (state.currentIndex >= state.questions.size) return

        _uiState.value = state.copy(
            score = if (isCorrect) state.score + 1 else state.score,
            currentIndex = state.currentIndex + 1
        )
    }

    // -------------------------------------------------
    //           Enregistrer la partie jouée
    // -------------------------------------------------
    fun saveCurrentResult() {
        val state = _uiState.value
        if (state.questions.isEmpty()) return

        viewModelScope.launch {
            val result = QuizResult(
                date = System.currentTimeMillis(),
                score = state.score,
                category = state.currentCategory,
                difficulty = state.currentDifficulty
            )
            repository.saveResult(result)
        }
    }
}
