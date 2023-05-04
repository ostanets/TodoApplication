package com.ostanets.todoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ostanets.todoapp.domain.NoteRepository
import com.ostanets.todoapp.models.LocalDateConverter
import com.ostanets.todoapp.models.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateConverter::class)
abstract class TodoAppDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteRepository

    companion object {

        private const val DATABASE_NAME = "todoapp"

        @Volatile
        private var INSTANCE: TodoAppDatabase? = null

        fun getDatabase(context: Context): TodoAppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoAppDatabase::class.java,
                    DATABASE_NAME
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}