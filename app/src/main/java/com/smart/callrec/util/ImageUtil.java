package com.smart.callrec.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class ImageUtil {

    public static void setImageView(Context context, String url, ImageView imageView) {
        if (imageView != null && !StringUtils.isEmpty(url)) {
            Glide
                    .with(context)
                    .load(url)
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        }
    }

    public static void setImageView(Context context, int resId, ImageView imageView) {
        if (imageView != null) {
            Glide
                    .with(context)
                    .load(resId)
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        }
    }
}
