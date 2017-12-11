package com.example.chris.weekend5.utils;

/**
 * Created by singh on 12/7/17.
 */

public interface BasePresenter<V extends BaseView> {

    void attachView(V view);
    void detachView();
}
