package com.example.runninggroup.viewAndController;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.example.runninggroup.R;
import com.example.runninggroup.model.DaoGroup;
import com.example.runninggroup.model.DaoUser;
import com.example.runninggroup.viewAndController.adapter.MyPagerAdapter;
import com.example.runninggroup.viewAndController.fragment.FragmentCard;
import com.example.runninggroup.viewAndController.fragment.FragmentData;
import com.example.runninggroup.viewAndController.fragment.FragmentFriends;
import com.example.runninggroup.viewAndController.fragment.FragmentGroup;
import com.example.runninggroup.viewAndController.fragment.FragmentGroupMember;
import com.example.runninggroup.viewAndController.fragment.FragmentGroupTask;
import com.example.runninggroup.viewAndController.helper.GroupHelper;
import com.google.android.material.tabs.TabLayout;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;

public class GroupMessage extends AppCompatActivity implements View.OnClickListener {
    TextView nameText,groupText,numText,manageText;
    Button mButton;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    Intent mIntent;
    String username;
    String group;
    String num;
    String leader;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmessage);
        initView();
        initEvent();

    }


    private void initView() {
        nameText = findViewById(R.id.leaderName);
        groupText = findViewById(R.id.group);
        numText = findViewById(R.id.num);
        manageText = findViewById(R.id.manage);
        mButton = findViewById(R.id.join);

        mViewPager = findViewById(R.id.groupmessage_viewPager);
        mTabLayout = findViewById(R.id.groupmessage_tabLayout);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        ArrayList<String> list_Title = new ArrayList<>();

        //访问服务器
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mIntent = getIntent();
                username = mIntent.getStringExtra("username");
                List<GroupHelper> list = DaoUser.getMyGroupAll(username);
                if(list.size() != 0){
                    groupText.setText(list.get(0).getGroupName());
                    numText.setText(list.get(0).getNumbers()+"");
                    nameText.setText(list.get(0).getLeaderName());
                }

                group = groupText.getText().toString();
                num = numText.getText().toString();
                leader = nameText.getText().toString();
                if(! leader.equals(username)){
                    manageText.setVisibility(View.GONE);
                }else {
                    mButton.setVisibility(View.GONE);
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Activity向 FragmentGroupTask传递group信息
        FragmentGroupTask fragmentGroupTask = new FragmentGroupTask();
        FragmentGroupMember fragmentGroupMember = new FragmentGroupMember();
        Bundle budle=new Bundle();
        budle.putString("group",group);
        fragmentGroupTask.setArguments(budle);
        fragmentGroupMember.setArguments(budle);



        fragmentList.add(fragmentGroupMember);
        fragmentList.add(fragmentGroupTask);
        list_Title.add("Members");
        list_Title.add("Tasks");
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList,list_Title));
        mTabLayout.setupWithViewPager(mViewPager);

    }
    private void initEvent() {
        //加入、退出按钮点击事件
        mButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.join:
                if(mButton.getText().toString().equals("加入")){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String result = DaoUser.getMyGroup(username);

                            if("".equals(result)){
                                if(DaoUser.setMyGroup(username,group,1,"JOIN").equals("SUCCESS")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mButton.setText("退出");
                                            int number = Integer.parseInt(numText.getText().toString())+1;
                                            numText.setText(number+"");
                                        }
                                    });
                                    Looper.prepare();
                                    Toast.makeText(GroupMessage.this,"成功！",Toast.LENGTH_SHORT).show();
                                    Looper.loop();

                                }else {
                                    Looper.prepare();
                                    Toast.makeText(GroupMessage.this,"失败！",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }else {
                                Looper.prepare();
                                Toast.makeText(GroupMessage.this,"失败！\n您已经加入过跑团",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    }).start();

                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(DaoUser.setMyGroup(username,group,-1,"OUT").equals("SUCCESS")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mButton.setText("加入");
                                        int number = Integer.parseInt(numText.getText().toString())-1;
                                        numText.setText(number+"");
                                    }
                                });
                                Looper.prepare();
                                Toast.makeText(GroupMessage.this,"成功",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }else {
                                Looper.prepare();
                                Toast.makeText(GroupMessage.this,"失败",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    }).start();
                }
                break;

            case R.id.manage:
                Intent intent = new Intent(GroupMessage.this,Manage.class);
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                bundle.putString("group",group);
                bundle.putString("num",num);
                bundle.putString("leader",leader);
                intent.putExtras(bundle);
                startActivity(intent);

                break;
        }


    }
}
