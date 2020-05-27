package com.investigate.newsupper.bean;

import java.util.List;

/**
 * 同步答案的bean
 * @author EraJieZhang
 * @data 2018/11/27
 */
public class SysFeedAnswer{

    private List<DataBean> data;

    public List<DataBean> getData() {

        return data;
    }

    public void setData(List<DataBean> data) {

        this.data = data;
    }

    public static class DataBean {

        /**
         * questionindex : 2
         * answermap : [{"answerName":"VIS_2_2_0_2_10_3_0_0_0","answerValue":"12"},{"answerName":"VIS_2_2_1_2_10_3_0_0_0","answerValue":"4"},{"answerName":"VIS_2_2_2_2_10_3_0_0_0","answerValue":"8"}]
         */

        private String questionindex;
        private List<AnswermapBean> answermap;

        public String getQuestionindex() {

            return questionindex;
        }

        public void setQuestionindex(String questionindex) {

            this.questionindex = questionindex;
        }

        public List<AnswermapBean> getAnswermap() {

            return answermap;
        }

        public void setAnswermap(List<AnswermapBean> answermap) {

            this.answermap = answermap;
        }

        public static class AnswermapBean {

            /**
             * answerName : VIS_2_2_0_2_10_3_0_0_0
             * answerValue : 12
             */

            private String answerName;
            private String answerValue;

            public String getAnswerName() {

                return answerName;
            }

            public void setAnswerName(String answerName) {

                this.answerName = answerName;
            }

            public String getAnswerValue() {

                return answerValue;
            }

            public void setAnswerValue(String answerValue) {

                this.answerValue = answerValue;
            }
        }
    }
    
}
