package com.example.chris.weekend5;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.example.chris.weekend5.di.component.AppComponent;
import com.example.chris.weekend5.di.component.DaggerAppComponent;
import com.example.chris.weekend5.di.component.DetailsComponent;
import com.example.chris.weekend5.di.component.MapsComponent;
import com.example.chris.weekend5.di.module.AppModule;
import com.example.chris.weekend5.di.module.DetailsModule;
import com.example.chris.weekend5.di.module.MapsModule;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by chris on 12/8/2017.
 */

public class MapsApplication extends Application
{
    private static final String BASE_URL = "http://";
    private static final String API_KEY = "AIzaSyBgFi0vAWqYPVS7VvKxV5ZzPiDYcunr7Fo";
    private AppComponent appComponent;
    private MapsComponent mapsComponent;
    private DetailsComponent detailsComponent;
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        
        Timber.plant(new Timber.DebugTree());
        
        AppModule appModule = new AppModule(BASE_URL, API_KEY);
        
        appComponent = DaggerAppComponent.builder()
                .appModule(appModule)
                .build();
    }
    
    public static MapsApplication get(Context context)
    {
        return (MapsApplication) context.getApplicationContext();
    }
    
    public MapsComponent getMapsComponent()
    {
        mapsComponent = appComponent.add(new MapsModule());
        return mapsComponent;
    }
    public void clearMapsComponent()
    {
        mapsComponent = null;
    }
    
    public DetailsComponent getDetailsComponent()
    {
        detailsComponent = appComponent.add(new DetailsModule());
        return detailsComponent;
    }
    
    public void clearDetailsComponent()
    {
        detailsComponent = null;
    }
}
