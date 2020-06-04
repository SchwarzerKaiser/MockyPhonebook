package com.leewilson.mockyphonebook.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {

    @SerializedName("contacts")
    @Expose
    private List<Contact> contacts;

    @Override
    public String toString() {
        return "Response{" +
                "contacts=" + contacts +
                '}';
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
