package com.falso.news.mvvm;

import java.util.ArrayList;

public class NewsContainer {

    private String status;

    private ArrayList<News> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setArticles(ArrayList<News> articles) {
        this.articles = articles;
    }

    public ArrayList<News> getArticles() {
        return articles;
    }
}