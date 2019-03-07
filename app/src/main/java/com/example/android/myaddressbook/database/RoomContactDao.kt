package com.example.android.myaddressbook.database

import android.arch.persistence.room.*
import com.example.android.myaddressbook.model.Contact

@Dao
interface RoomContactDao {
    @get:Query("select * from contact_table")
    val allContact: List<Contact>
    @Insert
    fun insert(contact: Contact)
    @Delete
    fun delete(contact: Contact)
    @Update
    fun update(contact: Contact)
}