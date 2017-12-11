package com.example.chris.weekend5.data.remote;


import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by  Admin on 12/3/2017.
 */

public class RemoteDataSource {


    String baseUrl, apiKey;

    public RemoteDataSource(String baseURL, String apiKey) {
        this.baseUrl = baseURL;
        this.apiKey = apiKey;
    }


    public Retrofit create() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

//    public static Call<CurrentData> getCurrentWeather(String zipCode) {
//        RemoteService remoteService = getRemoteService();
//        return remoteService.getCurrentData(zipCode, apiKey);
//    }
//
//    public static Call<ForecastData> getForecastWeather(String zipCode) {
//        RemoteService weatherDayDataService = getRemoteService();
//        return weatherDayDataService.getForecastWeather(zipCode, apiKey);
//
//    }

    private  RemoteService getRemoteService() {
        Retrofit retrofit = create();
        return retrofit.create(RemoteService.class);
    }

//    //using rxjava observables
//    public  Observable<CurrentData> getCurrentWeatherRx(String zipcode) {
//        RemoteService remoteService = getRemoteService();
//        return remoteService.getCurrentDataRx(zipcode, apiKey);
//
//    }
//
//    public  Observable<ForecastData> getForecastWeatherRx(String zipcode) {
//        RemoteService remoteService = getRemoteService();
//        return remoteService.getForecastWeatherRx(zipcode, apiKey);
//
//    }


}

