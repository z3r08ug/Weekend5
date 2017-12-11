package com.example.chris.weekend5.di.module;


import com.example.chris.weekend5.data.remote.RemoteDataSource;
import com.example.chris.weekend5.view.maps.MapsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by singh on 12/7/17.
 */

@Module
public class MapsModule
{


    @Provides
    MapsPresenter providesMapsPresenter(RemoteDataSource remoteDataSource)
    {
        return new MapsPresenter(remoteDataSource);
    }

}
