package com.investigate.newsupper.util;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.investigate.newsupper.bean.GroupsBean;
import com.investigate.newsupper.bean.ReturnAnswer;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 Created by EEH on 2018/9/5.
 */
public class ResolverXML {

    private static final String TAG = "ResolverXML";
    /**
     唯一单例模式
     @return
     */
    private static ResolverXML mInstance;

    public synchronized static ResolverXML getInstance() {

        if (mInstance == null) {
            mInstance = new ResolverXML();
        }
        return mInstance;
    }
    //下载名单信息和公司组xml
    private String AccessPanelxml = "<survey>\n" + "\t<AccessPanel CustomerID=\"601\">\n" + "\t\t<sub sid=\"PanelPassword\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">受访密码</sub>\n" + "\t\t<sub sid=\"Company\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">所在单位</sub>\n" + "\t\t<sub sid=\"UserName\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">姓名</sub>\n" + "\t\t<sub sid=\"MailAddress\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">邮件地址</sub>\n" + "\t\t<sub sid=\"PanelCode\" len=\"最多120个文字\" note=\"\" modify=\"false\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">受访者编号</sub>\n" + "\t\t<sub sid=\"Phone\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">电话1</sub>\n" + "\t\t<sub sid=\"Phone2\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">电话2</sub>\n" + "\t\t<sub sid=\"Mobile\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">手机1</sub>\n" + "\t\t<sub sid=\"Mobile2\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">手机2</sub>\n" + "\t\t<sub sid=\"Fax\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">传真1</sub>\n" + "\t\t<sub sid=\"Fax2\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">传真2</sub>\n" + "\t\t<sub sid=\"Sex\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">性别</sub>\n" + "\t\t<sub sid=\"Address\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">地址</sub>\n" + "\t\t<sub sid=\"PostCode\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">邮编</sub>\n" + "\t\t<sub sid=\"Married\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">婚姻</sub>\n" + "\t\t<sub sid=\"Birthday\" len=\"*格式2008-08-08\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">生日</sub>\n" + "\t\t<sub sid=\"Age\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">年龄</sub>\n" + "\t\t<sub sid=\"Payment\" len=\"最多120个文字\" modify=\"true\" note=\"\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">月收入</sub>\n" + "\t\t<sub sid=\"Degree\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">学历</sub>\n" + "\t\t<sub sid=\"Industry\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">行业</sub>\n" + "\t\t<sub sid=\"Duty\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">职位</sub>\n" + "\t\t<sub sid=\"Idcard\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">身份证</sub>\n" + "\t\t<sub sid=\"Region\" len=\"最多120个文字\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">省市</sub>\n" + "\t\t<sub sid=\"Extra2\" len=\"不限文字数量\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">备注1</sub>\n" + "\t\t<sub sid=\"Extra3\" len=\"不限文字数量\" note=\"\" modify=\"true\" enable=\"1\" PresetValue=\"\" Permission=\"\" AppointPerson=\"\">备注2</sub>\n" + "\t</AccessPanel>\n" + "\t<Groups>\n" + "\t\t<group sid=\"612\">随访视频6</group>\n" + "\t</Groups>\n" + "</survey>";
    //新建名单提交之后的返回值
    private String SUBMITPANELXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><returnanswer><state>100</state><FeedID>90</FeedID><PanelID>224929</PanelID><SurveyID>9731</SurveyID><SC_ID>4</SC_ID></returnanswer>";

    /**
     解析下载名单信息和公司组xml的方法
     */
    public void ParseAccessPanelxml() {

        
        try {

            InputStream is = new ByteArrayInputStream(AccessPanelxml.getBytes());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);
            Element element = document.getDocumentElement();

            NodeList AccessPanellist = element.getElementsByTagName("AccessPanel");
            if (AccessPanellist != null) {
                Log.i(TAG, "ParseXml: AccessPanellist.getLength()=" + AccessPanellist.getLength());
                for (int i = 0, size = AccessPanellist.getLength(); i < size; i++) {
                    Element AccessPaneElement = (Element) AccessPanellist.item(i);
                    NamedNodeMap AccessPaneMap = AccessPaneElement.getAttributes();
                    if (null != AccessPaneMap) {
                        Node AccessPaneNode = AccessPaneMap.getNamedItem("CustomerID");
                        if (null != AccessPaneNode) {
                            Log.i(TAG, "ParseXml: CustomerID" + AccessPaneNode.getNodeValue().trim());

                        }
                    }
                    NodeList subllist = AccessPaneElement.getElementsByTagName("sub");
                    Log.i(TAG, "ParseXml: subllist.getLength()=" + subllist.getLength());
                    if (null != subllist) {
                        for (int j = 0, subsize = subllist.getLength(); j < subsize; j++) {
                            Element subElement = (Element) subllist.item(j);
                            NamedNodeMap subMap = subElement.getAttributes();

                            if (subElement != null) {
                                String RuleName = subElement.getTextContent();
                                Log.i(TAG, "ParseXml: -----------RuleName--------" + RuleName);
                            }


                            if (null != subMap) {
                                Node sidNode = subMap.getNamedItem("sid");
                                if (null != sidNode) {
                                    Log.i(TAG, "ParseXml: sidNode" + sidNode.getNodeValue().trim());
                                    //                                    Log.i(TAG, "ParseXml: sidNode.getTextContent()="+sidNode.getTextContent());
                                }
                                Node lenNode = subMap.getNamedItem("len");
                                if (null != lenNode) {
                                    Log.i(TAG, "ParseXml: lenNode" + lenNode.getNodeValue().trim());
                                }

                                Node noteNode = subMap.getNamedItem("note");
                                if (null != noteNode) {
                                    Log.i(TAG, "ParseXml: noteNode" + noteNode.getNodeValue().trim());
                                }

                                Node modifyNode = subMap.getNamedItem("modify");
                                if (null != modifyNode) {
                                    Log.i(TAG, "ParseXml: modifyNode" + modifyNode.getNodeValue().trim());
                                }

                                Node enableNode = subMap.getNamedItem("enable");
                                if (null != enableNode) {
                                    Log.i(TAG, "ParseXml: enableNode" + enableNode.getNodeValue().trim());
                                }

                                Node PresetValueNode = subMap.getNamedItem("PresetValue");
                                if (null != PresetValueNode) {
                                    Log.i(TAG, "ParseXml: PresetValueNode" + PresetValueNode.getNodeValue().trim());
                                }

                                Node PermissionNode = subMap.getNamedItem("Permission");
                                if (null != PermissionNode) {
                                    Log.i(TAG, "ParseXml: PermissionNode" + PermissionNode.getNodeValue().trim());
                                }

                                Node AppointPersonNode = subMap.getNamedItem("AppointPerson");
                                if (null != AppointPersonNode) {
                                    Log.i(TAG, "ParseXml: AppointPersonNode" + AppointPersonNode.getNodeValue().trim());
                                }
                            }
                        }
                    }
                }
            }

          

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     上传和修改名单的返回xml解析
     <R><S>97</S><FID>0</FID><RTP></RTP></R>
     */
    public ReturnAnswer SubmitPanelParseXml(String SUBMITPANELXML){
    	ReturnAnswer returnAnswer = new ReturnAnswer();
        try {
        	
            InputStream is = new ByteArrayInputStream(SUBMITPANELXML.getBytes());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);
            Element element = document.getDocumentElement();

            NodeList stateNodeList = element.getElementsByTagName("state");
            if (null != stateNodeList){
                Element stateelement = (Element)stateNodeList.item(0);
                if (stateelement != null) {
                    String state = stateelement.getTextContent();
                    
                    returnAnswer.setState(state);
                    /*如果返回值得state不是100 直接返回*/
                    if (!"100".equals(state)) {
						return returnAnswer;
					}
                    
                    Log.i(TAG, "ParseXml: -----------state--------" + state);
                }
            }


            NodeList FeedIDNodeList = element.getElementsByTagName("FeedID");
            if (null != FeedIDNodeList){
                Element FeedIDelement = (Element)FeedIDNodeList.item(0);
                if (FeedIDelement != null) {
                    String FeedID = FeedIDelement.getTextContent();
                    returnAnswer.setFeedID(FeedID);
                    Log.i(TAG, "ParseXml: -----------FeedID--------" + FeedID);
                }
            }


            NodeList PanelIDNodeList = element.getElementsByTagName("PanelID");
            if (null != PanelIDNodeList){
                Element PanelIDelement = (Element)PanelIDNodeList.item(0);
                if (PanelIDelement != null) {
                    String PanelID = PanelIDelement.getTextContent();
                    returnAnswer.setPanelID(PanelID);
                    Log.i(TAG, "ParseXml: -----------PanelID--------" + PanelID);
                }
            }


            NodeList SurveyIDNodeList = element.getElementsByTagName("SurveyID");
            if (null != SurveyIDNodeList){
                Element SurveyIDelement = (Element)SurveyIDNodeList.item(0);
                if (SurveyIDelement != null) {
                    String SurveyID = SurveyIDelement.getTextContent();
                    returnAnswer.setSurveyID(SurveyID);
                    Log.i(TAG, "ParseXml: -----------SurveyID--------" + SurveyID);
                }
            }


            NodeList SCIDNodeList = element.getElementsByTagName("SC_ID");
            if (null != SCIDNodeList){
                Element SCIDelement = (Element)SCIDNodeList.item(0);
                if (SCIDelement != null) {
                    String SCID = SCIDelement.getTextContent();
                    returnAnswer.setSCID(SCID);
                    Log.i(TAG, "ParseXml: -----------SCID--------" + SCID);
                }
            }






        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return returnAnswer;
    }

    
    /**
     * 公司组的解析
     */
    public  ArrayList<GroupsBean> GroupsParseXml(){
    	
    	ArrayList<GroupsBean> groupsBeans = new ArrayList<GroupsBean>();
    	
        try {
        	 InputStream is = new ByteArrayInputStream(AccessPanelxml.getBytes());
             DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
             DocumentBuilder builder = factory.newDocumentBuilder();
             Document document = builder.parse(is);
             Element element = document.getDocumentElement();
            

        	  NodeList Groupsllist = element.getElementsByTagName("Groups");
              if (Groupsllist != null) {
                  for (int i = 0, size = Groupsllist.getLength(); i < size; i++) {
                      Element GroupsElement = (Element) Groupsllist.item(i);

                      NodeList groupllist = GroupsElement.getElementsByTagName("group");
                      Log.i(TAG, "ParseXml: subllist.getLength()=" + groupllist.getLength());
                      if (null != groupllist) {
                          for (int j = 0, groupsize = groupllist.getLength(); j < groupsize; j++) {
                              GroupsBean groupsBean = new GroupsBean();
                              Element subElement = (Element) groupllist.item(j);
                              NamedNodeMap groupMap = subElement.getAttributes();

                              if (subElement != null) {
                                  String groupRuleName = subElement.getTextContent();
                                  groupsBean.setGroupName(groupRuleName);
                                  Log.i(TAG, "ParseXml: -----------groupRuleName--------" + groupRuleName);
                              }
                              if (null != groupMap) {
                                  Node groupsidNode = groupMap.getNamedItem("sid");
                                  if (null != groupsidNode) {
                                      String sid = groupsidNode.getNodeValue().trim();
                                      groupsBean.setSid(sid);
                                      Log.i(TAG, "ParseXml: groupsidNode" + sid);
                                  }
                              }
                              groupsBeans.add(groupsBean);
                          }
                      }
                  }
              }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return groupsBeans;
    }

}
