package com.hik.RtspClient.Playback;

import java.nio.ByteBuffer;
import java.util.Calendar;

import org.MediaPlayer.PlayM4.Player;
import org.MediaPlayer.PlayM4.Player.MPSystemTime;
import org.MediaPlayer.PlayM4.PlayerCallBack.PlayerDisplayCB;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hik.RtspClient.ABS_TIME;
import com.hik.RtspClient.RtspClient;
import com.hik.RtspClient.RtspClientCallback;
import com.hik.RtspClient.Exception.NetException;
import com.hik.RtspClient.Util.MediaPlayer;
import com.hik.RtspClient.Util.MediaPlayerMessageCallback;
import com.hik.RtspClient.Util.PlaybackPlayer;
import com.hik.RtspClient.Util.PlaybackState;

public class Playback extends PlaybackPlayer implements RtspClientCallback, PlayerDisplayCB{

private static final String LOG_TAG = "Live";
	
	
	private String mUrl = "";
	private int mModel = RtspClient.RTPRTSP_TRANSMODE;
	private String mDevName = "admin";
	private String mPsw = "12345";
	
	private SurfaceView mSurfaceView; 
	
	private Player mPlayerHandle;
	private int mPlayerPort = -1;
	
    private RtspClient mRtspEngineHandle;
    private int mRtspEngineIndex = RtspClient.RTSPCLIENT_INVALIDATE_ENGINEID;
    
    private MediaPlayerMessageCallback mMessageCallback;
    private int mUserId = -1;
    
    private PlaybackState mState = PlaybackState.RELEASE;


	private ABS_TIME mBeginT;
	private ABS_TIME mEndT;
	
	private MPSystemTime mCurrentT;


    
    public Playback() {
		// TODO Auto-generated constructor stub
    	initialize();
        
        Log.d(LOG_TAG, "Live >>> Live success!");
	}
    
	private void initialize() {
		// TODO Auto-generated method stub
		mPlayerHandle = Player.getInstance();
        if (null == mPlayerHandle) {
            Log.d(LOG_TAG, "initialize >>> Player handle is null!");
        }
        
        //RtspClient.initLib();
        mRtspEngineHandle = RtspClient.getInstance();
        if (null == mRtspEngineHandle) {
            Log.d(LOG_TAG, "initialize >>> RtspClient handle is null!");
        }
        
        mBeginT = new ABS_TIME();
        mEndT = new ABS_TIME();
        
        mCurrentT = new MPSystemTime();
        
        mState = PlaybackState.INIT;
	}

	
	
	@Override
	public void SetTime(long startTime, long stopTime) {
		// TODO Auto-generated method stub
		if(startTime < 0 || stopTime < 0) {
			return;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(startTime);
		mBeginT.setYear(calendar.get(Calendar.YEAR));
		mBeginT.setMonth(calendar.get(Calendar.MONTH)+1);
		mBeginT.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		mBeginT.setHour(calendar.get(Calendar.HOUR_OF_DAY));
		mBeginT.setMinute(calendar.get(Calendar.MINUTE));
		mBeginT.setSecond(calendar.get(Calendar.SECOND));
		
		calendar.setTimeInMillis(stopTime);
		mEndT.setYear(calendar.get(Calendar.YEAR));
		mEndT.setMonth(calendar.get(Calendar.MONTH)+1);
		mEndT.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		mEndT.setHour(calendar.get(Calendar.HOUR_OF_DAY));
		mEndT.setMinute(calendar.get(Calendar.MINUTE));
		mEndT.setSecond(calendar.get(Calendar.SECOND));
	}

	@Override
	public void start(SurfaceView surfaceView) throws NetException {
		// TODO Auto-generated method stub
		if(surfaceView == null)
		{
			Log.d(LOG_TAG, "Start >>> surfaceView is null");
			throw new NetException("start param null", 100);
		}
		
		mSurfaceView = surfaceView;
        
        if (mState == PlaybackState.STREAM) {
        	Log.d(LOG_TAG, "start >>> player is playing");
        	return;
        }
        
        startRtsp();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		if (mState == PlaybackState.INIT) {
            Log.d(LOG_TAG, "Stop >>> player have stoped");
            return;
        }
		
		stopRtsp();
        
        closePlayer();
        
        mMessageCallback.onMessageCallback(STOP_SUCCESS, mUserId, 0);
        
        mState = PlaybackState.INIT;
	}
	
	private void startRtsp() throws NetException {
        // TODO Auto-generated method stub
		Log.d(LOG_TAG, "startRtsp >>> startRtsp step1");
		
        if (null == mRtspEngineHandle) {
        	throw new NetException("mRtspEngineHandle is null!", mRtspEngineHandle.getLastError()); 
        }
        
        Log.d(LOG_TAG, "startRtsp >>> startRtsp step2");
        
        mRtspEngineIndex = mRtspEngineHandle.createRtspClientEngine(this, mModel);
        if (RtspClient.RTSPCLIENT_INVALIDATE_ENGINEID == mRtspEngineIndex) {
            throw new NetException("RtspClient createRtspClientEngine failed!", mRtspEngineHandle.getLastError());
        }

        Log.d(LOG_TAG, "startRtsp >>> startRtsp step31");
        
        boolean ret = mRtspEngineHandle.playbackByTime(mRtspEngineIndex, mUrl, mDevName, mPsw, mBeginT, mEndT);
        if (!ret) {
            int errorCode = mRtspEngineHandle.getLastError();
            mRtspEngineHandle.releaseRtspClientEngineer(mRtspEngineIndex);
            throw new NetException("RtspClient startRtspProc failed!", errorCode);
        }
        
        mState = PlaybackState.STREAM;
        mMessageCallback.onMessageCallback(PLAY_RTSP_SUCCESS, mUserId, 0);
        
        Log.d(LOG_TAG, "startRtsp >>> startRtsp step4");
    }
	
	private void stopRtsp() {
        // TODO Auto-generated method stub
        if (null != mRtspEngineHandle) {
            if (RtspClient.RTSPCLIENT_INVALIDATE_ENGINEID != mRtspEngineIndex) {
                mRtspEngineHandle.stopRtspProc(mRtspEngineIndex);
                mRtspEngineHandle.releaseRtspClientEngineer(mRtspEngineIndex);
                mRtspEngineIndex = RtspClient.RTSPCLIENT_INVALIDATE_ENGINEID;
            }
        }
    }
	
	@Override
	public void setParam(int model, String url, String name, String password) {
		// TODO Auto-generated method stub
		if(url == null || url.equals(""))
		{
			Log.d(LOG_TAG, "SetParam >>> url is null");
			return;
		}
		
		mUrl = url;
		mModel = model;
		
		mDevName = name;
		mPsw = password;
	}
	
	@Override
	public void pause() throws NetException {
		// TODO Auto-generated method stub
		if(mState != PlaybackState.DISPLAY) {
			Log.d(LOG_TAG, "pause >>> not in display state");
			return;
		}
		
		boolean ret = mRtspEngineHandle.pause(mRtspEngineIndex);
        if (!ret) {
            int errorCode = mRtspEngineHandle.getLastError();
            mRtspEngineHandle.releaseRtspClientEngineer(mRtspEngineIndex);
            throw new NetException("RtspClient pause failed!", errorCode);
        }
		
        mState = PlaybackState.PAUSE;
        
        mMessageCallback.onMessageCallback(PLAY_PAUSE_SUCCESS, mUserId, 0);
	}

	@Override
	public void resume() throws NetException {
		// TODO Auto-generated method stub
		if(mState != PlaybackState.PAUSE) {
			Log.d(LOG_TAG, "resume >>> not in pause state");
			return;
		}
		
		boolean ret = mRtspEngineHandle.resume(mRtspEngineIndex);
        if (!ret) {
            int errorCode = mRtspEngineHandle.getLastError();
            mRtspEngineHandle.releaseRtspClientEngineer(mRtspEngineIndex);
            throw new NetException("RtspClient resume failed!", errorCode);
        }
		
        mState = PlaybackState.DISPLAY;
        
        mMessageCallback.onMessageCallback(PLAY_RESUME_SUCCESS, mUserId, 0);
	}

	@Override
	public void slowPlay() throws NetException {
		// TODO Auto-generated method stub
		if(mState != PlaybackState.DISPLAY) {
			Log.d(LOG_TAG, "slowPlay >>> not in pause state");
			return;
		}
		
		boolean ret = mRtspEngineHandle.playbackSlow(mRtspEngineIndex);
        if (!ret) {
            int errorCode = mRtspEngineHandle.getLastError();
            mRtspEngineHandle.releaseRtspClientEngineer(mRtspEngineIndex);
            throw new NetException("RtspClient slowPlay failed!", errorCode);
        }
		
        mMessageCallback.onMessageCallback(PLAY_SLOW_SUCCESS, mUserId, 0);
	}

	@Override
	public void fastPlay() throws NetException {
		// TODO Auto-generated method stub
		if(mState != PlaybackState.DISPLAY) {
			Log.d(LOG_TAG, "fastPlay >>> not in pause state");
			return;
		}
		
		boolean ret = mRtspEngineHandle.playbackFast(mRtspEngineIndex);
        if (!ret) {
            int errorCode = mRtspEngineHandle.getLastError();
            mRtspEngineHandle.releaseRtspClientEngineer(mRtspEngineIndex);
            throw new NetException("RtspClient fastPlay failed!", errorCode);
        }
		
        mMessageCallback.onMessageCallback(PLAY_FAST_SUCCESS, mUserId, 0);
	}

	@Override
	public void normalPlay() throws NetException {
		// TODO Auto-generated method stub
		if(mState != PlaybackState.DISPLAY) {
			Log.d(LOG_TAG, "normalPlay >>> not in pause state");
			return;
		}
		
		boolean ret = mRtspEngineHandle.playbackNormal(mRtspEngineIndex);
        if (!ret) {
            int errorCode = mRtspEngineHandle.getLastError();
            mRtspEngineHandle.releaseRtspClientEngineer(mRtspEngineIndex);
            throw new NetException("RtspClient normalPlay failed!", errorCode);
        }
		
        mMessageCallback.onMessageCallback(PLAY_NORMAL_SUCCESS, mUserId, 0);
	}

	@Override
	public void setPosition(long startTime, long stopTime) throws NetException {
		// TODO Auto-generated method stub
		if(mState != PlaybackState.DISPLAY) {
			Log.d(LOG_TAG, "setPosition >>> not in pause state");
			return;
		}
		
		boolean ret = mRtspEngineHandle.setPlaybackPos(mRtspEngineIndex, mBeginT, mEndT);
        if (!ret) {
            int errorCode = mRtspEngineHandle.getLastError();
            mRtspEngineHandle.releaseRtspClientEngineer(mRtspEngineIndex);
            throw new NetException("RtspClient setPosition failed!", errorCode);
        }
		
        mMessageCallback.onMessageCallback(PLAY_SETPOS_SUCCESS, mUserId, 0);
	}

	@Override
	public PlaybackState GetState() {
		// TODO Auto-generated method stub
		return mState;
	}
	
	@Override
	public void setMessageCallback(MediaPlayerMessageCallback messageCallback,
			int useId) {
		// TODO Auto-generated method stub
		if(messageCallback == null)
		{
			Log.d(LOG_TAG, "setMessageCallback >>> messageCallback is null");
			return;
		}
		
		
		mMessageCallback = messageCallback;
		mUserId = useId;
	}

	@Override
	public void onDisplay(int arg0, ByteBuffer arg1, int arg2, int arg3,
			int arg4, int arg5, int arg6, int arg7) {
		// TODO Auto-generated method stub
		if (mState != PlaybackState.DISPLAY ) {
			 mState = PlaybackState.DISPLAY;
			 mMessageCallback.onMessageCallback(PLAY_DISPLAY_SUCCESS, mUserId, 0);
		}
		
		
	}

	@Override
	public void onDataCallBack(int handle, int dataType, byte[] data, int len,
			int timestamp, int packetNo, int useId) {
		// TODO Auto-generated method stub
		//Log.d(LOG_TAG, "onDataCallBack >>> handle["+handle+"]" +" len:"+len +" packetNo:"+packetNo);
		switch (dataType) {
	        case RtspClient.DATATYPE_HEADER:
	            boolean ret = processStreamHeader(data, len);
	            if (!ret) {
	            	Log.d(LOG_TAG, "MediaPlayer Header fail! such as:");
	            	Log.d(LOG_TAG, "MediaPlayer Header len:"+len);
	                mMessageCallback.onMessageCallback(MediaPlayer.START_OPEN_FAILED, mUserId, 0);
	            } else {
	                Log.d(LOG_TAG, "MediaPlayer Header success!");
	            }
	
	            break;
	        default:
	            processStreamData(data, len);
	            break;
	    }
	}

	@Override
	public void onMessageCallBack(int handle, int opt, int param1, int param2,
			int useId) {
		// TODO Auto-generated method stub
		
		
		Log.d(LOG_TAG, "onMessageCallBack(): handle:"+handle+" opt:"+ opt);
        if (opt == RtspClient.RTSPCLIENT_MSG_CONNECTION_EXCEPTION){
        	
        	if(mPlayerHandle.getSystemTime(mPlayerPort, mCurrentT)) {
        		mBeginT.setYear(mCurrentT.year);
        		mBeginT.setYear(mCurrentT.month);
        		mBeginT.setYear(mCurrentT.day);
        		mBeginT.setYear(mCurrentT.hour);
        		mBeginT.setYear(mCurrentT.min);
        		mBeginT.setYear(mCurrentT.sec);
        	}
        	else {
        		Log.d(LOG_TAG, "onMessageCallBack() >>> mPlayerHandle.getSystemTime fail");
        	}
        	
        	stop();
            
            try {
            	start(mSurfaceView);
            } catch (NetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
	}
	
	
	private boolean processStreamHeader(byte[] data, int len) {
        // TODO Auto-generated method stub
        if (-1 != mPlayerPort) {
            closePlayer();
        }
        
        boolean ret = openPlayer(data, len);
        if(!ret) {
            return false;
        }
        
        return ret;
    }
    
    private void processStreamData(byte[] data, int len) {
        // TODO Auto-generated method stub
        if (null == data || 0 == len) {
            Log.d(LOG_TAG, "processStreamData() Stream data error");
            return;
        }
        
        if (mPlayerHandle != null) {
            boolean ret = mPlayerHandle.inputData(mPlayerPort, data, len);
            if (!ret) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    private boolean openPlayer(byte[] data, int len) {
        // TODO Auto-generated method stub
        if (null == data || 0 == len) {
            Log.d(LOG_TAG, "openPlayer() Stream data error");
            return false;
        }

        if (null == mPlayerHandle) {
            Log.d(LOG_TAG, "openPlayer(): Player handle is null!");
            return false;
        }

        mPlayerPort = mPlayerHandle.getPort();
        if (-1 == mPlayerPort) {
            Log.d(LOG_TAG, "openPlayer(): Player port error!");
            return false;
        }

        boolean ret = mPlayerHandle.setStreamOpenMode(mPlayerPort, Player.STREAM_REALTIME);
        if (!ret) {
            int tempErrorCode = mPlayerHandle.getLastError(mPlayerPort);

            mPlayerHandle.freePort(mPlayerPort);
            mPlayerPort = -1;

            Log.d(LOG_TAG, "openPlayer(): Player setStreamOpenMode failed!" + tempErrorCode);
            return false;
        }

        ret = mPlayerHandle.openStream(mPlayerPort, data, len, 2 * 1024 * 1024);
        if (!ret) {
            Log.d(LOG_TAG, "openPlayer() mPlayerHandle.openStream failed!" + "Port: " + mPlayerPort
                    + "ErrorCode: " + mPlayerHandle.getLastError(mPlayerPort));
            return false;
        }

        ret = mPlayerHandle.setDisplayCB(mPlayerPort, this);
        if (!ret) {
            Log.d(LOG_TAG, "openPlayer() mPlayerHandle.setDisplayCB() failed!");
            return false;
        }

        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (null == surfaceHolder) {
            Log.d(LOG_TAG, "openPlayer() mPlayer mainSurface is null!");
            return false;
        }

        ret = mPlayerHandle.play(mPlayerPort, surfaceHolder);
        if (!ret) {
            Log.d(LOG_TAG, "openPlayer() mPlayerHandle.play failed!" + "Port: " + mPlayerPort
                    + "PlayView Surface: " + surfaceHolder);
            return false;
        }

        return true;
    }

    private void closePlayer() {
        // TODO Auto-generated method stub
        if (null != mPlayerHandle) {
            if (-1 != mPlayerPort) {
                boolean ret = mPlayerHandle.stop(mPlayerPort);
                if (!ret) {
                    Log.d(LOG_TAG, "closePlayer(): Player stop  failed!  errorCode:"+mPlayerHandle.getLastError(mPlayerPort));
                }

                ret = mPlayerHandle.closeStream(mPlayerPort);
                if (!ret) {
                    Log.d(LOG_TAG, "closePlayer(): Player closeStream  failed!");
                }

                ret = mPlayerHandle.freePort(mPlayerPort);
                if (!ret) {
                    Log.d(LOG_TAG, "closePlayer(): Player freePort  failed!");
                }
                
                mPlayerPort = -1;
            }
        }
    }
}
