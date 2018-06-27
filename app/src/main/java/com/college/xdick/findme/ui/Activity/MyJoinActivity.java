package com.college.xdick.findme.ui.Activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.college.xdick.findme.MyClass.SimpleItemTouchHelperCallback;
import com.college.xdick.findme.R;
import com.college.xdick.findme.adapter.ActivityAdapter2;
import com.college.xdick.findme.bean.MyActivity;
import com.college.xdick.findme.bean.MyUser;
import com.zyyoona7.popup.EasyPopup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2018/5/19.
 */

public class MyJoinActivity extends AppCompatActivity {
    private MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
    private List<MyActivity> activityList= new ArrayList<>();
    private ActivityAdapter2 adapter;
    private List<String> currentList= new ArrayList<>();
    private boolean flag=true;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ac);
        initView();

    }

    private void initView(){
        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setSupportActionBar(toolbar);


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ActivityAdapter2(activityList);
        adapter.setJoin(this);
        View footer = LayoutInflater.from(this).inflate(R.layout.item_footer, recyclerView, false);
        adapter.addFooterView(footer);
        recyclerView.setAdapter(adapter);
/*
        //先实例化Callback
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(recyclerView);*/


    }

    public void initData(){
        activityList.clear();
        BmobQuery<MyActivity> query = new BmobQuery<>();
        query.addWhereContainsAll("joinUser", Arrays.asList(myUser.getObjectId()));
        query.findObjects(new FindListener<MyActivity>() {
            @Override
            public void done(List<MyActivity> list, BmobException e) {
                if (e==null) {

                    for (MyActivity activity : list) {
                        currentList.add(activity.getObjectId());
                        activityList.add(activity);
                    }
                    Collections.sort(activityList);
                    Collections.reverse(activityList);
                    adapter.notifyDataSetChanged();


                    if (flag) {

                        try {
                            if (myUser.getJoin().length > list.size()) {

                                List<String> userList = Arrays.asList(myUser.getJoin());
                                Collection<String> collection = new ArrayList<>(userList);
                                collection.removeAll(currentList);
                                myUser.removeAll("join", collection);
                                myUser.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {

                                    }
                                });
                                Toast.makeText(MyJoinActivity.this, "Oops，其中一些活动已经被发布者删除", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    flag=false;
                }


            }

        });


    }


    @Override                //ToolBar上面的按钮事件
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case  android.R.id.home:
            {
                finish();
                break;
            }


            default:
                break;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}