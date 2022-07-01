package com.example.uitest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


public class PushService extends Service {
    //private PendingIntent PendingIntent = null;
    //通知栏消息
    //public Notification notification = null;
    //public Intent intent=null;
    private String title="您有新消息";
    final MessageThread thread = new MessageThread();
    private String text="这是一条新的测试消息";

    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate(){
        super.onCreate();
        Log.d("--create--","service is created");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("--onStartCom--","started");
        //开启线程
        thread.isRunning = true;
        thread.start();
        return super.onStartCommand(intent, flags, startId);
//
    }
    class MessageThread extends Thread{
        //运行状态
        public boolean isRunning = true;
        public int notificationID = 1000;
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            //初始化
            // 获取NotificationManager对象
            NotificationManager manager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String id = "channel_1";
            String name = getString(R.string.app_name);
            // 创建NotificationChannel对象，传入id name 和 重要级别
            NotificationChannel channel =
                    new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            //channel.canBypassDnd();//是否绕过请勿打扰模式
//          channel.enableLights(true);//闪光灯
//          channel.setLightColor(Color.RED);//闪关灯的灯光颜色
//          channel.shouldShowLights();//是否会有灯光
//          channel.setLockscreenVisibility(VISIBILITY_);//锁屏显示通知
            channel.canShowBadge();//桌面launcher的消息角标
            channel.enableVibration(true);//是否允许震动
            //channel.getAudioAttributes();//获取系统通知响铃声音的配置
            //channel.getGroup();//获取通知取到组
            channel.setBypassDnd(true);//设置可绕过 请勿打扰模式
            channel.setVibrationPattern(new long[]{100, 100, 200});//设置震动模式

            // 创建通知的通道
            manager.createNotificationChannel(channel);

            //setText("123");
            // Builder中传入上下文对象和通道id
            NotificationCompat.Builder builder1 = new NotificationCompat
                    .Builder(PushService.this, id)
                    .setWhen(System.currentTimeMillis()) //发送时间
                    .setSmallIcon(R.mipmap.ic_launcher)//设置图标
                    .setOngoing(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                    .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                    .setContentTitle(title)//设置标题
                    .setContentText(text)//消息内容
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
            //获取消息线程
            //private MessageThread thread = null;
            //点击查看
            Intent intent = new Intent(PushService.this, MainActivity.class); //设置点击跳转的activity
            PendingIntent pendingIntent = PendingIntent.getActivity(PushService.this, 0, intent, 0);
            builder1.setContentIntent(pendingIntent);
            Notification notification = builder1.build();
                    while(isRunning){
                        manager.notify(notificationID, notification);// 通过通知管理器发送通知
                        //避免覆盖消息，采取ID自增
                        notificationID++;
                        try {
                            //休息10秒
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
        }
    }
    public String setTitle(String title){
        this.title=title;
        return  title;
    }
    public String setText(String text){
        this.text=text;
        return title;
    }
    public void Destroy(){
        super.onDestroy();
    }
}
