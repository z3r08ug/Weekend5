package com.example.chris.weekend5.di.component;


import com.example.chris.weekend5.di.module.AppModule;
import com.example.chris.weekend5.di.module.DetailsModule;
import com.example.chris.weekend5.di.module.MapsModule;

import dagger.Component;

/**
 * Created by singh on 12/7/17.
 */

@Component(modules = AppModule.class)
public interface AppComponent
{

    MapsComponent add(MapsModule mapsModule);
    DetailsComponent add(DetailsModule detailsModule);

}
