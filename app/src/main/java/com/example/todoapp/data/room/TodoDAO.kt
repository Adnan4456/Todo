package com.example.todoapp.data.room

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.example.todoapp.Model.Todo
import kotlinx.coroutines.flow.Flow



@Dao
abstract class TodoDAO : BaseDAO<Todo>{
    /**
     * Get all data from the Data table.
     */
    @Query("SELECT * FROM todoTable")
     abstract fun getData(): PagingSource<Int , Todo>
//    abstract fun getData(): Flow<List<Todo>>

}