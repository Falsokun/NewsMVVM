package com.falso.news.mvvm;

import android.app.Application;

import com.falso.news.mvvm.repository.NewsApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static final String API_URL = "https://newsapi.org/v2/";
    public static final String API_KEY = "63fc3cd3d21d4256834ba0515a867801";

    private static NewsApi api;

    public App() {
        initRetrofit();
    }

    private void initRetrofit() {
        //Конвертер, необходимый для преобразования JSON'а в объекты
        Retrofit retrofitInstance = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        api = retrofitInstance.create(NewsApi.class);
    }

    public static NewsApi getApi() {
        return api;
    }
}

