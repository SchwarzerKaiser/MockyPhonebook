package com.leewilson.mockyphonebook.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.leewilson.mockyphonebook.api.ApiService;
import com.leewilson.mockyphonebook.model.Contact;
import com.leewilson.mockyphonebook.model.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private ApiService mService;

    private MutableLiveData<List<Contact>> mContacts = new MutableLiveData<List<Contact>>() {
        @Override
        protected void onActive() {
            super.onActive();
            mService.getContacts().enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    ApiResponse apiResponse = response.body();
                    List<Contact> newContacts = apiResponse.getContacts();
                    mContacts.setValue(newContacts);
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    oneShotErrorEvent();
                }
            });
        }
    };

    private MutableLiveData<Boolean> mNetworkErrorOccurred = new MutableLiveData<>(false);

    public MainViewModel(ApiService service) {
        mService = service;
    }

    private void oneShotErrorEvent() {
        mNetworkErrorOccurred.setValue(true);
        mNetworkErrorOccurred.setValue(false);
    }

    public LiveData<List<Contact>> getContacts() {
        return mContacts;
    }

    public LiveData<Boolean> getErrorFlag(){
        return mNetworkErrorOccurred;
    }
}
