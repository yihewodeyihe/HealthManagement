package com.example.uitest;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BodyStateFragment extends Fragment {

    AnimationDrawable anim ;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.body_state, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.stick_anim);
        imageView.setImageResource(R.drawable.act_matchstickman);
        anim = (AnimationDrawable) imageView.getDrawable();
        imageView.getViewTreeObserver().addOnPreDrawListener(onPreDrawListener);
        Button connect = view.findViewById(R.id.connect);
        EditText heartBeat = view.findViewById(R.id.heart_beat);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MyTestActivity.class);
                startActivityForResult(intent,100);
            }
        });
        /*new Thread(){
            public void run(){
                while(true){
                    heartBeat.setText(readMessage);
                    readMessage = "";
                }
            }
        }.start();*/
        return view;
    }

    ViewTreeObserver.OnPreDrawListener onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            anim.start();
            return true;
        }
    };

    static String readMessage;


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
}
