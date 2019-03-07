package com.example.android.myaddressbook.database.Repository

import androidx.lifecycle.LiveData
import com.example.android.myaddressbook.database.Dao.RoomContactDao
import com.example.android.myaddressbook.model.Contact

class ContactRepository(private val contactDao: RoomContactDao) {

    fun shouldFetchData(): Boolean{
        return false
    }


    fun listContact(): LiveData<List<Contact>> {

        return contactDao.listAll()

    }


    fun insert(contact: Contact){
        contactDao.insert(contact)
    }

    fun delete(contact: Contact){
        contactDao.delete(contact)
    }

}