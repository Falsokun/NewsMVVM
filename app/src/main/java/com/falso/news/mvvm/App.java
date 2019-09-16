package com.falso.news.mvvm;

import android.app.Application;

import com.falso.news.mvvm.repository.NewsApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static final String API_URL = "https://newsapi.org/v2/";
    public static final String API_KEY = "e7a4d3493ec84a1a9232789bf7a943cf"; //"63fc3cd3d21d4256834ba0515a867801";

    private static NewsApi api;

    public static App instance;

    public App() {
        initRetrofit();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    private void initRetrofit() {
        Retrofit retrofitInstance = new Retrofit.Builder().baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        api = retrofitInstance.create(NewsApi.class);
    }

    public static NewsApi getApi() {
        return api;
    }

}