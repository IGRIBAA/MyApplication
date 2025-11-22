package com.example.myapplication.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.*
import com.example.myapplication.viewmodel.QuizViewModel
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onQuizFinished: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val currentQuestion = uiState.questions.getOrNull(uiState.currentIndex)

    // État local pour la sélection
    var selectedAnswer by remember(uiState.currentIndex) { mutableStateOf<String?>(null) }
    var isAnswerCorrect by remember(uiState.currentIndex) { mutableStateOf<Boolean?>(null) }
    var hasAnswered by remember(uiState.currentIndex) { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp, vertical = 24.dp) // padding haut + côtés
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = PurplePrimary
            )
            return@Box
        }

        if (currentQuestion == null) {
            // Plus de questions -> on termine
            LaunchedEffect(Unit) {
                viewModel.saveCurrentResult()
                onQuizFinished()
            }
            return@Box
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --------- Header : progression + score ---------
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Question ${uiState.currentIndex + 1} / ${uiState.questions.size}",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )

                LinearProgressIndicator(
                    progress = (uiState.currentIndex + 1).toFloat() / uiState.questions.size.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = PurplePrimary,
                    trackColor = CardDark
                )

                Text(
                    text = "Score : ${uiState.score}",
                    style = MaterialTheme.typography.labelLarge,
                    color = BlueAccent
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --------- Question ---------
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = currentQuestion.question,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --------- Réponses ---------
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                currentQuestion.answers.forEach { answer ->
                    val backgroundColor = when {
                        !hasAnswered -> CardDark
                        // bonne réponse, toujours en vert quand on a répondu
                        answer == currentQuestion.correctAnswer -> GreenCorrect
                        // réponse sélectionnée mais fausse
                        answer == selectedAnswer && isAnswerCorrect == false -> RedWrong
                        else -> CardDark
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !hasAnswered) {
                                // On ne traite la réponse qu'une seule fois
                                if (!hasAnswered) {
                                    selectedAnswer = answer
                                    val correct = answer == currentQuestion.correctAnswer
                                    isAnswerCorrect = correct
                                    hasAnswered = true
                                    viewModel.registerAnswer(correct)
                                }
                            },
                        colors = CardDefaults.cardColors(containerColor = backgroundColor),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = answer,
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary
                        )
                    }
                }
            }

            // --------- Explication / correction ---------
            if (hasAnswered) {
                Spacer(modifier = Modifier.height(12.dp))

                val explanationText = if (isAnswerCorrect == true) {
                    "Bien joué ! Tu as choisi la bonne réponse."
                } else {
                    // On n'a pas de vraie explication depuis l’API,
                    // donc on affiche au moins la bonne réponse.
                    "Ce n’est pas correct. La bonne réponse était : \"${currentQuestion.correctAnswer}\""
                }

                Text(
                    text = explanationText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isAnswerCorrect == true) GreenCorrect else TextSecondary
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }

            // --------- Bouton "Question suivante" ---------
            Button(
                onClick = {
                    if (uiState.currentIndex == uiState.questions.lastIndex) {
                        // dernière question -> on sauvegarde + nav vers résultats
                        viewModel.saveCurrentResult()
                        onQuizFinished()
                    } else {
                        // passe à la suivante et reset l'état local
                        viewModel.goToNextQuestion()
                        selectedAnswer = null
                        isAnswerCorrect = null
                        hasAnswered = false
                    }
                },
                enabled = hasAnswered, // on force à répondre avant
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurplePrimary,
                    disabledContainerColor = CardDark
                )
            ) {
                Text(
                    text = if (uiState.currentIndex == uiState.questions.lastIndex)
                        "Terminer le quiz"
                    else
                        "Question suivante",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextPrimary
                )
            }
        }
    }
}
