package com.falso.news.mvvm.pojo;

import java.util.ArrayList;

public class NewsContainer {

    private ArrayList<News> articles;

    public void setArticles(ArrayList<News> articles) {
        this.articles = articles;
    }

    public ArrayList<News> getArticles() {
        return articles;
    }

}