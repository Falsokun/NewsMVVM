package com.falso.news.mvvm.repository;


import com.falso.news.mvvm.App;
import com.falso.news.mvvm.pojo.NewsContainer;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {

    @GET("everything?apiKey=" + App.API_KEY)
    Observable<NewsContainer> newsList(@Query("q") String topic, @Query("pageSize") int pageSize,
                                       @Query("page") int page);


}
