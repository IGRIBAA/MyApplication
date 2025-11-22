package com.example.myapplication.ui.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.HistoryItem
import com.example.myapplication.ui.theme.*
import com.example.myapplication.viewmodel.QuizViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: QuizViewModel,
    onBackToHome: () -> Unit
) {
    val history by viewModel.history.collectAsState()

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Historique",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary
                    )
                },
                actions = {
                    IconButton(onClick = onBackToHome) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Accueil",
                            tint = BlueAccent
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = BlueAccent
                )
            )
        }
    ) { innerPadding ->

        if (history.isEmpty()) {
            // État vide : on prend TOUT l’écran → pas de problème de scroll
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tu n’as pas encore joué de parties.",
                    color = TextSecondary
                )
            }
        } else {
            // Liste scrollable sur tout l'écran
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(history) { item ->
                    HistoryRow(item)
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
                text = "Catégorie : ${item.category} | Difficulté : ${item.difficulty}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}
