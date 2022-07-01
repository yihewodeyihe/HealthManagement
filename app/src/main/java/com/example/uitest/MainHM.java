package com.example.uitest;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

public class MainHM extends FragmentActivity implements View.OnClickListener {

    private BodyStateFragment firstFragment = null;// 用于显示微信界面
    private RecipeRecommended secondFragment = null;// 用于显示发现界面
    private SelfFragment thirdFragment = null;// 用于显示我界面

    private View firstLayout = null;// 微信显示布局
    private View secondLayout = null;// 发现显示布局
    private View ThirdLayout = null;// 我显示布局

    private ImageView stateImg = null;
    private ImageView recommend = null;
    private ImageView selfImg = null;

    private TextView stateText = null;
    private TextView  recommendText = null;
    private TextView selfText = null;

    private FragmentManager fragmentManager = null;// 用于对Fragment进行管理

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//要求窗口没有title
        setContentView(R.layout.activity_main);
        // 初始化布局元素
        initViews();
        fragmentManager = getFragmentManager();//用于对Fragment进行管理
        // 设置默认的显示界面
        setTabSelection(0);
    }

    private void initViews() {
        fragmentManager = getFragmentManager();
        firstLayout = findViewById(R.id.state_img);
        secondLayout = findViewById(R.id.recipe_img);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.state_layout:
                setTabSelection(0);// 当点击了微信时，选中第1个tab
                break;
            case R.id.recipe_layout:
                setTabSelection(2);// 当点击了发现时，选中第3个tab
                break;
            case R.id.self_layout:
                setTabSelection(3);// 当点击了我时，选中第4个tab
                break;
            default:
                break;
        }
    }

    private void setTabSelection(int index) {
        clearSelection();// 每次选中之前先清除掉上次的选中状态
        FragmentTransaction transaction = fragmentManager.beginTransaction();// 开启一个Fragment事务
        hideFragments(transaction);// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        switch (index) {
            case 0:
                // 当点击了我的微信tab时改变控件的图片和文字颜色
                stateImg.setImageResource(R.drawable.ic_menu_self);//修改布局中的图片
                stateText.setTextColor(Color.parseColor("#0090ff"));//修改字体颜色

                if (firstFragment == null) {
                    /*获取登录activity传过来的微信号*/
                    Intent intent = getIntent();
                    String number = intent.getStringExtra("weixin_number");
                    // 如果FirstFragment为空，则创建一个并添加到界面上
                    firstFragment = new BodyStateFragment();
                    transaction.add(R.id.fragment, firstFragment);

                } else {
                    // 如果FirstFragment不为空，则直接将它显示出来
                    transaction.show(firstFragment);//显示的动作
                }
                break;
            // 以下和firstFragment类同
            case 2:
                recommend.setImageResource(R.drawable.ic_menu_recipe);
                recommendText.setTextColor(Color.parseColor("#0090ff"));
                if (secondFragment == null) {
                    secondFragment = new RecipeRecommended();
                    transaction.add(R.id.fragment, thirdFragment);
                } else {
                    transaction.show(thirdFragment);
                }
                break;
            case 3:
                selfImg.setImageResource(R.drawable.ic_menu_self);
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

    private void clearSelection() {
        stateImg.setImageResource(R.drawable.ic_menu_state);
        stateText.setTextColor(Color.parseColor("#82858b"));

        recommend.setImageResource(R.drawable.ic_menu_recipe);
        recommendText.setTextColor(Color.parseColor("#82858b"));

        selfImg.setImageResource(R.drawable.ic_menu_self);
        selfText.setTextColor(Color.parseColor("#82858b"));
    }
}
