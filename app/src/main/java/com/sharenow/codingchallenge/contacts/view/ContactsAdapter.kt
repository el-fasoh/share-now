package com.sharenow.codingchallenge.contacts.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sharenow.codingchallenge.R
import com.sharenow.codingchallenge.contacts.data.Contact
import com.sharenow.codingchallenge.databinding.ContactItemBinding

/**
 * Adapter for recycler view that displays contacts.
 */
class ContactsAdapter : ListAdapter<Contact, ContactsAdapter.ViewHolder>(ContactsDiffUtil()),
    Filterable {

    private val originalList = mutableListOf<Contact>()
    private var filteredList = mutableListOf<Contact>()

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

    fun submitContacts(contacts: List<Contact>) {
        originalList.clear()
        originalList.addAll(contacts)
        submitList(originalList)
        notifyDataSetChanged()
    }

    class ContactsDiffUtil : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.equals(newItem)
        }
    }

    fun search(s: String?) {
        filter.filter(s)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterResults.values = originalList
                } else {
                    filteredList.clear()
                    filteredList.addAll(
                        originalList.filter {
                            it.email.lowercase().contains(charSearch) || it.name.lowercase()
                                .contains(charSearch)
                        }
                    )
                    filterResults.values = filteredList
                }
                return  filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val endProd = results?.values as ArrayList<Contact>
                submitList(endProd.toList())
                notifyDataSetChanged()
            }
        }

    }
}
