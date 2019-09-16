package com.falso.news.mvvm.mvvm.viewModel;

import androidx.lifecycle.MediatorLiveData;
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

    private final MediatorLiveData<Boolean> showMessage = new MediatorLiveData<>();

    public MainActivityViewModel() {
        isLoading.setValue(true);

        disposable.add(query.subscribe(newQuery -> initData(),
                ignored -> error.postValue(Utils.getString(R.string.err))));

        initData();
        observeData();
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

    private void observeData() {
        showMessage.addSource(isLoading, isLoading ->
                updateShowMessage(isLoading, data.getValue(), error.getValue()));

        showMessage.addSource(data, data ->
                updateShowMessage(isLoading.getValue(), data, error.getValue()));

        showMessage.addSource(error, err ->
                updateShowMessage(isLoading.getValue(), data.getValue(), err));
    }

    private void updateShowMessage(Boolean isLoading, ArrayList<News> data, String err) {
        if (data == null) {
            return;
        }

        showMessage.postValue(!isLoading && data.size() == 0
                || data.size() != 0 && err != null);
    }

    public void updateData() {
        initData();
    }

    public void onTopicChanged(String query) {
        this.query.onNext(query);
    }

    //region getters
    public MutableLiveData<String> getError() {
        return error;
    }

    public MutableLiveData<ArrayList<News>> getData() {
        return data;
    }

    public MutableLiveData<Boolean> isLoading() {
        return isLoading;
    }

    public MediatorLiveData<Boolean> getShowMessage() {
        return showMessage;
    }
    //endregion

}