package com.falso.news.mvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;

import com.falso.news.mvvm.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    private MainActivityViewModel mViewModel;

    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setLifecycleOwner(this);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mBinding.setViewModel(mViewModel);
        initRv();
    }

    private void initRv() {
        mAdapter = new NewsAdapter(new ArrayList<>());
        mBinding.newsListRv.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.newsListRv.setLayoutManager(layoutManager); // default
        mViewModel.getData().observe(this, data -> {
            mAdapter.setData(data);
            mAdapter.notifyDataSetChanged();
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mBinding.newsListRv.getContext(),
                layoutManager.getOrientation());
        mBinding.newsListRv.addItemDecoration(dividerItemDecoration);

        mBinding.newsListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    mViewModel.loadMore();
                }
            }
        });

        mViewModel.isLoading().observe(this, isLoading -> {
            delayedAnimation(isLoading);
        });
    }

    private void delayedAnimation(boolean isLoading) {
        mBinding.rvProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        Transition transition = new AutoTransition();
        transition.setDuration(500);
        TransitionManager.beginDelayedTransition(mBinding.container, transition);
    }
}
