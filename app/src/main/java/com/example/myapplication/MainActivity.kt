package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.db.AppDatabase
import com.example.myapplication.data.repository.QuizRepository
import com.example.myapplication.ui.home.HomeScreen
import com.example.myapplication.ui.quiz.QuizScreen
import com.example.myapplication.ui.result.HistoryScreen
import com.example.myapplication.ui.result.ResultScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.QuizViewModel

class MainActivity : ComponentActivity() {

    private val database by lazy { AppDatabase.getInstance(applicationContext) }
    private val repository by lazy { QuizRepository(database.quizDao()) }
    private val quizViewModel by lazy { QuizViewModel(repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    QuizNavHost(quizViewModel)
                }
            }
        }
    }
}

@Composable
fun QuizNavHost(viewModel: QuizViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onStartQuiz = { amount, category, difficulty ->
                    viewModel.loadQuiz(amount, category, difficulty)
                    navController.navigate("quiz")
                },
                onShowHistory = {
                    navController.navigate("history")
                }
            )
        }

        composable("quiz") {
            QuizScreen(
                viewModel = viewModel,
                onQuizFinished = {
                    navController.navigate("result")
                }
            )
        }

        composable("result") {
            ResultScreen(
                viewModel = viewModel,
                onBackToHome = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }

        composable("history") {
            HistoryScreen(
                viewModel = viewModel,
                onBackToHome = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }
    }
}
