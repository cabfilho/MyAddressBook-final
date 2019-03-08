package com.example.android.myaddressbook.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.android.myaddressbook.database.Dao.RoomContactDao
import com.example.android.myaddressbook.model.Contact




@Database(entities = [ Contact::class ], version = 2,exportSchema = false )
abstract class ContactRoomDatabase : RoomDatabase() {
    abstract fun contactDao(): RoomContactDao


    companion object {
        private var INSTANCE: ContactRoomDatabase? = null
        const val DBNAME = "ContactDataBase"

        fun getInstance(context: Context):ContactRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ContactRoomDatabase::class.java,
                        DBNAME)
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                instance
            }

        }
    }
}