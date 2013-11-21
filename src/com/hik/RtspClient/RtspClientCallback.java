package com.hik.RtspClient;

/**
 * 数据和消息回调接口
 * 
 * @author weilinfeng
 * @Data 2012-6-28
 */
public interface RtspClientCallback 
{
    /**
      * 数据回调函数
      * @param handle - 引擎id
      * @param dataType - 数据类型，决定data数据的类型,包括DATATYPE_HEADER和DATATYPE_STREAM两种类型
      * @param data 回调数据,分为：header数据和stream数据，由datatype作区分，header用于初始化播放库
      * @param len - data 数据的长度
      * @param timestamp - 时间戳（保留）
      * @param packetNo - rtp包号（保留）
      * @param useId - 用户数据，默认就是引擎id与handle相同
      * @since V1.0
      */
    public void onDataCallBack(int handle, int dataType, byte[] data, int len, int timestamp, int packetNo, int useId);

    /**
      * 消息回调函数
      * @param handle - 引擎id
      * @param opt - 回调消息，包括：RTSPCLIENT_MSG_PLAYBACK_FINISH,RTSPCLIENT_MSG_BUFFER_OVERFLOW,RTSPCLIENT_MSG_CONNECTION_EXCEPTION 三种
      * @param param1 - 保留参数
      * @param param2 - 保留参数
      * @param useId - 用户数据，默认就是引擎id与handle相同
      * @since V1.0
      */
    public void onMessageCallBack(int handle, int opt, int param1, int param2, int useId);
}
