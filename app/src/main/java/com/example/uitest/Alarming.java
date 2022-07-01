package com.example.uitest;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import android.telephony.SmsManager;
import android.view.View;

import java.util.List;


public class Alarming {
    /**
     * 发送短信警报
     */
    public static void alarm(String phoneNumber){
                SmsManager smsManager=SmsManager.getDefault();
                List<String> list=smsManager.divideMessage("机主当前身体状况较差，请尽快联系就医");
                for(String sms:list){
                    smsManager.sendTextMessage(phoneNumber,null,sms,null,null);
                }
            }
}
