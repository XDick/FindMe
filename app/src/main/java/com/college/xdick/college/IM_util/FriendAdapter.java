
package com.college.xdick.college.IM_util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.college.xdick.college.Activity.AskChatActivity;
import com.college.xdick.college.R;
import com.college.xdick.college.util.DynamicsAdapter;

import java.util.List;

import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<Friend> mFriendList;
    private Context mContext;
    private String  friendName;
    private String userId;


    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView friendNameTextView;



        public ViewHolder(View view){
            super(view);
            friendNameTextView = view.findViewById(R.id.friend_name_text);

        }
    }


    public FriendAdapter(List<Friend> friend){
        mFriendList = friend;
    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(mContext == null){
            mContext = parent.getContext();
        }


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend_list,parent,false);
       final ViewHolder holder = new FriendAdapter.ViewHolder(view);
        holder.friendNameTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
               Friend friend = mFriendList.get(position);

                Intent intent = new Intent(mContext , AskChatActivity.class);
                intent.putExtra("ID",userId);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendAdapter.ViewHolder holder, int position) {
        Friend friend = mFriendList.get(position);
        friendName=friend.getFriendUser().getUsername();
        holder.friendNameTextView.setText(friendName);


    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }


    public String getFriendInfo(){
        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("username", friendName);
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> object,BmobException e) {
                if(e==null){
                    for(BmobUser user :object){
                       userId= user.getObjectId();
                       }

                    }
                else{

                }
            }
        });

      return userId;

    }
}

