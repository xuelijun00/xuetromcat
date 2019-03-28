package com.yks.bigdata.util;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zh on 2017/7/3.
 */
public class DateUtils {
    private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");


    public static String getCurrentDateStr(){
        Date date = new Date();
        return sf.format(date);
    }

    public static Date formmatStr(String str)throws Exception{
        return sf.parse(str);
    }

    public static Date formmatStr(String str,String format)throws Exception{
        if (StringUtils.isNotEmpty(format)){
            SimpleDateFormat sf2 = new SimpleDateFormat(format);
            return sf2.parse(str);
        }else
            return formmatStr(str);
    }


}
