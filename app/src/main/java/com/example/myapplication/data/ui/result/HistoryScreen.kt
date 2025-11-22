package com.example.myapplication.ui.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.HistoryItem
import com.example.myapplication.ui.theme.*
import com.example.myapplication.viewmodel.QuizViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.remember

@Composable
fun HistoryScreen(
    viewModel: QuizViewModel,
    onBackToHome: () -> Unit
) {
    // on récupère directement la liste exposée par le ViewModel
    val history by viewModel.history.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Historique",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )

                TextButton(onClick = onBackToHome) {
                    Text("Accueil")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (history.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tu n’as pas encore joué de parties.",
                        color = TextSecondary
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(history) { item ->
                        HistoryRow(item)
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryRow(item: HistoryItem) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardDark)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = dateFormat.format(Date(item.date)),
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )
            Text(
                text = "Score : ${item.score}",
                style = MaterialTheme.typography.bodyMedium,
                color = BlueAccent,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Catégorie : ${item.category ?: "-"} | Difficulté : ${item.difficulty ?: "-"}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}
