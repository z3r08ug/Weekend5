package com.example.chris.weekend5.di.component;

import com.example.chris.weekend5.di.module.DetailsModule;
import com.example.chris.weekend5.view.placedetail.PlaceDetailsActivity;

import dagger.Subcomponent;

/**
 * Created by chris on 12/9/2017.
 */

@Subcomponent(modules = DetailsModule.class)
public interface DetailsComponent
{
    void inject(PlaceDetailsActivity placeDetailsActivity);
}
