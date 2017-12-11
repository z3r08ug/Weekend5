package com.example.chris.weekend5.di.module;

import com.example.chris.weekend5.data.remote.RemoteDataSource;
import com.example.chris.weekend5.view.placedetail.DetailsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chris on 12/9/2017.
 */

@Module
public class DetailsModule
{
    @Provides
    DetailsPresenter providesDetailsPresenter(RemoteDataSource remoteDataSource)
    {
        return new DetailsPresenter(remoteDataSource);
    }
}
