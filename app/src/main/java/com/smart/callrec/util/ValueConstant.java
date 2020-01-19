package com.smart.callrec.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Harry_Hai on 2/16/2018.
 */

public interface ValueConstant {
    @IntDef({ItemType.Header, ItemType.Item})
    @Retention(RetentionPolicy.SOURCE)
    @interface ItemType {
        int Header = 0, Item = 1;
    }
}
