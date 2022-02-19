package com.id.mindtodo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.id.mindtodo.data.model.ToDoTask

@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}