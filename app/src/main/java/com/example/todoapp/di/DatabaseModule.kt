package com.example.todoapp.di

import android.app.Application
import androidx.room.Room
import com.example.todoapp.Repository.TodoRepository
import com.example.todoapp.Repository.TodoRepositoryImpl
import com.example.todoapp.data.room.TodoDAO
import com.example.todoapp.data.room.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(application: Application):TodoDatabase =
        Room.databaseBuilder(application , TodoDatabase::class.java , "TodoDatabase")
            .fallbackToDestructiveMigration() //it is usefull when migrating from one version to another
            .build()

    @Provides
    fun providesDAO(db:TodoDatabase):TodoDAO = db.getDAO()

    @Provides
    @Singleton
    fun providesRepository(dao : TodoDAO) = TodoRepositoryImpl(dao) as TodoRepository
}