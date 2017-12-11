package com.example.chris.weekend5.data.remote;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by  Admin on 12/3/2017.
 */

public interface RemoteService {


//    @GET("{key}/conditions/q/{zipCode}")
//    Call<CurrentData> getCurrentData
//            (@Path(value = "zipCode", encoded = true) String zipCode,
//             @Path(value = "key", encoded = true) String key);
//
//    @GET("{key}/hourly/q/{zipCode}")
//    Call<ForecastData> getForecastWeather
//            (@Path(value = "zipCode", encoded = true) String zipCode,
//             @Path(value = "key", encoded = true) String key);
//
//
//    //using rxjava observables
//    @GET("{key}/conditions/q/{zipCode}.json")
//    Observable<CurrentData> getCurrentDataRx
//    (@Path(value = "zipCode", encoded = true) String zipCode,
//     @Path(value = "key", encoded = true) String key);
//
//    @GET("{key}/hourly/q/{zipCode}.json")
//    Observable<ForecastData> getForecastWeatherRx
//            (@Path(value = "zipCode", encoded = true) String zipCode,
//             @Path(value = "key", encoded = true) String key);
}
