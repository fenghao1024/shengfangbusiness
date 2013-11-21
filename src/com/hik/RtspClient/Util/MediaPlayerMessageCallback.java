package com.hik.RtspClient.Util;

public interface MediaPlayerMessageCallback {
    /**
     * 播放引擎消息回调接口
     * 
     * @param message 消息
     * @param useId 消息接收者
     * @param useData 消息数据
     * @since V1.0
     */
    public void onMessageCallback(int message, int useId, int useData);
}
