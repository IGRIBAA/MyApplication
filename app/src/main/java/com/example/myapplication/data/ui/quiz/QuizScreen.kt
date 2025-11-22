package com.example.myapplication.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.*
import com.example.myapplication.viewmodel.QuizViewModel
import androidx.compose.foundation.border

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onQuizFinished: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PurplePrimary)
        }
        return
    }

    if (state.error != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Erreur : ${state.error}",
                color = RedWrong
            )
        }
        return
    }

    val questions = state.questions
    val index = state.currentIndex

    if (index >= questions.size || questions.isEmpty()) {
        // plus de questions → on termine
        onQuizFinished()
        return
    }

    val question = questions[index]
    val progress = (index + 1f) / questions.size.toFloat()

    // Pour colorer les réponses après clic
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isLocked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(BackgroundDark, PurpleDark)
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top bar simple
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${index + 1}/${questions.size}",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = "Score : ${state.score}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BlueAccent,
                    fontWeight = FontWeight.SemiBold
                )
            }

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50)),
                color = PurplePrimary,
                trackColor = CardDark
            )

            // Carte de question
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CardDark
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = question.question,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                question.answers.forEach { answer ->
                    val isCorrect = answer == question.correctAnswer
                    val isSelected = answer == selectedAnswer

                    val backgroundColor =
                        when {
                            !isLocked && isSelected -> CardLight
                            isLocked && isSelected && isCorrect -> GreenCorrect
                            isLocked && isSelected && !isCorrect -> RedWrong
                            else -> CardLight
                        }

                    val borderColor =
                        if (isLocked && isCorrect) GreenCorrect else CardLight

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(backgroundColor)
                            .border(
                                width = 2.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(18.dp)
                            )
                            .clickable(enabled = !isLocked) {
                                selectedAnswer = answer
                                isLocked = true
                                viewModel.answer(isCorrect)
                            }
                            .padding(14.dp)
                    ) {
                        Text(
                            text = answer,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bouton "Suivant"
            Button(
                onClick = {
                    selectedAnswer = null
                    isLocked = false

                    if (index + 1 >= questions.size) {
                        onQuizFinished()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = isLocked
            ) {
                Text(if (index + 1 >= questions.size) "Voir le résultat" else "Question suivante")
            }
        }
    }
}
