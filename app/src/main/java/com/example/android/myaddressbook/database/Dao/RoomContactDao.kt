package com.example.android.myaddressbook.database.Dao


import androidx.room.*
import androidx.lifecycle.LiveData
import com.example.android.myaddressbook.model.Contact

@Dao
interface RoomContactDao {
    @Query("select * from contact_table")
    fun listAll(): LiveData<List<Contact>>

    @Insert
    fun insert(contact: Contact)

    @Delete
    fun delete(contact: Contact)

    @Update
    fun update(vararg contact: Contact)

    @Query("DELETE FROM contact_table")
    fun deleteAll()
}