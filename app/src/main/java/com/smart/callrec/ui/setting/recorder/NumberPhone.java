package com.smart.callrec.ui.setting.recorder;

import android.text.TextUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

import com.smart.callrec.entity.Recording;

/**
 * Created by ntthe on 2/26/2018.
 */
@Table(name = "NumberPhone")
public class NumberPhone extends Model {
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phoneNumber;
    @Column(name = "isExcluded")
    private boolean isExcluded;

    @Override
    public String toString() {
        return name +" ("+ phoneNumber+")";
    }

    public NumberPhone() {
        super();
    }

    public NumberPhone(String name, String phoneNumber, boolean isExcluded) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isExcluded = isExcluded;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isExcluded() {
        return isExcluded;
    }
    public void setExcluded(boolean excluded) {
        isExcluded = excluded;
    }
    public static List<NumberPhone> getAllItems() {
        return new Select().from(Recording.class).orderBy("Id DESC").execute();
    }

    public static List<NumberPhone> getItemsByType(boolean isExcluded) {
        return new Select().from(NumberPhone.class).where("isExcluded = ?", isExcluded).orderBy("Id DESC").execute();
    }
    public static NumberPhone getItemsById(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return null;
        }
        return new Select().from(NumberPhone.class).where("phone = ?", phone).executeSingle();
    }
}
