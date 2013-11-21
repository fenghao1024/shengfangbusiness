package com.hik.RtspClient.Util;


import android.view.SurfaceView;

import com.hik.RtspClient.Exception.NetException;

public abstract class MediaPlayer {
	
   
    
    public static final int PLAY_RTSP_SUCCESS = 10000;
    public static final int START_OPEN_FAILED = 10001;
    public static final int PLAY_DISPLAY_SUCCESS = 10002;
    public static final int STOP_SUCCESS = 10003;
    
    
    public abstract void setParam(int model, String url, String name, String password);
  
    /**
     * 这里对方法做描述
     * @throws NetException 
     * @see 
     * @since V1.0
     */
    public abstract void start(SurfaceView surfaceView) throws NetException;
    
    /**
     * 这里对方法做描述
     * @see 
     * @since V1.0
     */
    public abstract void stop();
    
    
    public abstract void setMessageCallback(MediaPlayerMessageCallback messageCallback, int useId);
}
