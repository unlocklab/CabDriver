package com.example.cabbooking.driver.other;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.DateFormat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class RoundImage {
    public static List<File> getListFiles(File parentDir, String file_name) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.getName().substring(0, 11).equals(file_name.substring(0, 11))) {
                file.delete();
            }
        }
        return inFiles;
    }

    public static String getLocalTime(long time) {
        try {
            final CharSequence cur_date2 = DateFormat.format("yyyy-MM-dd HH:mm:ss", time);
            System.out.println("asd----------------------"+cur_date2);
            return cur_date2.toString();
        } catch (Exception e) {
            return null;
        }
    }
    public static long getTimeMilies(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date mDate = sdf.parse(str);
            long timeInMilliseconds = mDate.getTime();
            return timeInMilliseconds;
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static String[] getDay(String str) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy ,HH:mm aaa");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("LOCAL"));
            Date myDate = simpleDateFormat.parse(str);
            CharSequence day = DateFormat.format("dd", myDate);
            CharSequence month = DateFormat.format("MM", myDate);
            CharSequence year = DateFormat.format("yyyy", myDate);

            String str1[] = new String[3];

            str1[0] = day.toString();
            str1[1] = month.toString();
            str1[2] = year.toString();

            return str1;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String[] getCurrentDay() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy ,HH:mm aaa");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("LOCAL"));
            Date myDate = new Date();
            CharSequence day = DateFormat.format("dd", myDate);
            CharSequence month = DateFormat.format("MM", myDate);
            CharSequence year = DateFormat.format("yyyy", myDate);

            String str1[] = new String[3];

            str1[0] = day.toString();
            str1[1] = month.toString();
            str1[2] = year.toString();

            return str1;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String setTime(String dateposted) {

        Date today = new Date();
        Date tomarrow = getDate1(dateposted);
        long different = today.getTime() - tomarrow.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        if(different>0) {
            long seconds = different / secondsInMilli;
            long minutes = different / minutesInMilli;
            long hours = different / hoursInMilli;
            long days = different / daysInMilli;

            System.out.println("asd-----"+hours+"-------"+minutes+"-------"+days);

            if(days>0){
                return convertInMyDate(dateposted);
            }
            else if(hours>0){
                return hours+" hours ago";
            }
            else if(minutes>0){
                return hours+" minutes ago";
            }
            else{
                return convertInMyDate(dateposted);
            }
        }
        else{
            return "Today ,"+convertInMyDate(dateposted);
        }
    }

    private static Date getDate(long utcTime) {
        try {
            String str = getLocalTime(utcTime);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date myDate = simpleDateFormat.parse(str);
            return myDate;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static Date getDate1(String str) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy ,HH:mm aaa");
            Calendar cal = Calendar.getInstance();
            TimeZone utcZone = cal.getTimeZone();
            simpleDateFormat.setTimeZone(utcZone);
            Date myDate = simpleDateFormat.parse(str);
            return myDate;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static String convertInMyDate(String str) {
      CharSequence year = null;
        try {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("LOCAL"));
            Date myDate = simpleDateFormat.parse(str);
            year = DateFormat.format("dd-MM-yy ,HH:mm aaa", myDate);
//            final CharSequence month = DateFormat.format("MM", myDate);
//            final CharSequence day = DateFormat.format("dd", myDate);
//            final CharSequence time = DateFormat.format("HH:mm aaa", myDate);

//            int x = Integer.parseInt(month.toString());
//
//            switch (x) {
//                case 1:
//                    return day + "-Jan-" + year+" ,"+time;
//                case 2:
//                    return day + "-Feb-" + year+" ,"+time;
//                case 3:
//                    return day + "-Mar-" + year+" ,"+time;
//                case 4:
//                    return day + "-Apr-" + year+" ,"+time;
//                case 5:
//                    return day + "-May-" + year+" ,"+time;
//                case 6:
//                    return day + "-Jun-" + year+" ,"+time;
//                case 7:
//                    return day + "-Jul-" + year+" ,"+time;
//                case 8:
//                    return day + "-Aug-" + year+" ,"+time;
//                case 9:
//                    return day + "-Sep-" + year+" ,"+time;
//                case 10:
//                    return day + "-Oct-" + year+" ,"+time;
//                case 11:
//                    return day + "-Nov-" + year+" ,"+time;
//                case 12:
//                    return day + "-Dec-" + year+" ,"+time;
//            }

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
        return year.toString();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static long getUtcTime() {
        Date date = new Date();
        return date.getTime();
    }
    public static long getCTime() {
        Date date = new Date();

        return date.getTime();
    }
    public static Bitmap createBitmapFromUri(Activity activity, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap mark(Bitmap src, String watermark, Point location, int color, int alpha, int size, boolean underline, Typeface typeface) {

        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);


        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(size);
        paint.setAntiAlias(true);
        paint.setUnderlineText(underline);
        paint.setTypeface(typeface);
        canvas.drawText(watermark, location.x, location.y, paint);

        return result;
    }

    public static String getApplicationName(Activity activity) {
        return activity.getApplicationInfo().loadLabel(activity.getPackageManager()).toString();
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        try {
            Bitmap output;

            if (bitmap.getWidth() > bitmap.getHeight()) {
                output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            } else {
                output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            float r = 0;

            if (bitmap.getWidth() > bitmap.getHeight()) {
                r = bitmap.getHeight() / 2;
            } else {
                r = bitmap.getWidth() / 2;
            }

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(r, r, r, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        try {
            ExifInterface ei = new ExifInterface(image_absolute_path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotate(bitmap, 90);

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotate(bitmap, 180);

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotate(bitmap, 270);

                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    return flip(bitmap, true, false);

                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    return flip(bitmap, false, true);

                default:
                    return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        try {
            Matrix matrix = new Matrix();
            matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return bitmap;
        }
    }


}