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
import androidx.lifecycle.ViewModelProviders
import com.example.android.myaddressbook.model.Contact
import com.google.gson.Gson
import kotlinx.android.synthetic.main.contact_list_item.view.*
import kotlinx.android.synthetic.main.input_contact_dialog.view.*
import java.text.SimpleDateFormat
import java.util.ArrayList


class ContactsAdapter(var items: List<Contact> = listOf(), context: Context, val action: (Int) -> Unit ): RecyclerView.Adapter<ViewHolder>(), TextWatcher {


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
            val (firstName, lastName, email) = items[position]
            val fullName = "$firstName $lastName"
            holder.nameLabel.text = fullName
            holder.emailLabel.text = email
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }



    internal inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameLabel: TextView = itemView.textview_name
        var emailLabel: TextView = itemView.textview_email

        init {

            itemView.setOnClickListener {
                action(adapterPosition)
                }
        }
    }

    @SuppressLint("InflateParams")

    class EmptyViewHolder(itemView: View): ViewHolder(itemView)

}