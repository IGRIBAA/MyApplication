package com.example.myapplication.data.model

data class HistoryItem(
    val id: Int,
    val date: Long,
    val score: Int,
    val category: String?,
    val difficulty: String?
)
