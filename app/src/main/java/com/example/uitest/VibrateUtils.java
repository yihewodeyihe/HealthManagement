package com.example.uitest;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

public class VibrateUtils {
    /**
     * 让手机振动milliseconds毫秒
     */
    public static void vibrate(Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        if(vib.hasVibrator()){  //判断手机硬件是否有振动器
            vib.vibrate(milliseconds);
        }
    }

    /**
     * 让手机以我们自己设定的pattern[]模式振动
     * long pattern[] = {1000, 2000, 1000, 1000, 3000};
     */
    public static void vibrate(Context context, long[] pattern){
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        if(vib.hasVibrator()){
            vib.vibrate(pattern,0);
        }
    }

    /**
     * 取消震动
     */
    public static void vibrateCancel(Context context){
        //关闭震动
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.cancel();
    }
}
