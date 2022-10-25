package com.example.todoapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "todoTable")
data class Todo(

    val title: String,
    val description: String,
    var status : Boolean,
    val createdDate:String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null
}