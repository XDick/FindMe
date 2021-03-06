package com.college.xdick.findme.ui.Activity;


import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.college.xdick.findme.R;


import com.college.xdick.findme.bean.AddTagBean;
import com.college.xdick.findme.bean.MainTagBean;
import com.college.xdick.findme.bean.MyActivity;

import com.college.xdick.findme.bean.MyUser;
import com.college.xdick.findme.ui.Base.BaseActivity;
import com.college.xdick.findme.util.ClassFileHelper;
import com.donkingliang.labels.LabelsView;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static cn.bmob.newim.core.BmobIMClient.getContext;

/**
 * Created by Administrator on 2018/4/11.
 */

public class SetActivitiyActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener {
    private EditText titleE,  placeE, contentE,createE;
    private ImageView cover;
    private LinearLayout layout,chooselayout,labellayout;
    private  TextView picTitle,chooseText,createText,timeE;
    private Button createButton;
    private List<MainTagBean> mainTagBeanList = new ArrayList<>();
    private Calendar now,end;
    private  List<String> selectTagList = new ArrayList<>();
    private  boolean ifcreateAdd =false;
    private  String usedAddTag="";
    private  int day;
    private long time,endTime;
    private MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
    private ImagePicker imagePicker ;
    private static String picturePath;
    private int chooseCalendarCount=0;
    private  long  nowtime =0,choosetime=0 ;

    private   String startDate ,endDate;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setactivity);
        initView();
        startImagePicker();
    }


    private void initView() {
        final LabelsView labelsView =  findViewById(R.id.setac_main_labels);
        final LabelsView labelsView2 = findViewById(R.id.setac_add_labels);
        final LabelsView labelsView_sub = findViewById(R.id.setac_sub_labels);
        final LabelsView labelsView_sort = findViewById(R.id.setac_sort_labels);

        createText = findViewById(R.id.setac_createtag_text);
        titleE = findViewById(R.id.setac_title_edittext);
        createE = findViewById(R.id.setac_createtag_edit);
        placeE = findViewById(R.id.setac_place_edittext);
        contentE = findViewById(R.id.setac_content_edittext);
        layout = findViewById(R.id.setac_content_linearlayout);
        cover = findViewById(R.id.ac_upload_pic_imageview);
        picTitle = findViewById(R.id.ac_upload_pic_title);
        chooseText =findViewById(R.id.setac_choosetag_text);
        chooselayout = findViewById(R.id.setac_choosetag);
        labellayout = findViewById(R.id.setac_lable);
        createButton = findViewById(R.id.setac_createtag_button);
        timeE = findViewById(R.id.setac_time_text);

        cover.setDrawingCacheEnabled(true);
        createText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!createText.getText().equals("没有合适的？创建标签")) {
                    selectTagList.set(3,"");
                    ifcreateAdd =false;


                }
                    createText.setVisibility(View.GONE);
                    createE.setVisibility(View.VISIBLE);
                    createButton.setVisibility(View.VISIBLE);

                    createE.setFocusable(true);
                    createE.setFocusableInTouchMode(true);
                    createE.requestFocus();
                    createE.findFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(createE, 0);
                }


        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = createE.getText().toString();
                if (tag.equals("")) {
                    createText.setText("没有合适的？创建标签");
                    ifcreateAdd =false;
                    selectTagList.set(3,"");
                    labelsView2.setMaxSelect(1);
                    labelsView2.setSelectType(LabelsView.SelectType.SINGLE);


                }
                else {
                    labelsView2.clearAllSelect();
                    labelsView2.setSelectType(LabelsView.SelectType.NONE);

                    selectTagList.set(3,tag);
                    createText.setText(tag);
                    ifcreateAdd =true;

                }

                createText.setVisibility(View.VISIBLE);
                createE.setVisibility(View.GONE);
                createButton.setVisibility(View.GONE);

            }
        });

     chooselayout.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(chooseText.getText().equals("选择标签")){
               labellayout.setVisibility(View.VISIBLE);
                 chooseText.setText("收 起");
             }

             else {
                 labellayout.setVisibility(View.GONE);
                 chooseText.setText("选择标签");

             }

         }

     });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentE.setFocusable(true);
                contentE.setFocusableInTouchMode(true);
                contentE.requestFocus();
                contentE.findFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(contentE, 0);
            }
        });

        timeE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCalendarCount=1;
                now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        SetActivitiyActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.show(getFragmentManager(), "Datepickerdialog");



            }

        });


        Toolbar toolbar = findViewById(R.id.toolbar_setac);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.startGallery(SetActivitiyActivity.this, new ImagePicker.Callback() {
                            // 选择图片回调
                            @Override
                            public void onPickImage(Uri imageUri) {



                            }

                            // 裁剪图片回调
                            @Override
                            public void onCropImage(Uri imageUri) {
                                if(imageUri!=null){
                                    Glide.with(SetActivitiyActivity.this).load(imageUri).into(cover);
                                    picTitle.setVisibility(View.GONE);
                                    picturePath=imageUri.toString().replace("file:///","");
                                }

                            }

                            // 自定义裁剪配置
                            @Override
                            public void cropConfig(CropImage.ActivityBuilder builder) {
                                builder
                                        // 是否启动多点触摸
                                        .setMultiTouchEnabled(false)
                                        // 设置网格显示模式
                                        .setGuidelines(CropImageView.Guidelines.ON)
                                        // 圆形/矩形
                                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                                        // 调整裁剪后的图片最终大小
                                        .setRequestedSize(960, 660)
                                        // 宽高比
                                        .setAspectRatio(16, 11);
                            }

                            // 用户拒绝授权回调
                            @Override
                            public void onPermissionDenied(int requestCode, String[] permissions,
                                                           int[] grantResults) {
                                Toast.makeText(SetActivitiyActivity.this,"抱歉(＞人＜；)，功能将无法实现",Toast.LENGTH_SHORT).show();
                            }

                        });







            }
        });

        String[] sort={"个人活动","团体组织"};
        labelsView_sort.setLabels(Arrays.asList(sort));
        if(myUser.isGod()){
            String[] sort2={"泛觅活动","个人活动","团体组织"};
            labelsView_sort.setLabels(Arrays.asList(sort2));
        }


        BmobQuery<MainTagBean> query = new BmobQuery<MainTagBean>();
        query.order("order");
        query.findObjects(new FindListener<MainTagBean>() {


            @Override
            public void done(List<MainTagBean> object, BmobException e) {
                if (e == null) {

                    mainTagBeanList.addAll(object);
                    labelsView.setLabels(object, new LabelsView.LabelTextProvider<MainTagBean>() {
                        @Override
                        public CharSequence getLabelText(TextView label, int position, MainTagBean data) {
                            //根据data和position返回label需要显示的数据。
                            return data.getMainTag();
                        }
                    });

                } else {

                }


            }
        });


        BmobQuery<AddTagBean> query2 = new BmobQuery<AddTagBean>();


        query2.setLimit(50);
        query2.order("-updatedAt");
        query2.findObjects(new FindListener<AddTagBean>() {
            @Override
            public void done(List<AddTagBean> object, BmobException e) {
                if (e == null) {

                    labelsView2.setLabels(object, new LabelsView.LabelTextProvider<AddTagBean>() {
                        @Override
                        public CharSequence getLabelText(TextView label, int position, AddTagBean data) {
                            //根据data和position返回label需要显示的数据。
                            return data.getAddTag();
                        }
                    });
                } else {
                }
            }
        });

        for (int i=0;i<4;i++){
            selectTagList.add("");
        }


        labelsView_sort.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                if (isSelect) {
                    selectTagList.set(0,label.getText().toString());
                    Log.d("TAG", "数据" + label.getText().toString());
                } else {

                    selectTagList.set(0,"");

                }

            }
        });



//标签的选中监听
        labelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                if (isSelect) {
                    selectTagList.set(1,label.getText().toString());
                    labelsView_sub.setLabels(Arrays.asList(mainTagBeanList.get(position).getSubTag()));
                    Log.d("TAG", "数据" + label.getText().toString());
                } else {
                    labelsView_sub.setLabels(null);
                    selectTagList.set(1,"");

                }

            }
        });

        labelsView_sub.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                if (isSelect) {
                    selectTagList.set(2,label.getText().toString());
                    Log.d("TAG", "数据" + label.getText().toString());
                } else {
                    selectTagList.set(2,"");
                }

            }
        });


//标签的选中监听
        labelsView2.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                if (isSelect) {
                    selectTagList.set(3,label.getText().toString());
                    usedAddTag=label.getText().toString();

                    Log.d("TAG", "数据" + label.getText().toString());
                } else {
                    selectTagList.set(3,"");
                    usedAddTag="";
                }

            }
        });

    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.toolbar_activity_menu, menu);
        return true;
    }

    @Override                //ToolBar上面的按钮事件
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.start_activity:

                final MyActivity activity = new MyActivity();
                final String content = contentE.getText().toString();
                final String title = titleE.getText().toString();
                final String date = timeE.getText().toString();
                final String place = placeE.getText().toString();
                final String[] gps = myUser.getGps();
                final String[] tag = selectTagList.toArray(new String[selectTagList.size()]);




                if(content.length()!=0&&title.length()!=0
                        &&place.length()!=0&&date.length()!=0&&date.contains(" - ")
                        &&!selectTagList.contains("")){

                 if (contentE.getText().toString().length()<=50){
                     Toast.makeText(SetActivitiyActivity.this,"活动描述不能少于50字",Toast.LENGTH_SHORT).show();

                 }
                 else {
                     Toast.makeText(SetActivitiyActivity.this, "发起中...", Toast.LENGTH_SHORT).show();
                    if(picturePath!=null) {

                        File file=new File(picturePath);
                        final String fileType = picturePath
                                .substring(picturePath.lastIndexOf("."));

                            final String renamePath=picturePath.
                                    substring(0,picturePath.lastIndexOf("/")+1)
                                    +"activity_image_"+myUser.getObjectId()
                                    +fileType;
                            //Toast.makeText(getContext(),renamePath,Toast.LENGTH_LONG).show();


                            file.renameTo(new File(renamePath));




                    final BmobFile bmobFile = new BmobFile(new File(renamePath));
                        finish();
                    bmobFile.uploadblock(new UploadFileListener() {

                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ClassFileHelper.deleteFile(new File(renamePath));
                                //Toast.makeText(getContext(), "上传文件成功:" + bmobFile.getFileUrl(), Toast.LENGTH_LONG).show();
                                final MyUser user = BmobUser.getCurrentUser(MyUser.class);
                                final String coverURL = bmobFile.getFileUrl();

                                activity.setCover(coverURL);
                                activity.setHost(myUser);
                                activity.setTitle(title);
                                activity.setTime(startDate);
                                activity.setEndTime(endDate);
                                activity.setContent(content);
                                activity.setPlace(place);
                                activity.setGps(gps);
                                activity.setTag(tag);
                                activity.setHostSchool(user.getSchool());
                                activity.setDate(time);
                                activity.setEndDate(endTime);
                                activity.setCommentCount(0);
                                activity.save(new SaveListener<String>() {

                                    @Override
                                    public void done(final String objectId, BmobException e) {
                                        if (e == null) {


                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                    String date = sdf.format(new Date(bmobTime));
                                                    user.setSetAcTime(date);
                                                    user.increment("setAcCount",1);
                                                    user.increment("Exp",10);
                                                    user.update(new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {

                                                        }
                                                    });




                                            if (ifcreateAdd) {
                                                AddTagBean tag = new AddTagBean(createText.getText().toString());
                                                tag.setProviderName(user.getUsername());
                                                tag.save(new SaveListener<String>() {
                                                    @Override
                                                    public void done(String s, BmobException e) {


                                                        BmobQuery<AddTagBean> query = new BmobQuery<AddTagBean>();
                                                        query.addWhereEqualTo("addTag", createText.getText().toString());
                                                        query.findObjects(new FindListener<AddTagBean>() {
                                                            @Override
                                                            public void done(List<AddTagBean> list, BmobException e) {
                                                                for (AddTagBean tag : list) {
                                                                    tag.UserCountAdd1();
                                                                    tag.update(tag.getObjectId(), new UpdateListener() {
                                                                        @Override
                                                                        public void done(BmobException e) {

                                                                        }
                                                                    });
                                                                    break;
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }

                                            if (!usedAddTag.equals("")) {
                                                BmobQuery<AddTagBean> query = new BmobQuery<AddTagBean>();
                                                query.addWhereEqualTo("addTag", usedAddTag);
                                                query.findObjects(new FindListener<AddTagBean>() {
                                                    @Override
                                                    public void done(List<AddTagBean> list, BmobException e) {
                                                        for (AddTagBean tag : list) {
                                                            tag.UserCountAdd1();
                                                            tag.update(tag.getObjectId(), new UpdateListener() {
                                                                @Override
                                                                public void done(BmobException e) {

                                                                }
                                                            });
                                                            break;

                                                        }
                                                    }
                                                });
                                            }
                                            Toast.makeText(SetActivitiyActivity.this, "发起成功", Toast.LENGTH_SHORT).show();


                                        } else {
                                            Toast.makeText(SetActivitiyActivity.this, "发起失败" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });


                            }



                            else

                            {
                                Toast.makeText(getContext(), picturePath + e.getMessage(), Toast.LENGTH_LONG).show();
                            }


                        }
                    });
                }

                 }


                }
                else {
                    Toast.makeText(getContext(), "请完善信息", Toast.LENGTH_LONG).show();
                }


                break;

            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imagePicker.onActivityResult(this, requestCode, resultCode, data);
    }





    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
       if (chooseCalendarCount==1){
           // DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
           time=time+hourOfDay*60*60*1000+minute*60*1000+second*1000;
           String min=""+minute;
           if (minute<10){min="0"+minute;}

           String time = hourOfDay+":"+min;
           startDate=startDate+" "+time;

           timeE.setText(startDate);

           end = Calendar.getInstance();
           DatePickerDialog dpd = DatePickerDialog.newInstance(
                   SetActivitiyActivity.this,
                   end.get(Calendar.YEAR),
                   end.get(Calendar.MONTH),
                   end.get(Calendar.DAY_OF_MONTH)
           );
           dpd.setVersion(DatePickerDialog.Version.VERSION_2);
           dpd.show(getFragmentManager(), "Datepickerdialog");
           Toast.makeText(SetActivitiyActivity.this,"选择截止时间(最多1天)",Toast.LENGTH_SHORT).show();
           chooseCalendarCount=2;
       }
       else if (chooseCalendarCount==2){



           // DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
           endTime=endTime+hourOfDay*60*60*1000+minute*60*1000+second*1000;

               if (endTime<time){
                   timeE.setText("选择时间(15天内)");
                   Toast.makeText(SetActivitiyActivity.this,"请选择正确的时间",Toast.LENGTH_SHORT).show();
                   return;
               }


           String min=""+minute;
           if (minute<10){min="0"+minute;}

           String time = hourOfDay+":"+min;
           endDate=endDate+" "+time;


           timeE.setText(startDate+" - "+endDate);


           chooseCalendarCount=0;

       }




    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {


        if (chooseCalendarCount==1){

            final String date = year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日";
            startDate=date;
            final  DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                    String  datestringnow = sdf.format(new Date(bmobTime));
                    String datestring=date;
                    Date date1=null;
                    Date date3 =null;


                    try {
                        date1 =sdf.parse(datestring);
                        date3 = sdf.parse(datestringnow);
                    }
                    catch (ParseException e2){
                        e2.getMessage();
                    }
                     nowtime = date3.getTime();
                     choosetime = date1.getTime();

                    time= choosetime;

                    day=(int) (choosetime-nowtime)/(1000*3600*24);

                    if (day>=0&&day<=15) {
                        TimePickerDialog tpd = TimePickerDialog.newInstance(
                                SetActivitiyActivity.this,
                                now.get(Calendar.HOUR),
                                now.get(Calendar.MINUTE),
                                false
                        );
                        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
                        tpd.show(getFragmentManager(), "Timepickerdialog");
                    }
                    else {

                        Toast.makeText(SetActivitiyActivity.this,"请选择正确的时间",Toast.LENGTH_SHORT).show();
                    }



        }
        else if (chooseCalendarCount==2){
            final String date = year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日";
            endDate=date;
            final  DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

            String datestring=date;
            Date date1=null;


            try {
                date1 =sdf.parse(datestring);
            }
            catch (ParseException e2){
                e2.getMessage();
            }
            long  choosetime2 = date1.getTime();

            endTime= choosetime2;

            day=(int) (choosetime2-choosetime)/(1000*3600*24);

            if (day>=0&&day<=1) {

                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        SetActivitiyActivity.this,
                        end.get(Calendar.HOUR),
                        end.get(Calendar.MINUTE),
                        false
                );
                tpd.setVersion(TimePickerDialog.Version.VERSION_2);
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
            else {
                timeE.setText("选择时间(15天内)");
                Toast.makeText(SetActivitiyActivity.this,"请选择正确的时间",Toast.LENGTH_SHORT).show();
            }


        }









    }


    private  void startImagePicker() {
        imagePicker = new ImagePicker();
// 设置标题
        imagePicker.setTitle("选择封面");
// 设置是否裁剪图片
        imagePicker.setCropImage(true);

    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
