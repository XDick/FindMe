package com.college.xdick.findme.BmobIM;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

import cn.bmob.newim.bean.BmobIMExtraMessage;

/**同意添加好友请求-仅仅只用于发送同意添加好友的消息
 * @author smile
 * @project AgreeAddFriendMessage
 * @date 2016-03-04-10:41
 */
//TODO 好友管理：9.6、自定义同意添加好友的消息类型
public class AgreeAddFriendMessage extends BmobIMExtraMessage {

    //以下均是从extra里面抽离出来的字段，方便获取
    private String uid;//最初的发送方
    private Long time;
    private String msg;//用于通知栏显示的内容

    @Override
    public String getMsgType() {
        //自定义一个`agree`的消息类型
        return "agree";
    }

    @Override
    public boolean isTransient() {
        //此处将同意添加好友的请求设置为false，为了演示怎样向会话表和消息表中新增一个类型，在对方的会话列表中增加`我通过了你的好友验证请求，我们可以开始聊天了!`这样的类型
        return false;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}