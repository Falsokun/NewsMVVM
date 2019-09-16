package com.falso.news.mvvm.repository;

import com.falso.news.mvvm.App;
import com.falso.news.mvvm.Utils;
import com.falso.news.mvvm.pojo.News;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DataRepository {

    public static Disposable getNews(int page, String query,
                                     Executor<ArrayList<News>> onDataReceived,
                                     Executor<Throwable> onError) {
        return App.getApi()
                .newsList(query, Utils.DEFAULT_PAGE_SIZE, page)
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

    public interface Executor<T> {
        void onResult(T resValue);
    }

}