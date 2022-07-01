package com.example.uitest;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

public class MediaUtils {
    private static MediaPlayer mediaPlayer;

    //开始播放
    public static void playRing(Context context){
        try {
            //用于获取手机默认铃声的Uri
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(context, alert);
            //告诉mediaPlayer播放的是铃声流
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //停止播放
    public static void stopRing(){
        if (mediaPlayer !=null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    }
    public static void maxRing(AudioManager localAudioManager){
        int currentVolume = localAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//获取当前音量
        localAudioManager.setStreamVolume(AudioManager.STREAM_RING, localAudioManager.getStreamMaxVolume(3), 4);
    }
}
