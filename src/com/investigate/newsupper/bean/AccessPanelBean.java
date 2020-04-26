package com.investigate.newsupper.bean;

/**
 Created by EEH on 2018/8/30.
 */
public class AccessPanelBean {
//<sub sid="Address" len="最多120个文字" note="" modify="true" enable="1" PresetValue="" Permission="" AppointPerson="">地址</sub>
    private String sid;//变量名
    private String len;//
    private String note;//改名
    private String modify;//
    private String enable;//
    private String AppointPerson;//
    private String Permission;//权限
    private String showtext;//显示文字
    private String submit;//提交数据
    private String PresetValue;//是否是下拉框以及下拉框的数据
    private String CustomerID;//
    
    public AccessPanelBean() {

    }
    /**
     @param sid 变量名
     @param note 改名
     @param permission 权限
     @param showtext 显示文字
     @param submit 提交数据
     @param presetValue 是否是下拉框以及下拉框的数据
     */
    public AccessPanelBean(String sid, String note, String permission, String showtext, String submit, String presetValue) {

        this.sid = sid;
        this.note = note;
        Permission = permission;
        this.showtext = showtext;
        this.submit = submit;
        PresetValue = presetValue;
    }

    @Override
    public String toString() {

        return "AccessPanelBean{" + "sid='" + sid + '\'' + ", note='" + note + '\'' + ", Permission='" + Permission + '\'' + ", showtext='" + showtext + '\'' + ", submit='" + submit + '\'' + ", PresetValue='" + PresetValue + '\'' + '}';
    }


    public String getCustomerID() {

        return CustomerID;
    }

    public void setCustomerID(String customerID) {

        CustomerID = customerID;
    }
    
    public String getLen() {

        return len;
    }

    public void setLen(String len) {

        this.len = len;
    }

    public String getModify() {

        return modify;
    }

    public void setModify(String modify) {

        this.modify = modify;
    }

    public String getEnable() {

        return enable;
    }

    public void setEnable(String enable) {

        this.enable = enable;
    }

    public String getAppointPerson() {

        return AppointPerson;
    }

    public void setAppointPerson(String appointPerson) {

        AppointPerson = appointPerson;
    }

    public String getSid() {

        return sid;
    }

    public void setSid(String sid) {

        this.sid = sid;
    }

    public String getNote() {

        return note;
    }

    public void setNote(String note) {

        this.note = note;
    }

    public String getPermission() {

        return Permission;
    }

    public void setPermission(String permission) {

        Permission = permission;
    }

    public String getShowtext() {

        return showtext;
    }

    public void setShowtext(String showtext) {

        this.showtext = showtext;
    }

    public String getSubmit() {

        return submit;
    }

    public void setSubmit(String submit) {

        this.submit = submit;
    }

    public String getPresetValue() {

        return PresetValue;
    }

    public void setPresetValue(String presetValue) {

        PresetValue = presetValue;
    }
}
