package com.example.todoapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.Model.Todo

@Database(entities = [Todo::class] , version = 1 , exportSchema = false)
abstract class TodoDatabase():RoomDatabase() {

    abstract fun getDAO(): TodoDAO

}