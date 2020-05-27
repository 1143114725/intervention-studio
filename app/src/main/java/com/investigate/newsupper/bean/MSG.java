package com.investigate.newsupper.bean;

import java.util.List;

/**
 Created by EEH on 2018/9/20.
 */
public class MSG {

    /**
     * state : 99
     * msg : 有重复
     * feedid : 26
     * data : [{"index":"1","cause":"编号存在重复"},{"index":"1","cause":"身份证号存在重复"},{"index":"1","cause":"手机号存在重复"}]
     */

    private String state;
    private String msg;
    private String feedid;
    private List<DataBean> data;

    public String getState() {

        return state;
    }

    public void setState(String state) {

        this.state = state;
    }

    public String getMsg() {

        return msg;
    }

    public void setMsg(String msg) {

        this.msg = msg;
    }

    public String getFeedid() {

        return feedid;
    }

    public void setFeedid(String feedid) {

        this.feedid = feedid;
    }

    public List<DataBean> getData() {

        return data;
    }

    public void setData(List<DataBean> data) {

        this.data = data;
    }

    public static class DataBean {

        /**
         * index : 1
         * cause : 编号存在重复
         */

        private String index;
        private String cause;

        public String getIndex() {

            return index;
        }

        public void setIndex(String index) {

            this.index = index;
        }

        public String getCause() {

            return cause;
        }

        public void setCause(String cause) {

            this.cause = cause;
        }
    }
}
