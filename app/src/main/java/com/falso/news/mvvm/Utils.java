package com.falso.news.mvvm;

public class Utils {

    public static int DEFAULT_PAGE_SIZE = 10;
    public static int DEFAULT_PAGE = 1;

    //idk is it necessary or not to do only one topic, but let it be
    public static String DEFAULT_TOPIC = "sports";

    public static String getString(int stringResourceId) {
        return App.getInstance().getApplicationContext().getString(stringResourceId);
    }

}