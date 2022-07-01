package com.example.uitest;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import android.os.Message;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TimeZone;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private BodyStateFragment firstFragment = null;// 用于显示身体数据界面
    private RecipeRecommended secondFragment = null;// 用于显示菜谱界面
    private SelfFragment thirdFragment = null;// 用于显示个人界面

    private View firstLayout = null;// 身体数据显示布局
    private View secondLayout = null;// 菜谱显示布局
    private View ThirdLayout = null;// 个人显示布局

    private ImageView stateImg = null;
    private ImageView recommend = null;
    private ImageView selfImg = null;
    public static SQLiteDatabase db2;

    private TextView stateText = null;
    private TextView  recommendText = null;
    private TextView selfText = null;

    private FragmentManager fragmentManager = null;// 用于对Fragment进行管理

    static String[] permissions={
            "android.permission.SEND_SMS",
            "android.permission.READ_PHONE_STATE",
            "android.permission.ACCESS_NOTIFICATION_POLICY"
    };
    DBclass dbsqLiteOpenHelper ;
    //final SQLiteDatabase db1 = dbsqLiteOpenHelper.getWritableDatabase();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //this.startService(new Intent(this,PushService.class));
        //数据库
        dbsqLiteOpenHelper = new DBclass(this,"Ding.db",null,3);
        final SQLiteDatabase db1 = dbsqLiteOpenHelper.getWritableDatabase();
        db1.delete("BaseDatas", "CreatTime=?", new String[]{"2022-06-24 06:00:00"});
        db2=db1;
        for(int i =0;i<=4;i++){
            ContentValues values = new ContentValues();
            values.put("id",i);
            values.put("BeatRate",62);
            values.put("HighPressure",120);
            values.put("LowPressure",80);
            values.put("BloodGlucose",9);
            values.put("BloodOxygen",100);
            values.put("CreatTime","2022-06-24 06:00:00");
            db1.insert("BaseDatas",null,values);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);//要求窗口没有title
        setContentView(R.layout.activity_main);
        // 初始化布局元素
        initViews();
        fragmentManager = getFragmentManager();//用于对Fragment进行管理
        // 设置默认的显示界面
        setTabSelection(0);


        //获取权限
        /*List<String> list=new ArrayList<>();
        list.clear();
        for(int i=0;i<permissions.length;i++){
            if(ContextCompat.checkSelfPermission(this,permissions[i])!= PackageManager.PERMISSION_GRANTED){
                list.add(permissions[i]);
            }
        }
        if(list.isEmpty()){
            *//*String[] needPP={"android.permission.ACCESS_NOTIFICATION_POLICY"};
            ActivityCompat.requestPermissions(this,needPP,2);*//*
        }else{
            String[] needP=list.toArray(new String[list.size()]);
            ActivityCompat.requestPermissions(this,needP,1);
        }
        getDoNotDisturb();*/
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        fragmentManager = getFragmentManager();
        firstLayout = findViewById(R.id.state_layout);
        secondLayout = findViewById(R.id.recipe_layout);
        ThirdLayout = findViewById(R.id.self_layout);

        stateImg = (ImageView) findViewById(R.id.state_img);
        recommend = (ImageView) findViewById(R.id.recipe_img);
        selfImg = (ImageView) findViewById(R.id.self_img);

        stateText = (TextView) findViewById(R.id.state_text);
        recommendText = (TextView) findViewById(R.id.recipe_text);
        selfText = (TextView) findViewById(R.id.self_text);

        //处理点击事件
        firstLayout.setOnClickListener(this);
        secondLayout.setOnClickListener(this);
        ThirdLayout.setOnClickListener(this);
    }

    /**
     * 点击事件跳转对应fragment
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.state_layout:
                setTabSelection(0);// 当点击了数据时，选中第1个tab
                break;
            case R.id.recipe_layout:
                setTabSelection(1);// 当点击了菜谱时，选中第2个tab
                break;
            case R.id.self_layout:
                setTabSelection(2);// 当点击了个人时，选中第3个tab
                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页 每个tab页对应的下标。0表示身体数据，1表示菜谱，2表示个人
     */
    private void setTabSelection(int index) {
        clearSelection();// 每次选中之前先清除掉上次的选中状态
        FragmentTransaction transaction = fragmentManager.beginTransaction();// 开启一个Fragment事务
        hideFragments(transaction);// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        switch (index) {
            case 0:
                // 当点击了我的微信tab时改变控件的图片和文字颜色
                stateImg.setImageResource(R.drawable.ic_menu_state_tab);//修改布局中的图片
                stateText.setTextColor(Color.parseColor("#0090ff"));//修改字体颜色

                if (firstFragment == null) {
                    /*获取登录activity传过来的微信号*//*
                    Intent intent = getIntent();
                    String number = intent.getStringExtra("weixin_number");*/
                    // 如果FirstFragment为空，则创建一个并添加到界面上
                    firstFragment = new BodyStateFragment();
                    transaction.add(R.id.fragment, firstFragment);

                } else {
                    // 如果FirstFragment不为空，则直接将它显示出来
                    transaction.show(firstFragment);//显示的动作
                }
                break;
            // 以下和firstFragment类同
            case 1:
                recommend.setImageResource(R.drawable.ic_menu_recipe_tab);
                recommendText.setTextColor(Color.parseColor("#0090ff"));
                if (secondFragment == null) {
                    secondFragment = new RecipeRecommended();
                    transaction.add(R.id.fragment, secondFragment);
                } else {
                    transaction.show(secondFragment);
                }
                break;
            case 2:
                selfImg.setImageResource(R.drawable.ic_menu_self_tab);
                selfText.setTextColor(Color.parseColor("#0090ff"));
                if (thirdFragment == null) {
                    thirdFragment = new SelfFragment();
                    transaction.add(R.id.fragment, thirdFragment);
                } else {
                    transaction.show(thirdFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 将所有的Fragment都设置为隐藏状态 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (firstFragment != null) {
            transaction.hide(firstFragment);
        }
        if (secondFragment != null) {
            transaction.hide(secondFragment);
        }
        if (thirdFragment != null) {
            transaction.hide(thirdFragment);
        }

    }

    /**
     * 清除掉所有的选中状态
     */
    private void clearSelection() {
        stateImg.setImageResource(R.drawable.ic_menu_state);
        stateText.setTextColor(Color.parseColor("#82858b"));

        recommend.setImageResource(R.drawable.ic_menu_recipe);
        recommendText.setTextColor(Color.parseColor("#82858b"));

        selfImg.setImageResource(R.drawable.ic_menu_self);
        selfText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 警报功能
     */

    private void alarmAlert(){
//        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
        VibrateUtils.vibrate(MainActivity.this,1000);
        AudioManager localAudioManager = (AudioManager) getSystemService(MainActivity.this.AUDIO_SERVICE);
        int currentMode = localAudioManager.getRingerMode();
        if(currentMode==0|localAudioManager.getRingerMode()==1){
            localAudioManager.adjustSuggestedStreamVolume(AudioManager.ADJUST_UNMUTE,AudioManager.USE_DEFAULT_STREAM_TYPE,0);
        }
        int currentVolume = localAudioManager.getStreamVolume(AudioManager.STREAM_RING);//获取当前音量
        MediaUtils.maxRing(localAudioManager);
        MediaUtils.playRing(MainActivity.this);
        WaringDialog.dialog(MainActivity.this,localAudioManager,currentVolume);
        View view = View.inflate(MainActivity.this,R.layout.self_info,null);
        EditText contact = view.findViewById(R.id.contact);
        String number = contact.getText().toString();
        Alarming.alarm(number);
//            }
//        });


    }

    /**
     * 获取免打扰权限
     */
    private void getDoNotDisturb(){

        NotificationManager notificationManager =
                (NotificationManager) MainActivity.this.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !notificationManager.isNotificationPolicyAccessGranted()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("警报需要获取免打扰权限，点击“手动授权” 中授予！");
            builder.setPositiveButton("手动授权", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent1 = new Intent(
                            android.provider.Settings
                                    .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    finish();
                    MainActivity.this.startActivity(intent1);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Toast.makeText(MainActivity.this, "请先打开权限！", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
                {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH)
                    {
                        return true;
                    }
                    else
                    {
                        return false; //默认返回 false
                    }
                }
            });
            builder.setCancelable(false);
            builder.show();
            builder.show().getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLUE);
        }
    }

    /**
     * 获取短信和获取设备状态权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:
                for(int i=0;i<grantResults.length;i++){
                    if(grantResults[i]!= PackageManager.PERMISSION_GRANTED){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("警报需要获取短信和手机状态权限，点击“手动授权” 中授予！");
                        builder.setPositiveButton("手动授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent2 = new Intent();
                                intent2.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent2.addCategory(Intent.CATEGORY_DEFAULT);
                                intent2.setData(Uri.parse("package:" + getPackageName()));
                                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                finish();
                                startActivity(intent2);
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                Toast.makeText(MainActivity.this, "请先打开权限！", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
                            {
                                if (keyCode == KeyEvent.KEYCODE_SEARCH)
                                {
                                    return true;
                                }
                                else
                                {
                                    return false; //默认返回 false
                                }
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();
                        builder.show().getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                    }
                }
        }
    }
//    static String readMessage;
//
//
//    public static Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    byte[] readBuf = (byte[]) msg.obj;
//                    readMessage= new String(readBuf, 0, readBuf.length);
//                    // ReadCdata(readMessage);
//                    String[] textMany = readMessage.split("-");
//
//
//                    break;
//            }
//
//        }
//    };
    public static   void ReadCdata(String AA){
        String[] g = AA.split("-");
        ContentValues values = new ContentValues();
        if(g[0]!=null)
        {System.out.println(g[0]);
            values.put("BeatRate",Integer.valueOf(g[0]));}
        if(g[1]!=null)
            values.put("HighPressure",Integer.valueOf(g[1]));
        if(g[2]!=null)
            values.put("LowPressure",Integer.valueOf(g[2]));
        if(g[3]!=null)
            values.put("BloodGlucose",Integer.valueOf(g[3]));
        if(g[4]!=null)
            values.put("BloodOxygen",Integer.valueOf(g[4]));

        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String hour;
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH))+1;
        String day = String.valueOf(cal.get(Calendar.DATE));
        if (cal.get(Calendar.AM_PM) == 0)
            hour = String.valueOf(cal.get(Calendar.HOUR));
        else

            hour = String.valueOf(cal.get(Calendar.HOUR)+12);
        String minute = String.valueOf(cal.get(Calendar.MINUTE));
        String second = String.valueOf(cal.get(Calendar.SECOND));
        values.put("CreatTime",year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second);
        db2.insert("BaseDatas",null,values);
    }

    public static Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ReadCdata(msg.obj.toString());
                    break;
            }
        }
    };
}
