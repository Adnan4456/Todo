package com.example.todoapp.Repository

import androidx.paging.PagingSource
import com.example.todoapp.Model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    suspend fun insert(todo:Todo)
//    fun getAllTodo(): Flow<List<Todo>>
    fun getAllTodo(): PagingSource<Int, Todo>
}