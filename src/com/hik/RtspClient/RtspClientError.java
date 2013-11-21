package com.hik.RtspClient;

/**
 * 错误码类
 * @author weilinfeng
 * @Data 2013-7-4
 */
public class RtspClientError 
{
    /**
     * 私有化构造函数，防止实例化
     */
    private RtspClientError(){}
    
    /** HPR初始化失败 */
    public static final int RTSPCLIENT_HPR_INIT_FAIL 						= 1;  
    /** 消息队列启动失败 */
    public static final int RTSPCLIENT_MSG_MANAGE_RUN_FAIL					= 2;  
    /** RtspClient未初始化 */
    public static final int RTSPCLIENT_NO_INIT								= 3;  
    /** 协议不支持 */
    public static final int RTSPCLIENT_PROTOCOL_NOTSUPPORT      			= 4;  
    /** 回调参数为空 */
    public static final int RTSPCLIENT_CALLBACK_PARAM_NULL					= 5;  
    /** 创建Rtsp引擎失败 */
    public static final int RTSPCLIENT_MOLLOC_RTSPENGINE_FAIL				= 6;  
    /** 分配Rtsp引擎数超过最大数（32） */
    public static final int RTSPCLIENT_RTSPENGINE_EXCEED_ERROR				= 7;  
    /** 无效的引擎ID */
    public static final int RTSPCLIENT_ENGINEID_INVALID						= 8;  
    /** 引擎不存在 */
    public static final int RTSPCLIENT_ENGINE_NOT_EXSIT						= 9;  
    /** 引擎为空 */
    public static final int RTSPCLIENT_ENGINE_NULL							= 10; 
    /** 开始时间为空 */
    public static final int RTSPCLIENT_START_TIME_NULL						= 11;  
    /** 设备名称或者密码为空 */
    public static final int RTSPCLIENT_DEV_NAMEORPSW_NULL					= 12;  
    /** 错误的基础端口号 */
    public static final int RTSPCLIENT_BASEPORT_ERROR						= 13;  
    /** 内存分配失败 */
    public static final int RTSPCLIENT_MALLOC_MEMORY_FAIL					= 14;  
    /** 引擎未初始化 */
    public static final int RTSPCLIENT_ENGINEER_NOINIT						= 15;  
    /** MAG、VTDU4.0不支持Rtp or Tcp */
    public static final int RTSPCLIENT_VERSION_PROTOCOL_NOTSUPPORT			= 16;  
    /** Rtsp地址错误 */
    public static final int RTSPCLIENT_RTSPURL_ERROR						= 17;  
    /** 连接流媒体服务器失败 */
    public static final int RTSPCLIENT_CONNECT_SERVER_FAIL					= 18;  
    /** 不在播放状态 */
    public static final int RTSPCLIENT_NO_PLAY_STATE						= 19;	 
    /** Describe重定向失败 */
    public static final int RTSPCLIENT_DECRIBE_REDIRCT_FAIL					= 101;    
    /** 生成Describe信令失败 */
    public static final int RTSPCLIENT_GENERATE_DESCRIBE_FAIL				= 102;  
    /** 发送Describe信令失败 */
    public static final int RTSPCLIENT_SEND_DESCRIBE_FAIL					= 103;  
    /** 接受Describe信令失败 */
    public static final int RTSPCLIENT_RECV_DESCRIBE_FAIL					= 104;  
    /** Describe回复非200OK */
    public static final int RTSPCLIENT_DESCRIBE_STATUS_NO_200OK				= 105;  
    /** Describe解析失败 */
    public static final int RTSPCLIENT_PARSE_DESCRIBE_FAIL					= 106;  
    /** 生成Setup信令失败 */
    public static final int RTSPCLIENT_GENERATE_SETUP_FAIL					= 201;  
    /** 发送Setup信令失败 */
    public static final int RTSPCLIENT_SEND_SETUP_FAIL						= 202;  
    /** 接收Setup信令失败 */
    public static final int RTSPCLIENT_RECV_SETUP_FAIL						= 203;  
    /** Setup回复非200OK */
    public static final int RTSPCLIENT_SETUP_STATUS_NO_200OK				= 204;  
    /** 解析Setup失败 */
    public static final int RTSPCLIENT_PARSE_SETUP_FAIL						= 205;  
    /** 生成Play信令失败 */
    public static final int RTSPCLIENT_GENERATE_PLAY_FAIL					= 301;  
    /** 发送Play信令失败 */
    public static final int RTSPCLIENT_SEND_PLAY_FAIL						= 302;  
    /** 接收Play信令失败 */
    public static final int RTSPCLIENT_RECV_PLAY_FAIL						= 303;  
    /** Play回复非200OK */
    public static final int RTSPCLIENT_PLAY_STATUS_NO_200OK					= 304;  
    /** 创建改变速率信令失败 */
    public static final int RTSPCLIENT_GENERATE_CHANGERATE_FAIL				= 401;  
    /** 发送改变速率信令失败 */
    public static final int RTSPCLIENT_SEND_CHANGERATE_FAIL					= 402;  
    /** 接收改变速率信令失败 */
    public static final int RTSPCLIENT_RECV_CHANGERATE_FAIL					= 403;  
    /** 改变速率回复非200OK */
    public static final int RTSPCLIENT_CHANGERATE_STATUS_NO_200OK			= 404;  
    /** 创建强制I帧信令失败 */
    public static final int RTSPCLIENT_GENERATE_FORCEIFRAME_FAIL			= 501;  
    /** 发送强制I帧信令失败 */
    public static final int RTSPCLIENT_SEND_FORCEIFRAME_FAIL				= 502;  
    /** 接收强制I帧信令失败 */
    public static final int RTSPCLIENT_RECV_FORCEIFRAME_FAIL				= 503;  
    /** 强制I帧回复非200OK */
    public static final int RTSPCLIENT_FORCEIFRAME_STATUS_NO_200OK			= 504;  
    /** 创建随机定位信令失败 */
    public static final int RTSPCLIENT_GENERATE_RANDOMPLAY_FAIL				= 601;  
    /** 发送随机定位信令失败 */
    public static final int RTSPCLIENT_SEND_RANDOMPLAY_FAIL					= 602;  
    /** 接收随机定位信令失败 */
    public static final int RTSPCLIENT_RECV_RANDOMPLAY_FAIL					= 603;  
    /** 随机定位回复非200OK */
    public static final int RTSPCLIENT_RANDOMPLAY_STATUS_NO_200OK			= 604;  
    /** 创建暂停信令失败 */
    public static final int RTSPCLIENT_GENERATE_PAUSE_FAIL					= 701;  
    /** 发送暂停信令失败 */
    public static final int RTSPCLIENT_SEND_PAUSE_FAIL						= 702;  
    /** 接收暂停信令失败 */
    public static final int RTSPCLIENT_RECV_PAUSE_FAIL						= 703;  
    /** 暂停回复非200OK */
    public static final int RTSPCLIENT_PAUSE_STATUS_NO_200OK				= 704;  
    /** 不在暂停状态 */
    public static final int RTSPCLIENT_NOT_PAUSE_STATE_FAIL					= 705;  
    /** 创建恢复信令失败 */
    public static final int RTSPCLIENT_GENERATE_RESUME_FAIL					= 801;  
    /** 发送恢复信令失败 */
    public static final int RTSPCLIENT_SEND_RESUME_FAIL						= 802;  
    /** 接收恢复信令失败 */
    public static final int RTSPCLIENT_RECV_RESUME_FAIL						= 803;  
    /** 恢复回复非200OK */
    public static final int RTSPCLIENT_RESUME_STATUS_NO_200OK				= 804;  
    /** 创建RtpUdp引擎失败 */
    public static final int RTSPCLIENT_MOLLOC_RTPUDPENGINE_FAIL				= 901;  
    /** 初始化RtpUdp引擎失败 */
    public static final int RTSPCLIENT_INIT_RTPUDPENGINE_FAIL				= 902;  
    /** 开始RtpUdp引擎失败 */
    public static final int RTSPCLIENT_START_RTPUDPENGINE_FAIL				= 903;  
    /** 创建RtcpUdp引擎失败 */
    public static final int RTSPCLIENT_MOLLOC_RTCPUDPENGINE_FAIL			= 1001;  
    /** 初始化RtcpUdp引擎失败 */
    public static final int RTSPCLIENT_INIT_RTCPUDPENGINE_FAIL				= 1002;  
    /** 开始RtcpUdp引擎失败 */
    public static final int RTSPCLIENT_START_RTCPUDPENGINE_FAIL				= 1003;  
    /** 创建RtpTcp引擎失败 */
    public static final int RTSPCLIENT_MOLLOC_RTPTCPENGINE_FAIL				= 1101;  
    /** 初始化RtpTcp引擎失败 */
    public static final int RTSPCLIENT_INIT_RTPTCPENGINE_FAIL				= 1102;  
    /** 开始 RtpTcp引擎失败 */
    public static final int RTSPCLIENT_START_RTPTCPENGINE_FAIL				= 1103;  
    /** 创建RtpRtsp引擎失败 */
    public static final int RTSPCLIENT_MOLLOC_RTPRTSPENGINE_FAIL		    = 1201;  
    /** 初始化RtpRtsp引擎失败 */
    public static final int RTSPCLIENT_INIT_RTPRTSPENGINE_FAIL				= 1202;  
    /** 开始RtpRtsp引擎失败 */
    public static final int RTSPCLIENT_START_RTPRTSPENGINE_FAIL				= 1203;  
}
