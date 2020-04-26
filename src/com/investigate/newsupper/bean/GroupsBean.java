package com.investigate.newsupper.bean;

/**
 Created by EEH on 2018/9/5.

 <Groups>
     <group sid="612">随访视频6</group>
 </Groups>
 */
public class GroupsBean {

    private String sid;//公司组编号
    private String GroupName;//公司组名称

    public GroupsBean() {

    }

    public GroupsBean(String sid, String groupName) {
        this.sid = sid;
        GroupName = groupName;
    }

    @Override
    public String toString() {
        return "GroupsBean{" + "sid='" + sid + '\'' + ", GroupName='" + GroupName + '\'' + '}';
    }

    public String getSid() {

        return sid;
    }

    public void setSid(String sid) {

        this.sid = sid;
    }

    public String getGroupName() {

        return GroupName;
    }

    public void setGroupName(String groupName) {

        GroupName = groupName;
    }
}
