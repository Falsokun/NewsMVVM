package com.falso.news.mvvm.mvvm.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.falso.news.mvvm.R;
import com.falso.news.mvvm.adapter.NewsAdapter;
import com.falso.news.mvvm.adapter.NewsAdapterDiffUtil;
import com.falso.news.mvvm.databinding.ActivityMainBinding;
import com.falso.news.mvvm.mvvm.viewModel.MainActivityViewModel;
import com.falso.news.mvvm.pojo.News;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    private MainActivityViewModel mViewModel;

    private NewsAdapter mAdapter;

    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setLifecycleOwner(this);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mBinding.setViewModel(mViewModel);
        initRv();
        initObservers();
        initRefresher();
    }

    private void initRefresher() {
        mBinding.refresh.setOnRefreshListener(() -> {
            mViewModel.updateData();
        });

        mViewModel.isLoading().observe(this, value -> {
            if (mBinding.refresh.isRefreshing()) {
                mBinding.refresh.setRefreshing(value);
            }
        });
    }

    private void initObservers() {
        mViewModel.getError().observe(this, errMsg -> {
            if (errMsg != null) {
                mAdapter.disableLoading();
                if (mAdapter.getData().size() != 0) {
                    if (snackbar == null || !snackbar.isShown()) {
                        snackbar = Snackbar.make(mBinding.getRoot(), errMsg, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }
            }
        });
    }

    private void initRv() {
        mAdapter = new NewsAdapter(new ArrayList<>());
        mBinding.newsListRv.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.newsListRv.setLayoutManager(layoutManager); // default

        mViewModel.getData().observe(this, data -> {
            mBinding.refresh.setRefreshing(false);
            mAdapter.setData(data);
        });

        mBinding.newsListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    mViewModel.loadMore();
                }
            }
        });

        mAdapter.setOnClickListener(url -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });
    }

}