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
import com.college.xdick.findme.adapter.ActivityAdapter;
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

    private Toolbar toolbar;
    private boolean ifEmpty=false;
    private  int size =0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ac);
        initView();
        initData();

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
       final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ActivityAdapter2(activityList);
        adapter.setJoin(this);
        View footer = LayoutInflater.from(this).inflate(R.layout.item_footer, recyclerView, false);
        adapter.addFooterView(footer);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(ifEmpty){
                    adapter.changeMoreStatus(ActivityAdapter.NO_MORE);
                }else
                {
                    adapter.changeMoreStatus(ActivityAdapter.LOADING_MORE);}
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&  layoutManager.findLastVisibleItemPosition()+1  == adapter.getItemCount()) {
                    if (ifEmpty){
                        //null
                    }
                    else {initData();}
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });




    }

    public void initData(){

        BmobQuery<MyActivity> query = new BmobQuery<>();

        query.addWhereContainsAll("joinUser",Arrays.asList(myUser.getObjectId()));
        query.order("-date");
        query.setLimit(10);
        query.setSkip(size);
        final int listsize = activityList.size();
        query.findObjects(new FindListener<MyActivity>() {
            @Override
            public void done(List<MyActivity> list, BmobException e) {
                if (e == null) {
                    activityList.addAll(list);

                    if (listsize==activityList.size()){
                        ifEmpty=true;
                        adapter.changeMoreStatus(ActivityAdapter.NO_MORE);
                        adapter.notifyDataSetChanged();
                    }else if (listsize+10>activityList.size()){
                        ifEmpty=true;
                        adapter.changeMoreStatus(ActivityAdapter.NO_MORE);
                        adapter.notifyItemInserted(adapter.getItemCount()-1);

                    }


                    else {
                        adapter.notifyItemInserted(adapter.getItemCount()-1);
                        size = size + 10;
                    }


                }else{
                    Toast.makeText(MyJoinActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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


}
