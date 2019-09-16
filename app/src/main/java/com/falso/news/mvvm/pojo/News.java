package com.falso.news.mvvm.pojo;


import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class News {

    private String author;

    private String title;

    private String description;

    private String url;

    @SerializedName(value = "urlToImage")
    private String imgUrl;

    @SerializedName(value = "publishedAt")
    private String date;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSimpleDate() {
        try {
            Date incomingDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(date);
            return new SimpleDateFormat("yyyy-mm-dd hh:mm", Locale.US).format(incomingDate);
        } catch (ParseException e) {
            return "";
        }
    }
}
