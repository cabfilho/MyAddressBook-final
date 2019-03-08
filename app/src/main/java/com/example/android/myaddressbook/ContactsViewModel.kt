package com.example.android.myaddressbook

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.android.myaddressbook.database.ContactRoomDatabase
import com.example.android.myaddressbook.database.Repository.ContactRepository
import com.example.android.myaddressbook.model.Contact

class ContactsViewModel (application: Application): AndroidViewModel(application) {


        // não é privado, porque será observado
        val contactList: LiveData<List<Contact>>
        private val repository: ContactRepository


        init {
            val database = ContactRoomDatabase.getInstance(application.applicationContext)
            val dao = database.contactDao()
            repository = ContactRepository(dao)
            contactList = repository.listContact()
        }


        fun insert(contact: Contact){
            DoAsync {
                repository.insert(contact)
            }
        }

        fun delete(contact: Contact){
            DoAsync {
                repository.delete(contact)
            }
        }

        fun deleteAll(){
            DoAsync {
                repository.deleteAll()
            }
        }
        fun update(contact: Contact){
            DoAsync {
                repository.update(contact)
            }
        }

        class DoAsync(val action: ()->Unit): AsyncTask<Unit, Unit, Unit>() {

            init {
                execute()
            }

            override fun doInBackground(vararg params: Unit?) {
                action()
            }
        }


}