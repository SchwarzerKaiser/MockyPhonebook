package com.leewilson.mockyphonebook.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.leewilson.mockyphonebook.api.ApiService;
import com.leewilson.mockyphonebook.ui.MainViewModel;

import javax.inject.Inject;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private ApiService mService;

    @Inject
    public MainViewModelFactory(ApiService service) {
        mService = service;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == MainViewModel.class) {
            return (T) new MainViewModel(mService);
        }
        return null;
    }
}
