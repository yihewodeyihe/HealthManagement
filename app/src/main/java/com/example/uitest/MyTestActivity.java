package com.example.uitest;

import android.Manifest;
import android.annotation.SuppressLint;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothSocket;
import android.content.*;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.*;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class MyTestActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private List<String> list1, list2;
    private ArrayAdapter<String> arrayAdapter1, arrayAdapter2;

    private final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");//随便定义⼀个
    private final String NAME = "Bluetooth_Socket";

    private BluetoothSocket socket;
    private BluetoothDevice device;
    private static TextView textView3;
//    public  static DBclass dbsqLiteOpenHelper1 = new DBclass(null,"Ding.db",null,3);
//    final  static  SQLiteDatabase db = dbsqLiteOpenHelper1.getWritableDatabase();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setGravity(Gravity.TOP);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView1 = new TextView(this);
        textView1.setTextSize(10);
        textView1.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        TextView textView2 = new TextView(this);
        textView2.setTextSize(10);
        textView2.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        textView3 = new TextView(this);
        textView3.setTextSize(10);
        textView3.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));


        list1 = new ArrayList<String>();
        ListView listView1 = new ListView(this);
        arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.array_adapter, list1);
        listView1.setAdapter(arrayAdapter1);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ensureDiscoverable();
                String s = arrayAdapter1.getItem(i);
                String address = s.substring(s.indexOf(":") + 1).trim();//把地址解析出来
                //主动连接蓝⽛服务端
                try {

                    //判断当前是否正在搜索
                    if (ActivityCompat.checkSelfPermission(MyTestActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                    try {

                        //获得远程设备
                        device = bluetoothAdapter.getRemoteDevice(address);
                        try {
                            if(SocketThread.device!=null)
                            {
                                if(device.getAddress().equals(SocketThread.device.getAddress()))
                                {
                                    return;
                                }
                                else
                                {
                                    if(SocketThread.socket!=null)
                                    {
                                        if(SocketThread.socket.isConnected())
                                        {
                                            SocketThread.socket.close();
                                        }
                                        SocketThread.socket=null;
                                    }
                                }
                            }
                            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                            //连接蓝牙服务套接字
                            Thread thread = new SocketThread(socket,device);
                            thread.start();

                        } catch (IOException e) {
                            e.printStackTrace();
                            if(socket!=null)
                            {
                                socket.close();
                            }
                            socket=null;
                        }
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                }
            }
        });

        list2=new ArrayList<String>();
        ListView listView2 = new ListView(this);
        arrayAdapter2 =new ArrayAdapter<String>(this,R.layout.array_adapter,list2);
        listView2.setAdapter(arrayAdapter2);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ensureDiscoverable();
                String s = arrayAdapter2.getItem(i);
                String address = s.substring(s.indexOf(":") + 1).trim();//把地址解析出来
                //主动连接蓝⽛服务端
                try {

                    //判断当前是否正在搜索
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                    try {

                        //获得远程设备
                        device = bluetoothAdapter.getRemoteDevice(address);
                        try {
                            if(SocketThread.device!=null)
                            {
                                if(device.getAddress().equals(SocketThread.device.getAddress()))
                                {
                                    return;
                                }
                                else
                                {
                                    if(SocketThread.socket!=null)
                                    {
                                        if(SocketThread.socket.isConnected())
                                        {
                                            SocketThread.socket.close();
                                        }
                                        SocketThread.socket=null;
                                    }
                                }
                            }
                            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                            //连接蓝牙服务套接字
                            Thread thread = new SocketThread(socket,device);
                            thread.start();

                        } catch (IOException e) {
                            e.printStackTrace();
                            if(socket!=null)
                            {
                                socket.close();
                            }
                            socket=null;
                        }
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                }
            }
        });



        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null)
        {
            textView1.setText("该设备不支持蓝牙");
        }
        else {
            Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,1);

            requestBluetoothPermission();
            setDiscoverableTimeout(bluetoothAdapter,1);
            ensureDiscoverable();
            textView1.setText("已绑定设备");
            textView2.setText("附近设备");
            //获得已绑定设备
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    arrayAdapter1.add(device.getName()+":"+device.getAddress());
                }
            }
            bluetoothAdapter.startDiscovery();
        }

        // 设置广播信息过滤
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//每搜索到⼀个设备就会发送⼀个该⼴播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//当全部搜索完后发送该⼴播
        filter.setPriority(Integer.MAX_VALUE);//设置优先级

        // 注册广播接收器，接收并处理搜索结果
        this.registerReceiver(receiver, filter);

        Button buttonSearch=new Button(this);
        buttonSearch.setBackgroundColor(Color.GRAY);
        buttonSearch.setText("搜索蓝牙");
        buttonSearch.setTextSize(10);
        buttonSearch.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                //开启搜索
                list1.removeAll(list1);
                arrayAdapter1.notifyDataSetChanged();
                list2.removeAll(list2);
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        arrayAdapter1.add(device.getName()+":"+device.getAddress());
                    }
                }
                bluetoothAdapter.startDiscovery();
            }
        });

        if(SocketThread.device!=null)
        {
            MainActivity.handler.obtainMessage(1,0,0,"已连上设备："+SocketThread.device.getName()).sendToTarget();
        }
        else
        {
            MainActivity.handler.obtainMessage(2,0,0,"断开连接").sendToTarget();
        }


        Button buttonBack=new Button(this);
        buttonBack.setBackgroundColor(Color.GRAY);
        buttonBack.setText("断开连接");
        buttonBack.setTextSize(10);
        buttonBack.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(SocketThread.socket!=null)
                        SocketThread.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        linearLayout.addView(buttonBack);

        linearLayout.addView(textView3);

        linearLayout.addView(buttonSearch);

        linearLayout.addView(textView1);
        linearLayout.addView(listView1);
        linearLayout.addView(textView2);
        linearLayout.addView(listView2);

        setContentView(linearLayout);

//        new Thread()
//        {
//            public void run()
//            {
//                while (true)
//                {
//                    if(!readMessage.equals(""))
//                    {ReadCdata(readMessage);
//                        readMessage="";
//                    }
//                }
//            }
//        }.start();

    }


    private static final int REQUEST_BLUETOOTH_PERMISSION=10;
    private void requestBluetoothPermission(){
        //判断系统版本
        if (Build.VERSION.SDK_INT >= 23) {
            //检测当前app是否拥有某个权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            //判断这个权限是否已经授权过
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                //判断是否需要向⽤户解释，为什么要申请该权限
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION))
                    Toast.makeText(this,"Need bluetooth permission.",
                            Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this ,new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_BLUETOOTH_PERMISSION);
                return;
            }else{
            }
        } else {
        }
    }

    //广播接收器
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                boolean repeat=false;
                for(int i = 0; i< arrayAdapter2.getCount(); i++)
                {
                    if(list2.get(i).equals(device.getName()+":"+device.getAddress()))
                        repeat=true;
                }
                if(!repeat) {
                    if (arrayAdapter2.getCount()<8) {
                        list2.add(device.getName()+":"+device.getAddress());
                        arrayAdapter2.notifyDataSetChanged();
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(MyTestActivity.this,"搜索完成",Toast.LENGTH_SHORT).show();//已搜素完成
            }
        }
    };

    @SuppressLint("MissingPermission")
    public void ensureDiscoverable(){     //设备可见
        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,0);
            startActivity(discoverableIntent);
        }
    }

    public static void setDiscoverableTimeout(BluetoothAdapter adapter, int timeout) {
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode =BluetoothAdapter.class.getMethod("setScanMode", int.class,int.class);
            setScanMode.setAccessible(true);

            setDiscoverableTimeout.invoke(adapter, timeout);
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE,timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

//    static String readMessage;
//    public static Handler handler = new Handler() {
//
//
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    //byte[] readm =(byte[])msg.obj;
////                    String readMessage = new String(readm,0, msg.arg1);
////                    //readMessage=new String((byte[])msg.obj,0, msg.arg1);
////                    textView3.setText(msg.arg1);
////                    System.out.println(msg.arg1);
//                    break;
//                case 2:
//                    textView3.setText("断开连接");
//                    break;
//
//
//            }
//        }
//    };

    @SuppressLint("MissingPermission")
    protected void onDestroy() {
        super.onDestroy();

        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(receiver);
    }
//    public static void ReadCdata(String AA){
//        String[] g = AA.split("-");
//        ContentValues values = new ContentValues();
//        if(g[0]!=null)
//            values.put("BeatRate",Integer.valueOf(g[0]));
//        if(g[1]!=null)
//            values.put("HighPressure",Integer.valueOf(g[1]));
//        if(g[2]!=null)
//            values.put("LowPressure",Integer.valueOf(g[1]));
//        if(g[3]!=null)
//            values.put("BloodGlucose",Integer.valueOf(g[2]));
//        if(g[4]!=null)
//            values.put("BloodOxygen",Integer.valueOf(g[3]));
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//        String hour;
//        String year = String.valueOf(cal.get(Calendar.YEAR));
//        String month = String.valueOf(cal.get(Calendar.MONTH))+1;
//        String day = String.valueOf(cal.get(Calendar.DATE));
//        if (cal.get(Calendar.AM_PM) == 0)
//            hour = String.valueOf(cal.get(Calendar.HOUR));
//        else
//
//            hour = String.valueOf(cal.get(Calendar.HOUR)+12);
//        String minute = String.valueOf(cal.get(Calendar.MINUTE));
//        String second = String.valueOf(cal.get(Calendar.SECOND));
//        values.put("CreatTime",year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second);
//        db2.insert("BaseDatas",null,values);
//    }

}