package com.example.android.myaddressbook

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.android.myaddressbook.model.Contact
import com.google.gson.Gson
import kotlinx.android.synthetic.main.contact_list_item.view.*
import kotlinx.android.synthetic.main.input_contact_dialog.view.*
import java.text.SimpleDateFormat
import java.util.ArrayList


class ContactsAdapter(var items: List<Contact> = listOf(), context: Context): RecyclerView.Adapter<ViewHolder>(), TextWatcher {


    private lateinit var mContacts: ArrayList<Contact>

    private lateinit var mPrefs: SharedPreferences

    private lateinit var mFirstNameEdit: EditText
    private lateinit var mLastNameEdit: EditText
    private lateinit var mEmailEdit: EditText

    private var mEntryValid = false
    private var context = context
    enum class ItemType{
        DEFAULT, EMPTY
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun afterTextChanged(editable: Editable) {
        val notEmpty: TextView.() -> Boolean = { text.isNotEmpty() }
        val isEmail: TextView.() -> Boolean = { Patterns.EMAIL_ADDRESS.matcher(text).matches() }

        mEntryValid = mFirstNameEdit.validateWith(validator = notEmpty) and
                mLastNameEdit.validateWith(validator = notEmpty) and
                mEmailEdit.validateWith(validator = isEmail)
    }

    fun setNewList(list: List<Contact>){
        items = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = if(items.isNotEmpty())
        ItemType.DEFAULT.ordinal
    else ItemType.EMPTY.ordinal


    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.contact_list_item, parent, false)
        return ContactViewHolder(view)
    }


    override fun onBindViewHolder(
            holder: ViewHolder, position: Int) {
        if (holder is ContactViewHolder) {
            val (firstName, lastName, email) = mContacts[position]
            val fullName = "$firstName $lastName"
            holder.nameLabel.text = fullName
            holder.emailLabel.text = email
        }
    }

    override fun getItemCount(): Int {
        return mContacts.size
    }



    internal inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameLabel: TextView = itemView.textview_name
        var emailLabel: TextView = itemView.textview_email

        init {

            itemView.setOnClickListener {showAddContactDialog(adapterPosition) }
        }
    }

    @SuppressLint("InflateParams")
    private fun showAddContactDialog(contactPosition: Int) {
        // Inflates the dialog view
        val dialogView = LayoutInflater.from(context)
                .inflate(R.layout.input_contact_dialog, null)

        mFirstNameEdit = dialogView.edittext_firstname
        mLastNameEdit = dialogView.edittext_lastname
        mEmailEdit = dialogView.edittext_email

        // Listens to text changes to validate after each key press
        mFirstNameEdit.addTextChangedListener(this)
        mLastNameEdit.addTextChangedListener(this)
        mEmailEdit.addTextChangedListener(this)

        // Checks if the user is editing an existing contact
        val editing = contactPosition > -1

        val dialogTitle = if (editing)
            context.getString(R.string.edit_contact)
        else
            context.getString(R.string.new_contact)

        // Builds the AlertDialog and sets the custom view. Pass null for
        // the positive and negative buttons, as you will override the button
        // presses manually to perform validation before closing the dialog
        val builder = AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle(dialogTitle)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, null)

        val dialog = builder.show()

        // If the contact is being edited, populates the EditText with the old
        // information
        if (editing) {
            val (firstName, lastName, email) = mContacts[contactPosition]
            mFirstNameEdit.setText(firstName)
            mFirstNameEdit.isEnabled = false
            mLastNameEdit.setText(lastName)
            mLastNameEdit.isEnabled = false
            mEmailEdit.setText(email)
        }
        // Overrides the "Save" button press and check for valid input
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            // If input is valid, creates and saves the new contact,
            // or replaces it if the contact is being edited
            if (mEntryValid) {
                if (editing) {
                    val editedContact = mContacts[contactPosition]
                    editedContact.email = mEmailEdit.text.toString()
                    mContacts[contactPosition] = editedContact
                    this.notifyItemChanged(contactPosition)
                } else {
                    val newContact = Contact(

                            mFirstNameEdit.text.toString(),
                            mLastNameEdit.text.toString(),
                            mEmailEdit.text.toString()
                    )

                    mContacts.add(newContact)
                    this.notifyItemInserted(mContacts.size)
                }
                saveContacts()
                dialog.dismiss()
            } else {
                // Otherwise, shows an error Toast
                Toast.makeText(context,
                        R.string.contact_not_valid,
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveContacts() {
        val editor = mPrefs.edit()
        editor.clear()
        val contactSet = mContacts.map { Gson().toJson(it) }.toSet()
        editor.putStringSet("contact_key", contactSet)
        editor.apply()
    }
    class EmptyViewHolder(itemView: View): ViewHolder(itemView)

}