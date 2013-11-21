package com.hik.RtspClient.Util;

import com.hik.RtspClient.Exception.NetException;

public abstract class PlaybackPlayer extends MediaPlayer {
	
	public static final int PLAY_PAUSE_SUCCESS = 20000;
	public static final int PLAY_RESUME_SUCCESS = 20001;
	public static final int PLAY_SLOW_SUCCESS = 20002;
	public static final int PLAY_NORMAL_SUCCESS = 20003;
	public static final int PLAY_FAST_SUCCESS = 20004;
	public static final int PLAY_SETPOS_SUCCESS = 20004;
	
	
	public abstract void SetTime(long startTime, long stopTime);
	
	/**
     * 这里对方法做描述
	 * @throws NetException 
     * @see 
     * @since V1.0
     */
    public abstract void pause() throws NetException;
    
    /**
     * 这里对方法做描述
     * @throws NetException 
     * @see 
     * @since V1.0
     */
    public abstract void resume() throws NetException;
    
    /**
     * 这里对方法做描述
     * @throws NetException 
     * @see 
     * @since V1.0
     */
    public abstract void slowPlay() throws NetException;
    
    /**
     * 这里对方法做描述
     * @throws NetException 
     * @see 
     * @since V1.0
     */
    public abstract void fastPlay() throws NetException;
    
    /**
     * 这里对方法做描述
     * @throws NetException 
     * @see 
     * @since V1.0
     */
    public abstract void normalPlay() throws NetException;
    
    /**
     * 这里对方法做描述
     * @throws NetException 
     * @see 
     * @since V1.0
     */
    public abstract void setPosition(long startTime, long stopTime) throws NetException;
    
    public abstract PlaybackState GetState();
}
