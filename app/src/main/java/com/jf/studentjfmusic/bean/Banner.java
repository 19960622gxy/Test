package com.jf.studentjfmusic.bean;

import java.util.List;

/**
 * 将Json 封装为对象
 * Created by weidong on 2017/6/8.
 */

public class Banner {
    private List<ResultsBean> results;

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * picurl : http://or49nvrps.bkt.clouddn.com/5.jpg
         * desc : 独家推荐，薛之谦手绘漫画”
         * createdAt : 2017-06-06T08:24:43.333Z
         * updatedAt : 2017-06-06T08:24:43.333Z
         * objectId : 593666cbac502e0068bf478c
         */

        private String picurl;
        private String desc;
        private String createdAt;
        private String updatedAt;
        private String objectId;

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }
    }


//    ArrayList<Banner_Results> results;
//
//    public static class Banner_Results{
//        private String picurl;
//        private String desc;
//        private String createdAt;
//        private String updatedAt;
//        private String objectId;
//
//        public String getPicurl() {
//            return picurl;
//        }
//
//        public void setPicurl(String picurl) {
//            this.picurl = picurl;
//        }
//
//        public String getDesc() {
//            return desc;
//        }
//
//        public void setDesc(String desc) {
//            this.desc = desc;
//        }
//
//        public String getCreatedAt() {
//            return createdAt;
//        }
//
//        public void setCreatedAt(String createdAt) {
//            this.createdAt = createdAt;
//        }
//
//        public String getUpdatedAt() {
//            return updatedAt;
//        }
//
//        public void setUpdatedAt(String updatedAt) {
//            this.updatedAt = updatedAt;
//        }
//
//        public String getObjectId() {
//            return objectId;
//        }
//
//        public void setObjectId(String objectId) {
//            this.objectId = objectId;
//        }
//    }
//
//    public ArrayList<Banner_Results> getResults() {
//        return results;
//    }
//
//    public void setResults(ArrayList<Banner_Results> results) {
//        this.results = results;
//    }
}
