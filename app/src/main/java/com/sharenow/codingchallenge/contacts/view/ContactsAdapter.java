package com.sharenow.codingchallenge.contacts.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sharenow.codingchallenge.R;
import com.sharenow.codingchallenge.contacts.data.Contact;

/**
 * Adapter for recycler view that displays contacts.
 */
public class ContactsAdapter extends ListAdapter<Contact, ContactsAdapter.ViewHolder> {

    public ContactsAdapter() {
        super(new DiffUtil.ItemCallback<Contact>() {

            @Override
            public boolean areItemsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    /**
     * View holder for a contact.
     */
    public static final class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView emailTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name);
            emailTextView = itemView.findViewById(R.id.email);
        }

        /**
         * Bind data to the view.
         * @param contact Contact data to be bound.
         */
        public void bind(Contact contact) {
            nameTextView.setText(contact.getName());
            emailTextView.setText(contact.getEmail());
        }
    }
}
