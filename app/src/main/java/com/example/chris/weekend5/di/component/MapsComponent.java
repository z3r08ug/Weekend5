package com.example.chris.weekend5.di.component;


import com.example.chris.weekend5.di.module.MapsModule;
import com.example.chris.weekend5.view.maps.MapsActivity;

import dagger.Subcomponent;

/**
 * Created by singh on 12/7/17.
 */

@Subcomponent(modules = MapsModule.class)
public interface MapsComponent
{
    void inject(MapsActivity mapsActivity);
}
