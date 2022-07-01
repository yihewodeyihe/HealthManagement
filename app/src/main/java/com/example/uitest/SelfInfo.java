package com.example.uitest;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class SelfInfo extends Activity {
    private static final int resultCode = 2;
    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.self_info);
        EditText editText =SelfInfo.this.findViewById(R.id.contact);
        flag = true;
        //返回上一个Activity
        ImageButton button = this.findViewById(R.id.back_key);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SelfInfo.this,"祝您生活愉快",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        Button buttonCheck = this.findViewById(R.id.check);
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = SelfInfo.this.findViewById(R.id.contact);
                editText.setFocusable(flag);
                editText.setFocusableInTouchMode(flag);

                flag = !flag;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ImageView imageView = this.findViewById(R.id.stick_anima);
        imageView.setImageResource(R.drawable.act_matchstickman);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }
}
