package com.hik.RtspClient;

/**
 * RtspClient类,负责获取实时数据、获取远程录像数据
 * @author weilinfeng
 * @Data 2013-7-4
 */
public class RtspClient {

    static 
    {
        System.loadLibrary("gnustl_shared");
        System.loadLibrary("RtspClientSDK");
    }
    
    /** invalidate engind id*/
    public static final int RTSPCLIENT_INVALIDATE_ENGINEID           = -1;
    /** RTP over TCP */
    public static final int         RTPTCP_TRANSMODE             = 0x0000;
    /** RTP over UDP */
    public static final int         RTPUDP_TRANSMODE             = 0x0001;
    /** RTP over RTSP */
    public static final int         RTPRTSP_TRANSMODE            = 0x0003;
    /** Reliable RTP over UDP */
    public static final int         RTPUDP_RELIABLE_TRANSMODE    = 0x0004;
    /** RTP over multicast */
    public static final int         RTPMCAST_TRANSMODE           = 0x0005;
    /** hikvision data header flag */
    public static final int         DATATYPE_HEADER              = 1;
    /** hikvision data flag */
    public static final int         DATATYPE_STREAM              = 2;
    /** 回放数据接收完成的标识 */
    public static final int RTSPCLIENT_MSG_PLAYBACK_FINISH       = 0x0100; 
    /** rtp buffer overflow exception*/
    public static final int RTSPCLIENT_MSG_BUFFER_OVERFLOW       = 0x0101;
    /** rtsp连接异常，建议关闭重新请求 */
    public static final int RTSPCLIENT_MSG_CONNECTION_EXCEPTION  = 0x0102;
    
    /** rtspclient instance*/
    private static RtspClient mRtspClient               = null;

    
    /**
     * private RtspClient Structure
     */
    private RtspClient(){
        RtspClient.initLib();
    };
    
    /**
     * get RtspClient instance
     * 
     * @return RtspClient对象
     * @since V1.0
     */
    public static RtspClient getInstance() {
        if (null == mRtspClient) {
            synchronized(RtspClient.class) {
                mRtspClient = new RtspClient();
            }
        }
        
        return mRtspClient;
    }
    
    /**
     * init RtspClient 
     * 
     * @return true - 成功 or false - 失败
     * @since V1.0
     */
    private static native boolean initLib();

    /**
     * fini RtspClient
     * 
     * @return true - 成功 or false - 失败
     * @since V1.0
     */
    private static native boolean finiLib();

    /**
     * 
     * 创建一个取流引擎对象，用于从流媒体获取实时数据或者回放数据；
     * 创建成功都会分配一个0到32之间的引擎id，作为引擎的唯一标识，注意：每次使用完创建引擎之后，必须释放，否则32路引擎耗尽，将无法继续创建；
     * 对于实时预览该标识可用于：开始取流、停止取流、释放引擎
     * 对于远程回放，该引擎可用于：（开始回放、停止）、随机定位、（快进、慢放、正常）、（暂停、恢复）、释放引擎
     * @param aRtspClientObserver - 视频数据和消息的回调函数
     * @param tansMethod - rtp data 传输方式（rtp or rtsp、rtp or tcp、rtp or udp），建议使用rtp or rtsp
     * @param useId
     * @return 引擎id
     * @since V1.0
     */
    public native int createRtspClientEngine(RtspClientCallback aRtspClientObserver, int tansMethod);

    /**
     * 
     * 释放引擎
     * @param 引擎 id
     * @return true - 释放成功  or false - 释放失败
     * @since V1.0
     */
    public native boolean releaseRtspClientEngineer(int iEngineerID);

    /**
     * 
     * 开始获取实时视频数据（为了兼容老的iVMS-5060程序而保留的接口，不建议不要使用）
     * @param 引擎 id
     * @param 视频数据的url
     * @return true - 开始成功  or false - 开始失败
     * @since V1.0
     */
    public native boolean startRtspProc(int iEngineerID, String strRtspUrl);
    
    /**
     * 开始获取实时视频数据（建议使用）
     * 
     * @param 引擎 id
     * @param 视频数据的url
     * @return true - 开始预览成功  or false - 开始预览失败
     * @since V1.0
     */
    public native boolean startRtspProc(int iEngineerID, String strRtspUrl, String deviceName, String devicePsw);

    /**
     * 
     * 停止获取数据，此接口可用于停止实时数据和回放数据的获取，即：(startRtspProc-stopRtspProc),(playbackByTime-stopRtspProc)
     * @param 引擎 id
     * @return true - 停止成功  or false - 停止失败
     * @since V1.0
     */
    public native boolean stopRtspProc(int iEngineerID);
    /**
     * 开始获取回访数据
     * @param iEngineerID - 引擎id
     * @param playbackUrl - 回访数据的url
     * @param deviceName - 设备名称
     * @param devicePsw - 设备密码
     * @param beginT - 开始回放的时间点
     * @param endT - 结束回放的时间点
     * @return true - 开启回放成功  or false - 开启回放失败
     * @see 
     * @since V1.0
     */
    public native boolean playbackByTime(int iEngineerID, String playbackUrl, String deviceName, String devicePsw, ABS_TIME beginT, ABS_TIME endT);
    
    /**
     * 调用playbackByTime成功之后，调用此接口随机定位到某个有录像时间点开始播放
     * @param iEngineerID - 引擎id
     * @param beginT - 开始播放时间点
     * @param endT - 结束播放的时间点
     * @return true - 随机定位成功  or false - 随机定位失败
     * @see 
     * @since V1.0
     */
    public native boolean setPlaybackPos(int iEngineerID, ABS_TIME beginT, ABS_TIME endT);
    
    /**
     * 调用playbackByTime成功之后,调用此接口来暂停回放
     * @param iEngineerID
     * @return true - 暂停成功  or false - 暂停定位失败
     * @see 
     * @since V1.0
     */
    public native boolean pause(int iEngineerID);
    
    /**
     * 调用playbackByTime成功之后,调用此接口来继续回放
     * @param iEngineerID - 引擎id
     * @return true - 恢复成功  or false - 恢复失败
     * @see 
     * @since V1.0
     */
    public native boolean resume(int iEngineerID);
    
    /**
     * 调用playbackByTime成功之后,调用此接口来加快码服务器发送码流速度
     * @param iEngineerID
     * @return
     * @see 
     * @since V1.0
     */
    public native boolean playbackFast(int iEngineerID);
    
    /**
     * 调用playbackByTime成功之后,调用此接口减缓服务器发送码流速度
     * @param iEngineerID
     * @return
     * @see 
     * @since V1.0
     */
    public native boolean playbackSlow(int iEngineerID);
    
    /**
     * 调用playbackByTime成功之后,调用此接口使服务器以正常速度发送码流
     * @param iEngineerID
     * @return
     * @see 
     * @since V1.0
     */
    public native boolean playbackNormal(int iEngineerID);
    
    /**
     * 
     * 获取RtspClientSDK的版本号
     * @return 版本号
     * @since V1.0
     */
    public native String getRtspClientVision();

    /**
     * 
     * 获取错误码
     * @return 错误码
     * @since V1.0
     */
    public native int getLastError();
    
    
}
