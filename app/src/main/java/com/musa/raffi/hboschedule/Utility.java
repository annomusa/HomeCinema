package com.musa.raffi.hboschedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
}
