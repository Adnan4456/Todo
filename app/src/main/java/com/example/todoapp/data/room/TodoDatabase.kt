package com.example.todoapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.todoapp.Model.Todo
import com.example.todoapp.extension.DateConvertor
import retrofit2.Converter

@Database(entities = [Todo::class] , version = 1 , exportSchema = false)
@TypeConverters(DateConvertor::class)
abstract class TodoDatabase():RoomDatabase() {

    abstract fun getDAO(): TodoDAO
}