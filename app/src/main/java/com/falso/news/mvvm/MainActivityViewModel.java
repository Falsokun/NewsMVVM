package com.falso.news.mvvm;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class MainActivityViewModel extends BaseViewModel {

    private MutableLiveData<String> error = new MutableLiveData<>();

    private MutableLiveData<ArrayList<News>> data = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MainActivityViewModel() {
        isLoading.setValue(true);
        initData();
    }

    private void initData() {
        disposable.add(DataRepository.getNews(Utils.DEFAULT_PAGE, newsData -> {
                    data.postValue(newsData);
                    isLoading.postValue(false);
                },
                throwable -> {
                    isLoading.postValue(false);
                    error.postValue(throwable.getMessage());
                }));
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public MutableLiveData<ArrayList<News>> getData() {
        return data;
    }

    public MutableLiveData<Boolean> isLoading() {
        return isLoading;
    }

    public void loadMore() {
        isLoading.postValue(true);
        if (data.getValue() == null) {
            return;
        }

        int currentPage = data.getValue().size() / Utils.DEFAULT_PAGE_SIZE;
        disposable.add(DataRepository.getNews(currentPage + 1, newsData -> {
                    ArrayList<News> temp = data.getValue();
                    if (temp != null) {
                        temp.addAll(newsData);
                    } else {
                        temp = newsData; // not possible
                    }

                    data.postValue(temp);
                    isLoading.postValue(false);
                },
                throwable -> {
                    isLoading.postValue(false);
                    error.postValue(throwable.getMessage());
                }));
    }

}