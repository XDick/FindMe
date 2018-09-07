package com.college.xdick.findme.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.college.xdick.findme.R;
import com.college.xdick.findme.bean.Comment;
import com.college.xdick.findme.bean.MyActivity;
import com.college.xdick.findme.bean.MyUser;
import com.college.xdick.findme.ui.Activity.ActivityActivity;
import com.college.xdick.findme.ui.Activity.MyJoinActivity;
import com.college.xdick.findme.ui.Activity.MyLikeActivity;
import com.college.xdick.findme.ui.Activity.MySetActivity;
import com.college.xdick.findme.ui.Activity.SearchActivity;
import com.college.xdick.findme.ui.Activity.SearchActivityActivity;
import com.college.xdick.findme.ui.Activity.SearchUserActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

/**
 * Created by Administrator on 2018/5/19.
 */

public class SearchActivityAdapter extends RecyclerView.Adapter<SearchActivityAdapter.ViewHolder>
      //  implements ItemTouchHelperAdapter
{
    private int ITEM_TYPE_NORMAL = 0;
    private int ITEM_TYPE_HEADER = 1;
    private int ITEM_TYPE_FOOTER = 2;
    private int ITEM_TYPE_EMPTY = 3;




    private List<MyActivity> mActivityList;
    private Context mContext;
    private View mHeaderView;
    private View mFooterView;
    private View mEmptyView;


   private List<MyActivity> allList;





    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,time,host,join;
        ImageView cover;
        CardView cardView;


        public ViewHolder(View view){
            super(view);
            title= view.findViewById(R.id.title_ac);
            cardView =view.findViewById(R.id.cardview_ac);
            cover = view.findViewById(R.id.cover_ac);
            time= view.findViewById(R.id.time_ac);
            host = view.findViewById(R.id.host_ac);
            join = view.findViewById(R.id.join_ac);

        }

     /*   public void setVisibility(boolean isVisible){
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
            if (isVisible){
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
            }else{
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }*/
    }



    public SearchActivityAdapter(List<MyActivity> activity){
        mActivityList = activity;
    }


   public void setList(List<MyActivity>list){

        allList=list;

   }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        if(mContext == null){
            mContext = parent.getContext();
        }


        if (viewType == ITEM_TYPE_HEADER) {
            return new ViewHolder(mHeaderView);
        } else if (viewType == ITEM_TYPE_EMPTY) {
            return new ViewHolder(mEmptyView);
        } else if (viewType == ITEM_TYPE_FOOTER) {


            final ViewHolder holder= new ViewHolder(mFooterView);
            mFooterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SearchActivityActivity.class);
                    intent.putExtra("ACTIVITYLIST",(Serializable) allList);
                    mContext.startActivity(intent);
                }
            });
            return holder;
        } else {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_search_activity, parent, false);
            final ViewHolder holder = new SearchActivityAdapter.ViewHolder(view);


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    MyActivity activity = mActivityList.get(position);
                    Intent intent = new Intent(mContext, ActivityActivity.class);
                    intent.putExtra("ACTIVITY",activity);
                    mContext.startActivity(intent);


                }
            });





            return holder;
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (null != mHeaderView && position == 0) {
            return ITEM_TYPE_HEADER;
        }
        if (null != mFooterView
                && position == getItemCount() - 1) {
            return ITEM_TYPE_FOOTER;
        }
        if (null != mEmptyView && mActivityList.size() == 0){
            return ITEM_TYPE_EMPTY;
        }
        return ITEM_TYPE_NORMAL;

    }


    @Override
    public void onBindViewHolder(final SearchActivityAdapter.ViewHolder holder, int position) {

        int type = getItemViewType(position);

        if (type == ITEM_TYPE_HEADER
                || type == ITEM_TYPE_FOOTER
                || type == ITEM_TYPE_EMPTY) {
            return;
        }


        int realPos = getRealItemPosition(position);

        final MyActivity activity = mActivityList.get(realPos);


         holder.title.setText(activity.getTitle());

         holder.time.setText(activity.getTime());

         Glide.with(mContext).load(activity.getCover())
                 .apply(diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).into(holder.cover);

          holder.host.setText("由"+activity.getHostName()+"发起");

        try {
            holder.join.setText(activity.getJoinUser().length+"人参与");
        }
        catch (Exception e){
            e.printStackTrace();
        }










    }
    private int getRealItemPosition(int position) {
        if (null != mHeaderView) {
            return position - 1;
        }
        return position;
    }



    @Override
    public int getItemCount() {

        if (mActivityList!=null) {

            int itemCount = mActivityList.size();
            if (null != mEmptyView && itemCount == 0) {
                itemCount++;
            }
            if (null != mHeaderView) {
                itemCount++;
            }
            if (null != mFooterView) {
                itemCount++;
            }
            return itemCount;
        }

        return 0;

    }


    public void addHeaderView(View view) {
        mHeaderView = view;
        notifyItemInserted(0);
    }

    public void addFooterView(View view) {
        mFooterView = view;
        notifyItemInserted(getItemCount() - 1);
    }

    public void setEmptyView(View view) {
        mEmptyView = view;
        notifyDataSetChanged();
    }



}


  /*  @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mActivityList,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemDissmiss(int position) {
        //移除数据
        mActivityList.remove(position);
        notifyItemRemoved(position);
    }*/
