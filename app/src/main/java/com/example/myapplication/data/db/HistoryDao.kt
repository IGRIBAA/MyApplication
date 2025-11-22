package com.example.myapplication.data.db

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("SELECT * FROM QuizResult ORDER BY date DESC")
    fun getHistory(): Flow<List<QuizResult>>
}
