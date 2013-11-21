package icedot.work.activity;

import icedot.work.adapter.ListView_Text_Adapter;
import icedot.work.shengfang.business.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hik.RtspClient.RtspClient;
import com.hik.RtspClient.Exception.NetException;
import com.hik.RtspClient.Live.Live;
import com.hik.RtspClient.Util.LivePlayer;
import com.hik.RtspClient.Util.LiveState;
import com.hik.RtspClient.Util.MediaPlayerMessageCallback;

public class VidioMgrActivity extends Activity implements MediaPlayerMessageCallback,
SurfaceHolder.Callback
{
	private ProgressBar _progressBar;	
	private ListView  _lv_titleList;		//列表
	private ListView_Text_Adapter _adapter;
	private TextView _text_playTitle;		//当前的标题
	private Map<String,String> _vidioList;
	
	private int  _vidioPlay;				//视频是否在播放
	private static final int STOP_VIDIO = 0;
	private static final int PLAY_VIDIO = 1;
	private static final int CHANGE_VIDIO = 2;
	//private static final int STARTFAIL_VIDIO=-1;
	
	private static final String LOG_TAG           = "VidioMgrActivity";
	protected static final int  START_THREAD_FAIL = 1;
	private Live				_hikLive;
	private Thread              _startThread	  = null;
    private Thread              _stopThread		  = null;
    private SurfaceView  		_mainView;		//视频输出的View
	private Handler             _handler          = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_vidiomgr);
		
		initView();
		initData();
	}
	
	@Override
	protected void onDestroy()
	{
		stopVideo();
		super.onDestroy();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) 
	{
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		
	}

	@Override
	public void onMessageCallback(int message, int useId, int useData) 
	{
		sendMessage(message, useId, useData);
	}

	private void initView() 
	{
		_mainView = (SurfaceView) findViewById(R.id.vidiomgr_video_main);
		_mainView.getHolder().addCallback(this);
		_progressBar = (ProgressBar) findViewById(R.id.vidiomgr_progress_bar);
		_lv_titleList = (ListView) findViewById(R.id.vidiomgr_listview_titlelist);
		_text_playTitle = (TextView) findViewById(R.id.vidiomgr_text_playtitle);
	}
	
	private void initData() 
	{
		_vidioPlay = STOP_VIDIO;
		_vidioList = new HashMap<String, String>();
		_vidioList.put("景区二","rtsp://219.151.10.234:554/pag://192.168.1.10:7302:sf01:0:SUB:TCP:admin:12345:MCU");
		_vidioList.put("景区一","rtsp://219.151.10.234:554/pag://192.168.1.10:7302:sf01:0:SUB:TCP:admin:12345:MCU");		
		_adapter = new ListView_Text_Adapter(this);
		
		Set<String> keyString = _vidioList.keySet();
		for(String title : keyString)
		{
			_adapter.get_list().add(title);
		}
		
		_lv_titleList.setAdapter(_adapter);
		_lv_titleList.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				//Log.d("ListViewItemClick", "arg2=" + arg2);				
				if(_vidioPlay == STOP_VIDIO)
				{
					_progressBar.setVisibility(View.VISIBLE);
					startVideo();
					_text_playTitle.setText(_adapter.get_list().get(arg2));
				}
				else if(_vidioPlay == CHANGE_VIDIO)
				{
					//_text_playTitle.setText(_adapter.get_list().get(arg2));
					Log.d(LOG_TAG,"The _vidioPlay = " + CHANGE_VIDIO);
				}
				else if(_vidioPlay == PLAY_VIDIO)
				{
					_vidioPlay = CHANGE_VIDIO;
					stopVideo();	
					_progressBar.setVisibility(View.VISIBLE);	
					_text_playTitle.setText(_adapter.get_list().get(arg2));
					
					VidioTask task = new VidioTask();
					task.execute();
				}
			}
		});
		
		_hikLive = new Live(this);
		_hikLive.setMessageCallback(this, 0);
		if(_vidioList.size() > 0)
		{
			_lv_titleList.setSelection(0);
			String key = (String)_lv_titleList.getItemAtPosition(0);
			_text_playTitle.setText(key);
			_hikLive.setParam(RtspClient.RTPRTSP_TRANSMODE,
					_vidioList.get(key), "admin", "12345");
			
			Log.d(LOG_TAG,_vidioList.get(key));
		}
		_progressBar.setVisibility(View.VISIBLE);
		_handler = createMessageHandler();
		
		//开始播放
		if (_hikLive.GetState() == LiveState.INIT) 
		{
			if (_startThread != null) 
            {
                if (!_startThread.isAlive()) 
                {
                    startVideo();
                }
            } 
            else 
            {
                startVideo();
            }
		}
//		else
//		{
//			if (_stopThread != null) 
//            {
//                if (!_stopThread.isAlive())
//                {
//                    stopVideo();
//                }
//            }
//            else 
//            {
//                stopVideo();
//            }
//		}
	}
	
	private void startVideo() 
	{
        _startThread = new Thread(new Runnable() 
        {
        	@Override
            public void run() 
            {
                Log.d(LOG_TAG, "startVideo >>> step1 ");

                try 
                {
                    if (_stopThread != null) 
                    {
                        _stopThread.join();
                    }
                }
                catch (InterruptedException e1) 
                {
                    e1.printStackTrace();
                }

                Log.d(LOG_TAG, "startVideo >>> step2 ");

                if (_hikLive.GetState() == LiveState.STREAM || 
                		_hikLive.GetState() == LiveState.DISPLAY) 
                {
                    Log.d(LOG_TAG, "startVideo >>> mVideoSurface is starting ");
                    return;
                }

                Log.d(LOG_TAG, "startVideo >>> step3 ");

                try 
                {
                	_hikLive.start(_mainView);
                } 
                catch (NetException e)
                {
                    e.printStackTrace();
                    sendMessage(START_THREAD_FAIL, 0, 0);
                }

                Log.d(LOG_TAG, "mStartThread >>> step4 ");
            }

        }, "StartThread");

        _startThread.start();
    }
	
	private void stopVideo() 
	{
        _stopThread = new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                try 
                {
                    if (_startThread != null) 
                    {
                        _startThread.join();
                    }
                } 
                catch (InterruptedException e1) 
                {
                    e1.printStackTrace();
                }

                if (_hikLive.GetState() == LiveState.INIT) 
                {
                    Log.d(LOG_TAG, "stopVideo >>> mVideoSurface have stoped ");
                    return;
                }

                _hikLive.stop();
            }
        }, "StopThread");

        _stopThread.start();
    }
	
	public void image_stop_onClick(View v)
	{
		stopVideo();
		_text_playTitle.setText("请选择视频地点");
		//this.finish();
	}
	
	 private Handler createMessageHandler() 
	 {
	        Handler hanlder = new Handler() 
	        {
	            @Override
	            public void handleMessage(Message msg) 
	            {
	                switch (msg.what) {
	                    case LivePlayer.PLAY_RTSP_SUCCESS:
	                        handleRtspSuccess(msg.arg1);
	                    break;
	                    case LivePlayer.PLAY_DISPLAY_SUCCESS:
	                        handleDisPlaySuccess(msg.arg1);
	                    break;
	                    case LivePlayer.STOP_SUCCESS:
	                        handleStopSuccess(msg.arg1);
	                    break;
	                    case LivePlayer.START_OPEN_FAILED:
	                        handleDisplayFail(msg.arg1);
	                    break;
	                    case START_THREAD_FAIL:
	                        handleStartThreadFail(msg.arg1);
	                    break;
	                }
	            }

	            private void handleStartThreadFail(int arg1) 
	            {
	                _progressBar.setVisibility(View.GONE);
	                Log.d(LOG_TAG,"handleStartThreadFail");
	            }

	            private void handleDisplayFail(int arg1) 
	            {
	            	Log.d(LOG_TAG,"handleDisplayFail");
	            }

	            private void handleStopSuccess(int arg1)
	            {
	            	_progressBar.setVisibility(View.GONE);
	            	Log.d(LOG_TAG,"handleStopSuccess");
	            	_vidioPlay = STOP_VIDIO;
	            }

	            private void handleDisPlaySuccess(int arg1) 
	            {
	            	_progressBar.setVisibility(View.GONE);
	            	Log.d(LOG_TAG,"handleDisPlaySuccess");
	            	_vidioPlay = PLAY_VIDIO;
	            }

	            private void handleRtspSuccess(int arg1) 
	            {
	            	Log.d(LOG_TAG,"handleRtspSuccess");
	            }

	        };
	        return hanlder;

	    }
	
    /**
     * 发送消息
     * 
     * @param msg - 消息对象
     * @param arg1 - 错误码
     * @since V1.0
     */
    private void sendMessage(int msg, int arg1, int arg2)
    {
        if (_handler == null) {
            _handler = createMessageHandler();
        }

        Message message = Message.obtain();
        message.what = msg;
        message.arg1 = arg1;
        message.arg2 = arg2;
        _handler.sendMessage(message);
    }
    
    private class VidioTask extends AsyncTask<Void, Void, Integer>
    {
    	@Override
    	protected Integer doInBackground(Void... params) 
    	{
    		try
    		{
	    		while(_vidioPlay != STOP_VIDIO)
	    		{
	    			Thread.sleep(500);
	    		}
    		}
    		catch (Exception e) 
    		{  
    			e.printStackTrace(); 
    		} 

    		//String key = (String)_lv_titleList.getSelectedItem();
			_hikLive.setParam(RtspClient.RTPRTSP_TRANSMODE,
					_vidioList.get("景区二"), "admin", "12345");
			
			Log.d(LOG_TAG,_vidioList.get("景区二"));
			VidioMgrActivity.this.startVideo();
			
    		return 0;
    	}
    }
}
