package com.example.uitest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.view.KeyEvent;

import androidx.annotation.RequiresApi;

public class WaringDialog {
    public static void dialog(Context context, AudioManager localAudioManager,int currentVolume){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("尽快就医?");
        builder.setPositiveButton("收到", new DialogInterface.OnClickListener(){

            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                MediaUtils.stopRing();
                VibrateUtils.vibrateCancel(context);
                localAudioManager.setStreamVolume(AudioManager.STREAM_RING,currentVolume,AudioManager.FLAG_PLAY_SOUND);
                localAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }

        });

        AlertDialog alertDialog = builder.create();

        alertDialog.setCancelable(false);//无法通过点击屏幕其他位置关闭弹窗
        /*设置按键监听，使弹窗无法通过返回键关闭*/
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener(){
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_SEARCH)
                {
                    return true;
                }
                else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN|keyCode == KeyEvent.KEYCODE_VOLUME_MUTE){
                    localAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    MediaUtils.maxRing(localAudioManager);//无法通过音量键降低声音或静音
                    return true;
                }
                else
                {
                    return false; //默认返回 false
                }
            }
        });


        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLUE);
    }

}
