package com.leewilson.mockyphonebook.di;

import com.leewilson.mockyphonebook.ui.MainActivity;

import dagger.Component;

@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);
}
