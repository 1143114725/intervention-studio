package com.investigate.newsupper.bean;

/**
 * 人员列表Bean
 * Created by EEH on 2018/3/28.
 */

public class ItemPresionBean {
    private String pid;
    private String name;
    private String tel;
    private String nowstate;
    private String nextproject;

    public ItemPresionBean(String pid, String name, String tel, String nowstate, String nextproject) {
        this.pid = pid;
        this.name = name;
        this.tel = tel;
        this.nowstate = nowstate;
        this.nextproject = nextproject;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNowstate() {
        return nowstate;
    }

    public void setNowstate(String nowstate) {
        this.nowstate = nowstate;
    }

    public String getNextproject() {
        return nextproject;
    }

    public void setNextproject(String nextproject) {
        this.nextproject = nextproject;
    }

}
