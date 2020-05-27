package com.investigate.newsupper.bean;

/**
 Created by EEH on 2018/9/20.
 */
//"<R><S>100</S><FID>13</FID><RTP>100</RTP><MSG>json</MSG></R>";
public class Repeat {
    String S;
    String FID;
    String RTP;
    MSG MSG;

    public Repeat() {
    }

    public Repeat(String s, String FID, String RTP, MSG MSG) {

        S = s;
        this.FID = FID;
        this.RTP = RTP;
        this.MSG = MSG;
    }

    public String getS() {

        return S;
    }

    public void setS(String s) {

        S = s;
    }

    public String getFID() {

        return FID;
    }

    public void setFID(String FID) {

        this.FID = FID;
    }

    public String getRTP() {

        return RTP;
    }

    public void setRTP(String RTP) {

        this.RTP = RTP;
    }

    public MSG getMSG() {

        return MSG;
    }

    public void setMSG(MSG MSG) {

        this.MSG = MSG;
    }
}
