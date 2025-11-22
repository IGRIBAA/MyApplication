package com.example.myapplication.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuizResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val score: Int,
    val category: String?,
    val difficulty: String?
)
