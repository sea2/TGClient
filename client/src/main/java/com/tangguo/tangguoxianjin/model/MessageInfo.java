package com.tangguo.tangguoxianjin.model;

import java.util.List;

/**
 * Created by lhy on 2017/5/9.
 */

public class MessageInfo {


    /**
     * messageList : [{"date":"测试内容cw42","status":"测试内容eecy","text":"测试内容n834","title":"测试内容x856"}]
     * pageNo : 1
     * unread : 1
     */

    private int pageNo;
    private int unread;
    private List<MessageListBean> messageList;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public List<MessageListBean> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageListBean> messageList) {
        this.messageList = messageList;
    }

    public static class MessageListBean {
        /**
         * date : 测试内容cw42
         * status : 测试内容eecy
         * text : 测试内容n834
         * title : 测试内容x856
         */

        private String date;
        private String status;
        private String text;
        private String title;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
