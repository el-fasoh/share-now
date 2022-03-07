package com.sharenow.codingchallenge.contacts.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sharenow.codingchallenge.R
import com.sharenow.codingchallenge.contacts.data.Contact
import com.sharenow.codingchallenge.databinding.ContactItemBinding

/**
 * Adapter for recycler view that displays contacts.
 */
class ContactsAdapter : ListAdapter<Contact, ContactsAdapter.ViewHolder>(ContactsAdapter.ContactsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * View holder for a contact.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ContactItemBinding.bind(itemView)

        /**
         * Bind data to the view.
         * @param contact Contact data to be bound.
         */
        fun bind(contact: Contact) {
            with(binding) {
                name.text = contact.name
                email.text = contact.email
            }
        }
    }

    class ContactsDiffUtil : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.equals(newItem)
        }
    }
}
