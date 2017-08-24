package com.tangguo.tangguoxianjin.model;

import java.util.List;

/**
 * Created by lhy on 2017/5/16.
 */

public class BorrowProgressInfo {


    /**
     * canRepay : 测试内容jhq1
     * recordList : [{"isSelected":"测试内容1jp7","text":"测试内容55q8","title":"测试内容15ju"}]
     * status : 测试内容6q8r
     */

    private String canRepay;
    private String status;
    private List<RecordListBean> recordList;

    public String getCanRepay() {
        return canRepay;
    }

    public void setCanRepay(String canRepay) {
        this.canRepay = canRepay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RecordListBean> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<RecordListBean> recordList) {
        this.recordList = recordList;
    }

    public static class RecordListBean {
        /**
         * isSelected : 测试内容1jp7
         * text : 测试内容55q8
         * title : 测试内容15ju
         */

        private String isSelected;
        private String text;
        private String title;

        public String getIsSelected() {
            return isSelected;
        }

        public void setIsSelected(String isSelected) {
            this.isSelected = isSelected;
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
