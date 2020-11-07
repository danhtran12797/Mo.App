package com.thd.danhtran12797.moapp.utils;

import android.content.res.Resources;
import android.graphics.Point;
import android.view.WindowManager;

import com.thd.danhtran12797.moapp.App;

import static android.content.Context.WINDOW_SERVICE;

public class ScreenUtils {

    private static ScreenUtils screenUtils;
    private int width;
    private int height;

    public static ScreenUtils getInstance() {
        if (screenUtils == null) {
            screenUtils = new ScreenUtils();
        }
        return screenUtils;
    }

    private ScreenUtils() {
        Point size = new Point();
        WindowManager mWindowManager = (WindowManager) App.getContext().getSystemService(WINDOW_SERVICE);
        if (mWindowManager != null) {
            mWindowManager.getDefaultDisplay().getSize(size);
        }
        width = size.x;
        height = size.y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getDensity() {
        return App.getContext().getResources().getDisplayMetrics().density;
    }

    public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}