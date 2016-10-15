package com.musa.raffi.hboschedule;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Asus on 10/6/2016.
 */

public class Utility {
    public static String generalizeDateToShow(String date){
        String res = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date d = sdf.parse(date);
            sdf.applyPattern("EEE, d MMM yyyy");
            res = sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static boolean isInternetAvailable(Context ctx) {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static Calendar getCalender(String date, String time) {
        Calendar result = Calendar.getInstance();
        String dateString = date + " " + time;

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date mDate = null;
        try {
            mDate = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        result.setTime(mDate);
        return result;
    }
}
