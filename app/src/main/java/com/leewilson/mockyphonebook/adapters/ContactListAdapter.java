package com.leewilson.mockyphonebook.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leewilson.mockyphonebook.R;
import com.leewilson.mockyphonebook.model.Contact;
import com.leewilson.mockyphonebook.util.SortConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> implements Filterable {

    private SortConfig mSortConfig;
    private List<Contact> dataFull = new ArrayList<>();
    private List<Contact> data = new ArrayList<>();

    public ContactListAdapter() {
        mSortConfig = SortConfig.LAST_NAME;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.phoneNumber.setText(data.get(position).getPhoneNumber());
        holder.name.setText(data.get(position).getName());
        holder.address.setText(data.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void submitList(List<Contact> contacts) {
        dataFull.addAll(contacts);
        data.addAll(contacts);
        sortContacts();
    }

    /**
     * Sort Contact items based on SortConfig definition.
     * @param sortConfig
     */
    public void sort(SortConfig sortConfig) {
        mSortConfig = sortConfig;
        sortContacts();
    }

    private void sortContacts() {
        Collections.sort(data, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                switch (mSortConfig) {
                    case LAST_NAME:
                        String c1Surname = getSurnameFromNameField(c1.getName());
                        String c2Surname = getSurnameFromNameField(c2.getName());
                        return c1Surname.compareTo(c2Surname);
                    case FIRST_NAME:
                        String c1FirstName = getFirstNameFromNameField(c1.getName());
                        String c2FirstName = getFirstNameFromNameField(c2.getName());
                        return c1FirstName.compareTo(c2FirstName);
                    default:
                        return 0;
                }
            }
        });
        notifyDataSetChanged();
    }

    private String getSurnameFromNameField(String fullName) {
        int spaceIndex = fullName.indexOf(' ');
        return fullName.substring((spaceIndex + 1));
    }

    private String getFirstNameFromNameField(String fullName) {
        int spaceIndex = fullName.indexOf(' ');
        return fullName.substring(0, spaceIndex);
    }

    /**
     * Gets a handler for filtering RecyclerView items asynchronously.
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Contact> filteredContacts = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredContacts.addAll(dataFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Contact contact : dataFull) {
                        if (contact.getName().toLowerCase().contains(filterPattern) ||
                                contact.getAddress().toLowerCase().contains(filterPattern) ||
                                contact.getPhoneNumber().toLowerCase().contains(filterPattern)) {

                            filteredContacts.add(contact);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredContacts;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                data.clear();
                data.addAll((List<Contact>) filterResults.values);
                sortContacts();
            }
        };
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView address;
        TextView phoneNumber;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contact_name);
            address = itemView.findViewById(R.id.contact_address);
            phoneNumber = itemView.findViewById(R.id.contact_phone_number);
        }
    }
}
