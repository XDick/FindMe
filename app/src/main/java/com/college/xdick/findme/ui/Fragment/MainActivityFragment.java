package com.college.xdick.findme.ui.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.college.xdick.findme.R;
import com.college.xdick.findme.adapter.MyFragmentStatePagerAdapter;
import com.college.xdick.findme.bean.MyUser;

import com.college.xdick.findme.ui.Activity.LoginActivity;
import com.college.xdick.findme.ui.Activity.SetActivitiyActivity;

import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

import cn.bmob.v3.listener.QueryListener;


/**
 * Created by Administrator on 2018/5/6.
 */

public class MainActivityFragment extends Fragment {
    private View rootView;
    private ViewPager mViewPager1;
    private ImageView logo;
    private TabLayout mTabLayout;
    private LinearLayout newsort, datesort;
    private TextView gpsTextView;
    private String[] tabTitle = {"推荐", "同城", "同校"};//每个页面顶部标签的名字
    private MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
    private  MyFragmentStatePagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);
        initViews();
        setHasOptionsMenu(true);
        Log.d("", mViewPager1.getCurrentItem() + "当前");
        return rootView;

    }

    private void initViews() {
        mViewPager1 = (ViewPager) rootView.findViewById(R.id.mViewPager1);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.mTabLayout);
        adapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(), tabTitle);
        logo = rootView.findViewById(R.id.logo);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar_ac);
        toolbar.setTitle("");
        gpsTextView = rootView.findViewById(R.id.gps_main_ac_toolbar);
        if (bmobUser != null) {
            String[] gps = bmobUser.getGps();
            if (gps != null) {
                gpsTextView.setText(gps[2]);
            }
        } else {
            gpsTextView.setText("未登录");
            gpsTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                }
            });
        }
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();







        for (int i = 0; i < tabTitle.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tabTitle[i]));
        }
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        //这里注意的是，因为我是在fragment中创建MyFragmentStatePagerAdapter，所以要传getChildFragmentManager()
        mViewPager1.setAdapter(adapter);


        //在设置viewpager页面滑动监听时，创建TabLayout的滑动监听
        mViewPager1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //在选中的顶部标签时，为viewpager设置currentitem
                mViewPager1.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });










        final EasyPopup easyPopup = EasyPopup.create()
                .setContentView(getContext(), R.layout.popup_sort)
                .setWidth(400)
                .apply();

        datesort = easyPopup.findViewById(R.id.datesort);




        newsort = easyPopup.findViewById(R.id.newsort);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyPopup.showAtAnchorView(logo, YGravity.BELOW, XGravity.CENTER, 0, 0);
            }
        });



        datesort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (mViewPager1.getCurrentItem()){
                    case 0:
                    {
                        ActivityFragment fragment=((ActivityFragment)adapter.instantiateItem(mViewPager1,0));
                        fragment.setSize(0);
                        fragment.sortData(fragment.REFRESH);
                        easyPopup.dismiss();
                        break;

                    }
                    case 1:
                    {
                        ActivitygpsFragment fragment=((ActivitygpsFragment)adapter.instantiateItem(mViewPager1,1));
                        fragment.setSize(0);
                        fragment.sortData(fragment.REFRESH);
                        easyPopup.dismiss();
                        break;
                    }
                    case 2:
                    {
                        ActivityschoolFragment fragment=((ActivityschoolFragment)adapter.instantiateItem(mViewPager1,2));
                        fragment.setSize(0);
                        fragment.sortData(fragment.REFRESH);
                        easyPopup.dismiss();
                        break;
                    }
                }
            }
        });



        newsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (mViewPager1.getCurrentItem()){
                    case 0:
                    {
                        ActivityFragment fragment=((ActivityFragment)adapter.instantiateItem(mViewPager1,0));
                        fragment.setSize(0);
                        fragment.initData(fragment.REFRESH);
                        easyPopup.dismiss();
                        break;

                    }
                    case 1:
                    {
                        ActivitygpsFragment fragment=((ActivitygpsFragment)adapter.instantiateItem(mViewPager1,1));
                        fragment.setSize(0);
                        fragment.initData(fragment.REFRESH);
                        easyPopup.dismiss();
                        break;
                    }
                    case 2:
                    {
                        ActivityschoolFragment fragment=((ActivityschoolFragment)adapter.instantiateItem(mViewPager1,2));
                        fragment.setSize(0);
                        fragment.initData(fragment.REFRESH);
                        easyPopup.dismiss();
                        break;
                    }
                }
            }
        });



    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.toolbar_set_activity_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override                //ToolBar上面的按钮事件
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.set_activity:
                bmobUser = BmobUser.getCurrentUser(MyUser.class);
                Bmob.getServerTime(new QueryListener<Long>() {
                    @Override
                    public void done(Long aLong, BmobException e) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String date = sdf.format(new Date(aLong * 1000L));
                        if (bmobUser != null) {
                            if (!date.equals(bmobUser.getSetAcTime()) || bmobUser.getUsername().equals("叉地克")) {
                                Intent intent = new Intent(getContext(), SetActivitiyActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getContext(), "今天已经发过活动了o(╥﹏╥)o", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                break;


            default:
                break;
        }

        return true;
    }





}