package com.hik.RtspClient.Live;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.MediaPlayer.PlayM4.Player;
import org.MediaPlayer.PlayM4.PlayerCallBack.PlayerDisplayCB;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hik.RtspClient.RtspClient;
import com.hik.RtspClient.RtspClientCallback;
import com.hik.RtspClient.Exception.NetException;
import com.hik.RtspClient.Util.Global;
import com.hik.RtspClient.Util.LivePlayer;
import com.hik.RtspClient.Util.LiveState;
import com.hik.RtspClient.Util.MediaPlayer;
import com.hik.RtspClient.Util.MediaPlayerMessageCallback;

public class Live extends LivePlayer implements RtspClientCallback, PlayerDisplayCB {

    private static final String        LOG_TAG                 = "Live";

    private String                     mUrl                    = "";
    private int                        mModel                  = RtspClient.RTPRTSP_TRANSMODE;

    private String                     mDevName                = "admin";
    private String                     mPsw                    = "12345";

    private SurfaceView                mSurfaceView;

    private Player                     mPlayerHandle;
    private int                        mPlayerPort             = -1;

    private RtspClient                 mRtspEngineHandle;
    private int                        mRtspEngineIndex        = RtspClient.RTSPCLIENT_INVALIDATE_ENGINEID;

    private MediaPlayerMessageCallback mMessageCallback;
    private int                        mUserId                 = -1;

    private LiveState                  mState                  = LiveState.RELEASE;

    private Context                    mContext                = null;

    private int                        mPlayViewIndex          = -1;

    private String                     sourceVideoFilePath     = "";
    private String                     thumbilVideoFilePath    = "";
    private final String               THUMBNAILDIR            = "thumbnails";
    private final String               videoFileFormate        = ".mp4";
    private final String               picFileFormate          = ".jpg";

    private ByteBuffer                 mStreamHeadDataBuffer   = null;
    private FileOutputStream           mRecordFileOutputStream = null;

    private boolean                    mRecordMark             = false;

    public Live(Context context) {
        // TODO Auto-generated constructor stub
        mContext = context;
        initialize();

        Log.d(LOG_TAG, "Live >>> Live success!");
    }

    private void initialize() {
        // TODO Auto-generated method stub
        mPlayerHandle = Player.getInstance();
        if (null == mPlayerHandle) {
            Log.d(LOG_TAG, "initialize >>> Player handle is null!");
        }

        // RtspClient.initLib();
        mRtspEngineHandle = RtspClient.getInstance();
        if (null == mRtspEngineHandle) {
            Log.d(LOG_TAG, "initialize >>> RtspClient handle is null!");
        }

        mState = LiveState.INIT;
    }

    @Override
    public void setParam(int model, String url, String name, String password) {
        // TODO Auto-generated method stub
        if (url == null || url.equals("")) {
            Log.d(LOG_TAG, "SetParam >>> url is null");
            return;
        }

        mUrl = url;
        mModel = model;

        mDevName = name;
        mPsw = password;
    }

    @Override
    public void start(SurfaceView surfaceView) throws NetException {
        // TODO Auto-generated method stub

        if (surfaceView == null) {
            Log.d(LOG_TAG, "Start >>> surfaceView is null");
            throw new NetException("start param null", 100);
        }

        mSurfaceView = surfaceView;

        if (mState == LiveState.STREAM) {
            Log.d(LOG_TAG, "start >>> player is playing");
            return;
        }

        startRtsp();
    }

    public void setPlayIndex(int playIndex) {
        mPlayViewIndex = playIndex;
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

        boolean ret = mRtspEngineHandle.startRtspProc(mRtspEngineIndex, mUrl, mDevName, mPsw);
        if (!ret) {
            int errorCode = mRtspEngineHandle.getLastError();
            mRtspEngineHandle.releaseRtspClientEngineer(mRtspEngineIndex);
            throw new NetException("RtspClient startRtspProc failed!", errorCode);
        }

        mState = LiveState.STREAM;
        mMessageCallback.onMessageCallback(PLAY_RTSP_SUCCESS, mUserId, 0);

        Log.d(LOG_TAG, "startRtsp >>> startRtsp step4");
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        if (mState == LiveState.INIT) {
            Log.d(LOG_TAG, "Stop >>> player have stoped");
            return;
        }

        stopRtsp();

        closePlayer();

        mMessageCallback.onMessageCallback(STOP_SUCCESS, mUserId, 0);

        mState = LiveState.INIT;
    }
public boolean getIsRecoding()
{
    return mRecordMark;}
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
    public void setMessageCallback(MediaPlayerMessageCallback messageCallback, int userId) {
        // TODO Auto-generated method stub
        if (messageCallback == null) {
            Log.d(LOG_TAG, "setMessageCallback >>> messageCallback is null");
            return;
        }

        mMessageCallback = messageCallback;
        mUserId = userId;
    }

    @Override
    public LiveState GetState() {
        // TODO Auto-generated method stub
        return mState;
    }

    @Override
    public void onDataCallBack(int handle, int dataType, byte[] data, int len, int timestamp, int packetNo, int useId) {
        // TODO Auto-generated method stub
        // Log.d(LOG_TAG, "onDataCallBack >>> handle["+handle+"]" +" len:"+len +" packetNo:"+packetNo);
        switch (dataType) {
            case RtspClient.DATATYPE_HEADER:
                boolean ret = processStreamHeader(data, len);
                if (!ret) {
                    Log.d(LOG_TAG, "MediaPlayer Header fail! such as:");
                    Log.d(LOG_TAG, "MediaPlayer Header len:" + len);
                    mMessageCallback.onMessageCallback(MediaPlayer.START_OPEN_FAILED, mUserId, 0);
                } else {
                    Log.d(LOG_TAG, "MediaPlayer Header success!");
                }

            break;
            default:
                processStreamData(data, len);
            break;
        }
        processRecordData(dataType, data, len);
    }

    /**
     * 录像数据处理
     * 
     * @param dataType 数据流
     * @param dataBuffer 数据缓存
     * @param dataLength 数据长度
     */
    private void processRecordData(int dataType, byte[] dataBuffer, int dataLength) {
        // Log.e("LiveControl", "processRecordData has data ");
        // if (mIsProcessStop) {
        // // Log.e("LiveControl", "processRecordData has stop it ");
        // dataBuffer = null;
        // return;
        // }
        switch (dataType) {
            case RtspClient.DATATYPE_HEADER:
                mStreamHeadDataBuffer = ByteBuffer.allocate(dataLength);
                for (int i = 0; i < mStreamHeadDataBuffer.capacity(); i++) {
                    mStreamHeadDataBuffer.put(dataBuffer[i]);
                }
            break;
            case RtspClient.DATATYPE_STREAM:
                if (mRecordMark) {
                    writeRecordStreamData(dataBuffer, dataLength, mRecordFileOutputStream);
                }
            break;
            default:
            break;
        }
        dataBuffer = null;
    }

    /**
     * 录像数据写到文件
     * 
     * @param file 录像文件
     * @param recordData 录像数据
     * @param length 录像数据长度
     * @since V1.0
     */
    private boolean writeRecordStreamData(byte[] recordData, int length, FileOutputStream fouts) {
        if (null == recordData || length <= 0) {
            return false;
        }

        if (null == fouts) {
            return false;
        }

        try {
            fouts.write(recordData, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean processStreamHeader(byte[] data, int len) {
        // TODO Auto-generated method stub
        if (-1 != mPlayerPort) {
            closePlayer();
        }

        boolean ret = openPlayer(data, len);
        if (!ret) {
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
            Log.d(LOG_TAG, "openPlayer() mPlayerHandle.openStream failed!" + "Port: " + mPlayerPort + "ErrorCode: "
                    + mPlayerHandle.getLastError(mPlayerPort));
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
            Log.d(LOG_TAG, "openPlayer() mPlayerHandle.play failed!" + "Port: " + mPlayerPort + "PlayView Surface: "
                    + surfaceHolder);
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
                    Log.d(LOG_TAG,
                            "closePlayer(): Player stop  failed!  errorCode:" + mPlayerHandle.getLastError(mPlayerPort));
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

    public byte[] capturePicture() {
        byte[] res = null;
        if (!checkIsHasReady())
            return res;
        Player.MPInteger width = new Player.MPInteger();
        Player.MPInteger height = new Player.MPInteger();
        mPlayerHandle.getPictureSize(mPlayerPort, width, height);

        int picSize = width.value * height.value * 3;
        if (picSize <= 0) {
            return res;
        }
        res = new byte[picSize];
        Player.MPInteger jpgSize = new Player.MPInteger();
        mPlayerHandle.getJPEG(mPlayerPort, res, picSize, jpgSize);
        if (jpgSize.value <= 0) {
            res = null;
        }

        return res;
    }

    public boolean startRecord(String SourceVideoRootPath) {
        boolean res = false;
        if (!checkIsHasReady())
            return res;
        if (SourceVideoRootPath == null)
            return res;
        if (SourceVideoRootPath.equals(""))
            return res;
        File file = new File(SourceVideoRootPath);
        if (!file.exists())
            file.mkdirs();
        sourceVideoFilePath = SourceVideoRootPath.endsWith("/") == true ? SourceVideoRootPath : SourceVideoRootPath
                + "/";
        thumbilVideoFilePath = sourceVideoFilePath + THUMBNAILDIR;
        thumbilVideoFilePath = thumbilVideoFilePath.endsWith("/") == true ? thumbilVideoFilePath : thumbilVideoFilePath
                + "/";
        file = new File(thumbilVideoFilePath);
        if (!file.exists())
            file.mkdirs();
        String fileName = Global.getCurrentDateStringWith();

        sourceVideoFilePath += fileName + videoFileFormate;
        thumbilVideoFilePath += fileName + picFileFormate;
        mRecordMark=true;
        byte[] thumbBT = capturePicture();
        if (thumbBT != null && thumbBT.length > 0) {
            try {
                // 开始写数据
                file = new File(thumbilVideoFilePath);
                if (!file.exists()) {

                    file.createNewFile();

                }
                FileOutputStream fout = new FileOutputStream(file);

                fout.write(thumbBT);
                fout.flush();
                fout.close();
                file=new File(sourceVideoFilePath);
                if (!file.exists()) {

                    file.createNewFile();

                }
                if(writeStreamHead(file))
                {
                res = true;
                }
                else
                {
                    removeFile(sourceVideoFilePath) ;
                    removeFile(thumbilVideoFilePath) ;
                    
                }
            } catch (FileNotFoundException e) {
                removeFile(sourceVideoFilePath) ;
                removeFile(thumbilVideoFilePath) ;
                e.printStackTrace();
            } catch (IOException e) {
                removeFile(sourceVideoFilePath) ;
                removeFile(thumbilVideoFilePath) ;
                e.printStackTrace();
            }
        }

        return res;
    }
    public boolean stopRecord()
    {
        boolean res=false;
        if (!mRecordMark) {
            res=true;
            return res;
        }
        mRecordMark = false;
        if (!checkIsHasReady()) {
            res=true;
            return res;

        }
        stopWriteStreamData();
       
        mRecordMark=false;
        res=true;
        return res;
    }
    /**
     * 删除文件
     * 
     * @param file 文件
     * @since V1.0
     */
    private void removeFile(String filePath) {
        if(filePath==null)
            return;
        if(filePath.equals(""))
            return;
        File file=new File(filePath);
        if (null == file || (!file.exists())) {
            return;
        }

        try {
            file.delete();
            file = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 停止写入数据流
     * 
     * @since V1.0
     */
    private void stopWriteStreamData() {
        if (null == mRecordFileOutputStream) {
            return;
        }

        try {
            mRecordFileOutputStream.flush();
            mRecordFileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mRecordFileOutputStream = null;
             
        }
    }
    /**
     * 写流头文件
     * 
     * @param file 写入的文件
     * @return true - 写入头文件成功. false - 写入头文件失败.
     * @since V1.0
     */
    private boolean writeStreamHead(File file) {
        if (null == file || null == mStreamHeadDataBuffer) {
            return false;
        }

        byte[] tempByte = mStreamHeadDataBuffer.array();
        if (null == tempByte) {
            return false;
        }

        try {
            if (null == mRecordFileOutputStream) {
                mRecordFileOutputStream = new FileOutputStream(file);
            }
            mRecordFileOutputStream.write(tempByte, 0, tempByte.length);
        } catch (Exception e) {
            e.printStackTrace();
            mRecordFileOutputStream = null;
            return false;
        }

        return true;
    }
    /**
     * 检查播放状态
     * 
     * @return true - 处于播放状态. false -处于不播放状态
     * @since V1.0
     */
    private boolean checkIsHasReady() {
        boolean res = false;
        if (mState == LiveState.DISPLAY && (null != mPlayerHandle && -1 != mPlayerPort)) {
            res = true;
        }

        return res;
    }

    @Override
    public void onMessageCallBack(int handle, int opt, int param1, int param2, int useId) {
        // TODO Auto-generated method stub
        Log.d(LOG_TAG, "onMessageCallBack(): handle:" + handle + " opt:" + opt);
        if (opt == RtspClient.RTSPCLIENT_MSG_CONNECTION_EXCEPTION) {

            stop();

            try {
                start(mSurfaceView);
            } catch (NetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            if (null != mMessageCallback) {

                mMessageCallback.onMessageCallback(handle, opt, mPlayViewIndex);
            }
        }
    }

    @Override
    public void onDisplay(int arg0, ByteBuffer arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7) {
        // TODO Auto-generated method stub
        if (mState != LiveState.DISPLAY) {
            mState = LiveState.DISPLAY;
            mMessageCallback.onMessageCallback(PLAY_DISPLAY_SUCCESS, mUserId, 0);
        }
    }

    /**
     * 发送消息
     * 
     * @param message 消息
     * @since V1.0
     */
    private void sendMessage(int message) {
        if (null != mMessageCallback) {
            mMessageCallback.onMessageCallback(message, 0, mPlayViewIndex);
        }
    }

}
