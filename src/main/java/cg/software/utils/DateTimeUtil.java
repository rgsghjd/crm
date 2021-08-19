package cg.software.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    public static String getsystime(){
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");//格式化时间
        Date date=new Date();//获取系统当前时间
        String time=sf.format(date);//将系统时间格式化
        return time;
    }
}
