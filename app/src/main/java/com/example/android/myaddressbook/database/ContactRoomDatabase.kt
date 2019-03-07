package com.example.android.myaddressbook.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.android.myaddressbook.model.Contact


@Database(entities = [ Contact::class ], version = 1 )
abstract class ContactRoomDatabase : RoomDatabase() {
}