package com.example.todoapp.extension

import androidx.room.TypeConverter
import java.util.*

class DateConvertor {

    @TypeConverter
    fun fromDateToLong(value : Date): Long{
        return value.time
    }

    @TypeConverter
    fun LongToDate(value:Long):Date {
        return Date(value)
    }
}