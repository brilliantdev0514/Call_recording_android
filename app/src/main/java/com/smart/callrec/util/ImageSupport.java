package com.smart.callrec.util;

import android.util.Log;

public class ImageSupport {
    public static final String hot = "http://static.laguaz.com";

    public static String getThumbnail(String url) {
        return getImage(url, "150x150");
    }

    public static String getAlbumThumbnail(String url) {
        return getImage(url, "200x200");
    }

    public static String getCover(String url) {
        return getImage(url, "620x260");
    }

    public static String getImage(String url, String size) {
        if (url == null || url.isEmpty()) return "";

        String newUrl = url.replace("/images/", "/" + size + "/images/");
        if (!newUrl.contains(hot)) newUrl = hot + newUrl;
        Log.d("ImageSupport", "url: " + url);
        Log.d("ImageSupport", "newUrl: " + newUrl);

        return newUrl;
    }

}
