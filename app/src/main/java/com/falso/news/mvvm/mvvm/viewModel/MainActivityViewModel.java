package com.falso.news.mvvm.mvvm.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.falso.news.mvvm.R;
import com.falso.news.mvvm.Utils;
import com.falso.news.mvvm.pojo.News;
import com.falso.news.mvvm.repository.DataRepository;

import java.util.ArrayList;

import io.reactivex.subjects.BehaviorSubject;

public class MainActivityViewModel extends BaseViewModel {

    private final MutableLiveData<String> error = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<News>> data = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private final BehaviorSubject<String> query = BehaviorSubject.createDefault(
            Utils.DEFAULT_TOPIC);

    public MainActivityViewModel() {
        isLoading.setValue(true);

        disposable.add(query.subscribe(newQuery -> initData(),
                                       ignored -> error.postValue(Utils.getString(R.string.err))));

        initData();
    }

    private void initData() {
        disposable.add(DataRepository.getNews(Utils.DEFAULT_PAGE, query.getValue(), newsData -> {
            data.postValue(newsData);
            isLoading.postValue(false);
        }, throwable -> {
            isLoading.postValue(false);
            error.postValue(Utils.getString(parseError(throwable)));
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
        if (isLoading.getValue() != null && isLoading.getValue()) {
            return;
        }

        isLoading.postValue(true);
        if (data.getValue() == null) {
            return;
        }

        int currentPage = data.getValue().size() / Utils.DEFAULT_PAGE_SIZE;
        disposable.add(DataRepository.getNews(currentPage + 1, query.getValue(), newsData -> {
            ArrayList<News> temp = data.getValue();
            if (temp != null) {
                temp.addAll(newsData);
            } else {
                temp = newsData; // not possible
            }

            data.postValue(temp);
            isLoading.postValue(false);
        }, throwable -> {
            isLoading.postValue(false);
            error.postValue(Utils.getString(parseError(throwable)));
        }));
    }

    public void updateData() {
        initData();
    }

    public void onTopicChanged(String query) {
        this.query.onNext(query);
    }
}