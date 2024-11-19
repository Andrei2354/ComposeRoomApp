package com.example.composeroomapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface TaskDao {
    @Insert suspend fun insert(task: Task)
    @Query("SELECT * FROM tasks") suspend fun getAllTasks(): List<Task>
    @Update suspend fun update(task: Task)
    @Delete suspend fun delete(task: Task) }