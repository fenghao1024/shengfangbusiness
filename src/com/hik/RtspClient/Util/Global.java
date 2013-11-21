package com.hik.RtspClient.Util;

import java.util.Calendar;

public class Global {

    public static String getCurrentDateStringWith()
    {
        String res="";
       
          Calendar cld=Calendar.getInstance();
          String fileName = String.format("%04d%02d%02d%02d%02d%02d%03d", cld.get(Calendar.YEAR),
                  cld.get(Calendar.MONTH) + 1, cld.get(Calendar.DAY_OF_MONTH),
                  cld.get(Calendar.HOUR_OF_DAY), cld.get(Calendar.MINUTE), cld.get(Calendar.SECOND),
                  cld.get(Calendar.MILLISECOND));
          
          res=fileName;
        
        return res;
    }
}
