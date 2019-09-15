package com.falso.news.mvvm;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DataRepository {

    static Disposable getNews(int page, Executor<ArrayList<News>> onDataReceived, Executor<Throwable> onError) {
        return App.getApi()
                .newsList(Utils.DEFAULT_TOPIC, Utils.DEFAULT_PAGE_SIZE, page)
                .subscribeOn(Schedulers.newThread())
                .subscribe(x -> {
                    if (onDataReceived != null) {
                        onDataReceived.onResult(x.getArticles());
                    }
                }, throwable -> {
                    if (onError != null) {
                        onError.onResult(throwable);
                    }
                });
    }

    interface Executor<T> {
        void onResult(T resValue);
    }

}