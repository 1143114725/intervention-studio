package com.investigate.newsupper.intervention;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.investigate.newsupper.bean.Answer;
import com.investigate.newsupper.bean.AnswerMap;
import com.investigate.newsupper.global.MyApp;
import com.investigate.newsupper.util.BaseLog;
import com.investigate.newsupper.util.DialogUtil;
import com.investigate.newsupper.util.ListUtils;
/**
 * 常驻干预
 * @author EraJi
 * @date 2020年4月26日22:03:12
 *
 */
public class Interventionutil {
	public  int surveyId;
	public  MyApp ma;
	public  String uuid;
	
	public Interventionutil(int surveyId, MyApp ma, String uuid) {
		super();
		this.surveyId = surveyId;
		this.ma = ma;
		this.uuid = uuid;
	}
	
	/**
	 * 获取答案
	 * 
	 * @param index
	 * @return
	 */
	public Answer getanswer(String index) {
		Answer p4aans = ma.dbService.getAnswer(uuid, index);
		if (p4aans != null && p4aans.getAnswerMapArr() != null) {
			return p4aans;
		}
		return null;

	}
	
	public String getRowText(String str){
		String rowtext = "";
		String s[] = str.split("@@");
		
		Answer ans = getanswer(s[0]);
		if (ans != null) {
			for (int i = 0; i < ans.getAnswerMapArr().size(); i++) {
				AnswerMap ansmaperlist = ans.getAnswerMapArr().get(i);
				if (ansmaperlist.getAnswerValue().equals(s[1])) {
					rowtext = ansmaperlist.getAnswerText();
				}
			}
		}
		return rowtext;
	}
	
	

}
