package com.college.xdick.findme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.college.xdick.findme.R;
import com.college.xdick.findme.bean.MyUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

import static org.greenrobot.eventbus.EventBus.TAG;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{



    private List<BmobIMMessage> mMsgList;
    private Context mContext;
    BmobIMConversation c;





    public int findPosition(BmobIMMessage message) {
        int index = this.getCount();
        int position = -1;
        while(index-- > 0) {
            if(message.equals(this.getItem(index))) {
                position = index;
                break;
            }
        }
        return position;
    }

    public int findPosition(long id) {
        int index = this.getCount();
        int position = -1;
        while(index-- > 0) {
            if(this.getItemId(index) == id) {
                position = index;
                break;
            }
        }
        return position;
    }
    public void addMessages(List<BmobIMMessage> messages) {
        mMsgList.addAll(0, messages);
        notifyDataSetChanged();
    }

    public void addMessage(BmobIMMessage message) {
        mMsgList.addAll(Arrays.asList(message));
        notifyDataSetChanged();
    }
    public void remove(int position){
        mMsgList.remove(position);
        notifyDataSetChanged();
    }

    public BmobIMMessage getFirstMessage() {
        if (null != mMsgList &&mMsgList.size() > 0) {
            return mMsgList.get(0);
        } else {
            return null;
        }
    }
    public int getCount() {
        return this.mMsgList == null?0:this.mMsgList.size();
    }

    public BmobIMMessage getItem(int position){
        return this.mMsgList == null?null:(position >= this.mMsgList.size()?null:this.mMsgList.get(position));
    }




    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout leftLayout,rightLayout;

        TextView leftMsg,rightMsg,time;

        ImageView avatar_me,avatar_you;




        public ViewHolder(View view){
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            leftMsg = view. findViewById(R.id.left_msg);
            rightMsg = view.findViewById(R.id.right_msg);
            time=view.findViewById(R.id.msg_time);
            avatar_me = view.findViewById(R.id.msg_avatar_me);
            avatar_you = view.findViewById(R.id.msg_avatar_you);

        }
    }

    public MsgAdapter(List<BmobIMMessage> msgList){
        mMsgList = msgList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg,
                parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.leftLayout.setVisibility(View.VISIBLE);
        holder.rightLayout.setVisibility(View.VISIBLE);
        String currentUid="";
        currentUid = BmobUser.getCurrentUser().getObjectId();
        BmobIMMessage msg = mMsgList.get(position);
        Log.d(TAG,"位置:"+position+"消息是"+msg.getContent()+"id:"+msg.getFromId()+"当前ID"+currentUid);
        long t=Long.valueOf(msg.getCreateTime());
        SimpleDateFormat sdf1= new SimpleDateFormat("yyyy/MM/dd HH:mm");

        String time1 = sdf1.format(new Date(t));//long转化成Date

        final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String  nowtime = sdf.format(new Date());

        String  rawdate =sdf.format(new Date(t));

        String time = time1.substring(time1.indexOf(" ")+1);

        if (nowtime.equals(rawdate)){
            holder.time.setText(time);
        }
        else{

            holder.time.setText(time1);
        }


        Glide.with(mContext).load(msg.getBmobIMConversation().getConversationIcon()).into(holder.avatar_you);
        Glide.with(mContext).load(BmobUser.getCurrentUser(MyUser.class).getAvatar()).into(holder.avatar_me);

        if(msg.getFromId().equals(currentUid)) {

            holder.rightMsg.setText(msg.getContent());
            holder.leftLayout.setVisibility(View.GONE);



           Log.d(TAG,"左边消失，右边出现消息是"+msg.getContent()+"id:"+msg.getFromId()+"当前ID"+currentUid);
       }
       else {

            holder.leftMsg.setText(msg.getContent());
            holder.rightLayout.setVisibility(View.GONE);

           Log.d(TAG,"右边消失，左边出现,消息是"+msg.getContent()+"id:"+msg.getFromId()+"当前ID"+currentUid);
        }
    }


    @Override
    public int getItemCount() {
        return mMsgList.size();
    }




}