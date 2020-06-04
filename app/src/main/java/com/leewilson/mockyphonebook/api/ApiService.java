package com.leewilson.mockyphonebook.api;

import retrofit2.Call;
import retrofit2.http.GET;

import com.leewilson.mockyphonebook.model.ApiResponse;

public interface ApiService {

    @GET("v2/581335f71000004204abaf83")
    Call<ApiResponse> getContacts();
}
