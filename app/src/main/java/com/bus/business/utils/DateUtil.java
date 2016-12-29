package com.bus.business.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/29
 */
public class DateUtil {
    public static String getCurGroupDay(long time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(new Date(time));
    }
}
