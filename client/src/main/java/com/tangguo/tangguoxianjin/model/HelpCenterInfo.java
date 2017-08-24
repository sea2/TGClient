package com.tangguo.tangguoxianjin.model;

import java.util.List;

/**
 * Created by lhy on 2017/5/2.
 */

public class HelpCenterInfo {


    private List<QaListBeanX> qaList;

    public List<QaListBeanX> getQaList() {
        return qaList;
    }

    public void setQaList(List<QaListBeanX> qaList) {
        this.qaList = qaList;
    }

    public static class QaListBeanX {
        /**
         * qaList : [{"text":"测试内容541v","title":"测试内容m559"}]
         * text : 测试内容ip44
         * title : 测试内容73pk
         */

        private String text;
        private String title;
        private List<QaListBean> qaList;

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

        public List<QaListBean> getQaList() {
            return qaList;
        }

        public void setQaList(List<QaListBean> qaList) {
            this.qaList = qaList;
        }

        public static class QaListBean {
            /**
             * text : 测试内容541v
             * title : 测试内容m559
             */

            private String text;
            private String title;

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
}
