package com.example.todoapp.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.todoapp.Model.Todo
import com.example.todoapp.Repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel
    @Inject
    constructor(private val todoRepository: TodoRepository)
    : ViewModel() {

    init {
//        getAllTodo()
    }

    //to store data use MutableState in jetpack compose
//        val response:MutableState<List<Todo>> = mutableStateOf(listOf())

        fun insert(todo: Todo) = viewModelScope.launch {
            //it is a suspended function therefore call in viewModelScope coroutine
            todoRepository.insert(todo)
        }

    val response = Pager(
            PagingConfig(pageSize = 10)
        ){
        todoRepository.getAllTodo()
    }.flow.cachedIn(viewModelScope)




    /*
        fun getAllTodo() = viewModelScope.launch {

            todoRepository.getAllTodo()
                .catch { e->
                    Log.d("MainActivity", "Exception: ${e.message}")
                }.collect{
                    response.value= it
                }
        }*/
    }