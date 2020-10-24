package com.thd.danhtran12797.moapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class ImageResizer {

    //For Image Size 640*480, use MAX_SIZE =  307200 as 640*480 307200
    //private static long MAX_SIZE = 360000;
    //private static long THUMB_SIZE = 6553;

    public static Bitmap reduceBitmapSize(Bitmap bitmap, int MAX_SIZE) {
        double ratioSquare;
        int bitmapHeight, bitmapWidth;
        bitmapHeight = bitmap.getHeight();
        bitmapWidth = bitmap.getWidth();
        ratioSquare = (bitmapHeight * bitmapWidth) / MAX_SIZE;
        if (ratioSquare <= 1)
            return bitmap;
        double ratio = Math.sqrt(ratioSquare);
        Log.d("mylog", "Ratio: " + ratio);
        int requiredHeight = (int) Math.round(bitmapHeight / ratio);
        int requiredWidth = (int) Math.round(bitmapWidth / ratio);
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true);
    }

    public static File saveBitmap(Bitmap bitmap) {
        File imagepath = null;
        if (Environment.getExternalStorageState().equals("mounted")) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            StringBuilder sb = new StringBuilder();
            sb.append(externalStorageDirectory.getAbsolutePath());
            sb.append("/MoApp");
            File file = new File(sb.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(file.getPath());
            String file_name = "/" + System.currentTimeMillis() + ".jpg";
            sb2.append(file_name);
            imagepath = new File(sb2.toString());
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(imagepath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                Log.d("mylog", "saveBitmap: " + e.getMessage());
            }
        }
        return imagepath;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("mylog", "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

//    public static File getBitmapFile(Bitmap reduceBitmap) {
//        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "moapp3");
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        reduceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//        byte[] bitmapData = bos.toByteArray();
//
//        try {
//            file.createNewFile();
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(bitmapData);
//            fos.flush();
//            fos.close();
//            return file;
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d("mylog", "getBitmapFile: "+e.getMessage());
//        }
//        return file;
//    }

//
//    public static Bitmap generateThumb(Bitmap bitmap, int THUMB_SIZE) {
//        double ratioSquare;
//        int bitmapHeight, bitmapWidth;
//        bitmapHeight = bitmap.getHeight();
//        bitmapWidth = bitmap.getWidth();
//        ratioSquare = (bitmapHeight * bitmapWidth) / THUMB_SIZE;
//        if (ratioSquare <= 1)
//            return bitmap;
//        double ratio = Math.sqrt(ratioSquare);
//        Log.d("mylog", "Ratio: " + ratio);
//        int requiredHeight = (int) Math.round(bitmapHeight / ratio);
//        int requiredWidth = (int) Math.round(bitmapWidth / ratio);
//        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true);
//    }

}