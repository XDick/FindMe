package com.college.xdick.college.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.college.xdick.college.IM_util.MsgAdapter;
import com.college.xdick.college.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

public class ChatActivity extends AppCompatActivity implements MessageListHandler {

    private RecyclerView msgRecyclerView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapter;
    private Toolbar toolbar;
    BmobIMConversation conversation;

    private List<BmobIMMessage> msgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        BaseIMoperation();
        initView();



    }


    private void initView(){


        toolbar = findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendIMMsg();
                }
            }
        );
    }

    private void BaseIMoperation(){


//在聊天页面的onCreate方法中，通过如下方法创建新的会话实例,这个obtain方法才是真正创建一个管理消息发送的会话
        conversation=BmobIMConversation.obtain(BmobIMClient.getInstance(),
                (BmobIMConversation) getIntent().getSerializableExtra("c"));
    }


    private void sendIMMsg(){
        String content = inputText.getText().toString();
        BmobIMTextMessage msg =new BmobIMTextMessage();
        msg.setContent(content);
     //可随意设置额外信息
        Map<String,Object> map =new HashMap<>();
        map.put("level", "1");
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void onStart(BmobIMMessage msg) {
                super.onStart(msg);

               /* if(!"".equals(content)) {
                    msg.setContent(content);*/

                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);
                    //当有新消息 刷新ListVIEW中的显示
                }

            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                     adapter.notifyDataSetChanged();
                    //讲ListView定位到最后一行
                    inputText.setText("");
                if (e != null) {
                   Toast.makeText(ChatActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i = 0; i < list.size(); i++) {
            addMessage2Chat(list.get(i));
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    private void addMessage2Chat(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        if (conversation != null && event != null && conversation.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()) {//并且不为暂态消息
            if (adapter.findPosition(msg) < 0) {//如果未添加到界面中
                msgList.add(msg);
                adapter.notifyItemInserted(msgList.size()-1);
                //更新该会话下面的已读状态
                conversation.updateReceiveStatus(msg);
            }
            msgRecyclerView.scrollToPosition(msgList.size()-1);
        } else {
            //Logger.i("不是与当前聊天对象的消息");
        }
    }


    private void addUnReadMessage() {
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if (cache.size() > 0) {
            int size = cache.size();
            for (int i = 0; i < size; i++) {
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
            msgRecyclerView.scrollToPosition(msgList.size()-1);}
        }

    @Override
    protected void onPause() {
        //移除页面消息监听器
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }


}

