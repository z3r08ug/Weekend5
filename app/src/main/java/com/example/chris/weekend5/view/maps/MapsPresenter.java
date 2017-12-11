package com.example.chris.weekend5.view.maps;

import android.util.Log;

import com.example.chris.weekend5.data.remote.RemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by singh on 12/7/17.
 */

public class MapsPresenter implements MapsContract.Presenter
{

    private static final String TAG = MapsPresenter.class.getSimpleName() + "_TAG";
    RemoteDataSource remoteDataSource;
    MapsContract.View view;
//    List<List<HourlyForecast>> hourlyList = new ArrayList<>();

    @Inject
    public MapsPresenter(RemoteDataSource remoteDataSource)
    {
        this.remoteDataSource = remoteDataSource;

    }


    @Override
    public void attachView(MapsContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

}
