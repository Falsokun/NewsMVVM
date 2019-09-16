package com.falso.news.mvvm.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.falso.news.mvvm.pojo.News;

import java.util.ArrayList;
import java.util.Objects;

public class NewsAdapterDiffUtil extends DiffUtil.Callback {

    private ArrayList<News> oldData;
    private ArrayList<News> newData;

    public NewsAdapterDiffUtil(ArrayList<News> oldData, ArrayList<News> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData.size();
    }

    @Override
    public int getNewListSize() {
        return newData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        News oldNews = oldData.get(oldItemPosition);
        News newNews = newData.get(newItemPosition);
        return Objects.equals(oldNews.getAuthor(), newNews.getAuthor())
                && Objects.equals(oldNews.getDate(), newNews.getDate());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        News oldNews = oldData.get(oldItemPosition);
        News newNews = newData.get(newItemPosition);
        return Objects.equals(oldNews.getAuthor(), newNews.getAuthor())
                && Objects.equals(oldNews.getDate(), newNews.getDate())
                && Objects.equals(oldNews.getDescription(), newNews.getDescription())
                && Objects.equals(oldNews.getUrl(), newNews.getUrl());
    }
}
