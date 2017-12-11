package com.example.chris.weekend5.view.placedetail;

import com.example.chris.weekend5.data.remote.RemoteDataSource;
import com.example.chris.weekend5.view.maps.MapsContract;

import javax.inject.Inject;

/**
 * Created by chris on 12/9/2017.
 */

public class DetailsPresenter implements DetailsContract.View
{
    
    private static final String TAG = DetailsPresenter.class.getSimpleName() + "_TAG";
    RemoteDataSource remoteDataSource;
    DetailsContract.View view;
//    List<List<HourlyForecast>> hourlyList = new ArrayList<>();
    
    @Inject
    public DetailsPresenter(RemoteDataSource remoteDataSource)
    {
        this.remoteDataSource = remoteDataSource;
    }
    
    @Override
    public void showError(String s)
    {
    
    }
}
