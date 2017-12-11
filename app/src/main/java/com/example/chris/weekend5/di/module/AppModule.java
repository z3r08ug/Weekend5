package com.example.chris.weekend5.di.module;


import com.example.chris.weekend5.data.remote.RemoteDataSource;

import dagger.Module;
import dagger.Provides;

/**
 * Created by singh on 12/7/17.
 */

@Module
public class AppModule
{

    String BaseURL;
    String apiKey;

    public AppModule(String baseURL, String apiKey)
    {
        BaseURL = baseURL;
        this.apiKey = apiKey;
    }

    @Provides
    RemoteDataSource providesRemoteDataSource()
    {
        return new RemoteDataSource(BaseURL, apiKey);
    }

}
