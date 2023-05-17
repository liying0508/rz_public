package io.renren.modules.report.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DueTimeUtil {

    public static String getDueTime(String start,String end){
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start.substring(0,10));
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(end.substring(0,10));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long l = endTime - startTime;
        long t = 30L*24*60*60*1000;
        Long ys = l%t;
        Long dueTime = l/t;
        if(ys>0){
            dueTime = dueTime + 1;
        }
        String s = dueTime.toString();
        return s;
    }

}
