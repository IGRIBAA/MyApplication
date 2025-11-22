package com.example.myapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    @Insert
    suspend fun insert(result: QuizResult)

    @Query("SELECT * FROM QuizResult ORDER BY date DESC")
    fun getAllResults(): Flow<List<QuizResult>>
}
