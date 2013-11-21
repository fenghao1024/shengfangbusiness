package com.hik.RtspClient;

/**
 * ABS_TIME，回放的的时间点的格式类
 * @author weilinfeng
 * @Data 2013-7-4
 */
public class ABS_TIME {
    private int dwYear;
    private int dwMonth;
    private int dwDay;
    private int dwHour;
    private int dwMinute;
    private int dwSecond;
    
    /**
     * 回放用的的时间点对象的构造函数
     */
    public ABS_TIME() {
        // TODO Auto-generated constructor stub
        dwYear = 0;
        dwMonth = 0;
        dwDay = 0;
        dwHour = 0;
        dwMinute = 0;
        dwSecond = 0;
    }
    
    /**
      * 获取年份
      * @return
      * @since V1.0
      */
    public int getYear(){
        return dwYear;
    }
    
    /**
      * 设置年份
      * @param year
      * @since V1.0
      */
    public void setYear(int year){
        dwYear = year;
    }
    
    /**
      * 获取月份
      * @return
      * @since V1.0
      */
    public int getMonth(){
        return dwMonth;
    }
    
    /**
      * 设置月份
      * @param month
      * @since V1.0
      */
    public void setMonth(int month){
        dwMonth = month;
    }
    
    /**
      * 获取天
      * @return
      * @since V1.0
      */
    public int getDay(){
        return dwDay;
    }
    
    /**
      * 设置天
      * @param day
      * @since V1.0
      */
    public void setDay(int day){
        dwDay = day;
    }
    
    /**
      * 获取小时
      * @return
      * @since V1.0
      */
    public int getHour(){
        return dwHour;
    }
    
    /**
      * 设置小时
      * @param hour
      * @since V1.0
      */
    public void setHour(int hour){
        dwHour = hour;
    }
    
    /**
      * 获取分
      * @return
      * @since V1.0
      */
    public int getMinute(){
        return dwMinute;
    }
    
    /**
      * 设置分
      * @param minute
      * @since V1.0
      */
    public void setMinute(int minute){
        dwMinute = minute;
    }
    
    /**
      * 获取秒
      * @return
      * @since V1.0
      */
    public int getSecond(){
        return dwSecond;
    }
    
    /**
      * 设置秒
      * @param second
      * @since V1.0
      */
    public void setSecond(int second){
        dwSecond = second;
    }
    
    
}
