package com.college.xdick.findme.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2018/4/2.
 */

public class Dynamics extends BmobObject implements Serializable {


    private String activityId;
    private String activityTitle;
    private String activityCover;
    private String activityTime;
    private String activityHost;


    private String user;
    private String userId;
    private String content;
    private String[] picture;
    private String[] like;
    private Integer replycount=0;
    private Integer likeCount =0;

    private boolean ifAdd2Gallery=false;








    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }




    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getReplycount() {
        return replycount;
    }

    public void setReplycount(int replycount) {
        this.replycount = replycount;
    }

    public String[] getPicture() {
        return picture;
    }

    public void setPicture(String[] picture) {
        this.picture = picture;
    }

    public String[] getLike() {
        return like;
    }

    public void setLike(String[] like) {
        this.like = like;
    }


    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityCover() {
        return activityCover;
    }

    public void setActivityCover(String activityCover) {
        this.activityCover = activityCover;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public String getActivityHost() {
        return activityHost;
    }

    public void setActivityHost(String activityHost) {
        this.activityHost = activityHost;
    }

    public boolean isIfAdd2Gallery() {
        return ifAdd2Gallery;
    }

    public void setIfAdd2Gallery(boolean ifAdd2Gallery) {
        this.ifAdd2Gallery = ifAdd2Gallery;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
