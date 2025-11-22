package com.example.myapplication.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.BackgroundDark
import com.example.myapplication.ui.theme.PurplePrimary
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary
import com.example.myapplication.viewmodel.QuizViewModel

@Composable
fun HomeScreen(
    viewModel: QuizViewModel,
    onStartQuiz: (amount: Int, category: Int, difficulty: String) -> Unit,
    onShowHistory: () -> Unit
) {
    // Définis ici les paramètres de ton “quick quiz”
    val defaultAmount = 10
    val defaultCategory = 18       // Informatique
    val defaultDifficulty = "easy"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        BackgroundDark,
                        BackgroundDark.copy(alpha = 0.95f)
                    )
                )
            )
            .padding(horizontal = 24.dp)
    ) {

        // Contenu principal centré
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Titre
            Text(
                text = "Quiz Trainer",
                style = MaterialTheme.typography.headlineLarge,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sous-titre simple
            Text(
                text = "Teste tes connaissances en quelques questions.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Bouton principal : commencer un quiz
            Button(
                onClick = {
                    onStartQuiz(defaultAmount, defaultCategory, defaultDifficulty)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurplePrimary,
                    contentColor = TextPrimary
                )
            ) {
                Text(text = "Commencer un quiz")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bouton secondaire : historique
            OutlinedButton(
                onClick = onShowHistory,
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = PurplePrimary
                )
            ) {
                Text(text = "Voir l’historique des parties")
            }
        }

        // Petite phrase en bas de l’écran
        Text(
            text = "Astuce : l’objectif est juste de progresser, pas d’être parfait !",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}
