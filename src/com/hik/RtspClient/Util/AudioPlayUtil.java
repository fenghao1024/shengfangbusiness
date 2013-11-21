/* 
 * @ProjectName iVMS-5060_V3.0
 * @Copyright HangZhou Hikvision System Technology Co.,Ltd. All Right Reserved
 * 
 * @FileName AudioPlayUtil.java
 * @Description 这里对文件进行描述
 * 
 * @author mlianghua
 * @data Jun 28, 2012
 * 
 * @note 这里写本文件的详细功能描述和注释
 * @note 历史记录
 * 
 * @warning 这里写本文件的相关警告
 */
package com.hik.RtspClient.Util;



import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.util.Log;

/**
 * 在此对类做相应的描述
 * 
 * @author mlianghua
 * @Data Jun 28, 2012
 */
public class AudioPlayUtil{

    private static SoundPool mSoundPool = null;
    private static int       mSoundId   = -1;
    private static MediaPlayer player=null;

    private AudioPlayUtil() {
    };

    /**
     * 这里对方法做描述
     * 
     * @param context 上下文
     * @param rawFile 音频文件
     * @since V1.0
     */
    public static synchronized void playAudioFile2(Context context, int rawFile) {
        if (null == mSoundPool) {
            mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
            mSoundId = mSoundPool.load(context, rawFile, 1);
        }

        if (-1 != mSoundId && null != mSoundPool) {
            Log.e("onDrawTop", "play start soundid:"+mSoundId);
            mSoundPool.play(mSoundId, 1, 1, 1, 0, 1);
            Log.e("onDrawTop", "play finish");
        }
    }
    
    public static synchronized void playAudioFile(Context context, int rawFile) {
        if (null == player) {
            player = MediaPlayer.create(context, rawFile);
            player.setOnCompletionListener(new OnCompletionListener(){

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    player.release();
                    player=null;
                }});
            
        }
        if(player!=null)
            player.start();

        
    }

   
}
