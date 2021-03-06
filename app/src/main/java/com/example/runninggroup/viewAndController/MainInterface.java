package com.example.runninggroup.viewAndController;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.runninggroup.R;
import com.example.runninggroup.viewAndController.adapter.MyPagerAdapter;
import com.example.runninggroup.viewAndController.fragment.FragmentCard;
import com.example.runninggroup.viewAndController.fragment.FragmentData;
import com.example.runninggroup.viewAndController.fragment.FragmentFriends;
import com.example.runninggroup.viewAndController.fragment.FragmentGroup;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainInterface extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Button mBtn_sideSetting;
    private Button mBtn_exit;
    private ConstraintLayout mPesonalSetting;
    private LinearLayout mLineraLayout;
    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maininterface);
        initView();
        initEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void initView() {
        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tabLayout);
        mBtn_sideSetting = findViewById(R.id.sideSetting);
        mBtn_exit = findViewById(R.id.button_quit);
        mPesonalSetting = findViewById(R.id.personalSetting);
        mLineraLayout = findViewById(R.id.ll_container);
        mListView = findViewById(R.id.personalListView);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        ArrayList<String> list_Title = new ArrayList<>();
        fragmentList.add(new FragmentData());
        fragmentList.add(new FragmentCard());
        fragmentList.add(new FragmentFriends());
        fragmentList.add(new FragmentGroup());
        list_Title.add("数据");
        list_Title.add("打卡");
        list_Title.add("好友");
        list_Title.add("跑团");
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList,list_Title));
        mTabLayout.setupWithViewPager(mViewPager);//此方法就是让tablayout和ViewPager联动
        Intent intent = getIntent();
        int jump = intent.getIntExtra("jump",0);
        mViewPager.setCurrentItem(jump);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        mBtn_sideSetting.setOnClickListener(this);
        mBtn_exit.setOnClickListener(this);
        mLineraLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPesonalSetting.setVisibility(View.INVISIBLE);
                mLineraLayout.setVisibility(View.GONE);
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0){
                    Intent intent = new Intent(MainInterface.this, ResetPersonalData.class);
                    startActivity(intent);
                }else if (id == 1){
                    Toast.makeText(MainInterface.this, "该功能暂未上线", Toast.LENGTH_SHORT).show();
                }else if (id == 2){
                    Toast.makeText(MainInterface.this, "该功能暂未上线", Toast.LENGTH_SHORT).show();
                }else if (id == 3){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainInterface.this);
                    builder.setTitle("客服联系方式：");
                    builder.setMessage("客服联系邮箱：3448562305@qq.com");
                    builder.setCancelable(false);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainInterface.this, "期待您的联系与建议", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sideSetting:
                mPesonalSetting.setVisibility(View.VISIBLE);
                mLineraLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.button_quit:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainInterface.this);
                builder.setTitle("退出登录提示：");
                builder.setMessage("您确定要退出登录状态，并返回到登录界面吗？");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainInterface.this, Login.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainInterface.this, "运动就是坚持！！", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                break;
        }
    }

}
