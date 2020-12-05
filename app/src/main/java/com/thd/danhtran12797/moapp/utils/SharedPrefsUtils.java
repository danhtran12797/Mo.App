package com.thd.danhtran12797.moapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.thd.danhtran12797.moapp.App;

public class SharedPrefsUtils {
    private static SharedPrefsUtils sharedPrefsUtils;
    private SharedPreferences sharedPreferences;

    private SharedPrefsUtils() {
        sharedPreferences = App.getContext().getSharedPreferences("MoAppDatabase", Context.MODE_PRIVATE);
    }

    public static SharedPrefsUtils getInstance() {
        if (sharedPrefsUtils == null) {
            sharedPrefsUtils = new SharedPrefsUtils();
        }
        return sharedPrefsUtils;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> anonymousClass) {
        if (anonymousClass == String.class) {
            return (T) sharedPreferences.getString(key, null);
        } else if (anonymousClass == Boolean.class) {
            return (T) Boolean.valueOf(sharedPreferences.getBoolean(key, false));
        } else if (anonymousClass == Float.class) {
            return (T) Float.valueOf(sharedPreferences.getFloat(key, 0));
        } else if (anonymousClass == Integer.class) {
            return (T) Integer.valueOf(sharedPreferences.getInt(key, 0));
        } else if (anonymousClass == Long.class) {
            return (T) Long.valueOf(sharedPreferences.getLong(key, 0));
        }
        return null;
    }

    public <T> void put(String key, T data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        }
        editor.apply();
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}