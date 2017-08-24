package com.tangguo.tangguoxianjin.model;

import java.util.List;

/**
 * Created by lhy on 2017/5/9.
 */

public class NoticeInfo {


    private List<NoticeListBean> noticeList;

    public List<NoticeListBean> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeListBean> noticeList) {
        this.noticeList = noticeList;
    }

    public static class NoticeListBean {
        /**
         * url : http://taobao.com
         * title : 快来借钱吧
         * level : 1
         */

        private String url;
        private String title;
        private String level;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        @Override
        public String toString() {
            return "NoticeListBean{" + "url='" + url + '\'' + ", title='" + title + '\'' + ", level='" + level + '\'' + '}';
        }
    }
}
