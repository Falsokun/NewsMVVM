package com.falso.news.mvvm.mvvm.viewModel;

import androidx.lifecycle.ViewModel;

import com.falso.news.mvvm.R;

import java.net.UnknownHostException;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;

public class BaseViewModel extends ViewModel {

    CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    int parseError(Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
            return R.string.err_no_internet;
        }

        if (throwable instanceof HttpException) {
            HttpException exception = (HttpException) throwable;
            switch (exception.code()) {
                case 426:
                    return R.string.err_declined;
                case 401:
                    return R.string.err_unauthorized;
                case 429:
                    return R.string.err_too_many_requests;
            }
        }

        return R.string.err;
    }
}
