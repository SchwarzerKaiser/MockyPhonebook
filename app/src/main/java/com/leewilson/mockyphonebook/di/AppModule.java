package com.leewilson.mockyphonebook.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leewilson.mockyphonebook.util.Constants;
import com.leewilson.mockyphonebook.api.ApiService;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
class AppModule {

    @Provides
    Gson provideGsonConfig() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Provides
    Retrofit provideRetrofit(Gson gsonConfig){
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gsonConfig))
                .build();
    }

    @Provides
    ApiService provideApiService(Retrofit retrofitInstance) {
        return retrofitInstance.create(ApiService.class);
    }
}
