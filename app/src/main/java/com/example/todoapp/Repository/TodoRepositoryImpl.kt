package com.example.todoapp.Repository

import androidx.paging.PagingSource
import com.example.todoapp.Model.Todo
import com.example.todoapp.data.room.TodoDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

class TodoRepositoryImpl
    @Inject
    constructor(private val dao: TodoDAO)
    :TodoRepository{

    override suspend fun insert(todo: Todo) {

        //to run function on background IO thread .
        withContext(Dispatchers.IO){
            dao.insert(todo)
        }
    }

    override suspend fun delete(todo: Todo) {
        withContext(Dispatchers.IO){
            dao.delete(todo)
        }
    }

    override suspend fun update(todo: Todo) {
      withContext(Dispatchers.IO){
          dao.update(todo)
      }
    }

    override  fun getAllTodo(): PagingSource<Int, Todo> {
        return dao.getData()
    }
}