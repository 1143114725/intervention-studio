package com.investigate.newsupper.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;



import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.media.MediaPlayer;
import android.net.NetworkInfo;
import android.telephony.SignalStrength;
import android.view.KeyEvent;





public class FusionField
{
	/**
	 * ���浱ǰ��Activity
	 */
	public static Activity currentActivity = null;
	
	public static Activity videoActivity = null;

	/**
	 * ���浱ǰ��context
	 */
	public static Context currentContext = null;
	
	/**
	 * ����activity��״̬�����ڲ�׽HOME����ʹ�ó��򵽺�̨��N���Ӻ���˳�
	 */
	public static AbstractMap<Activity, String> activityStatus = new ConcurrentHashMap<Activity, String>();

	/**
	 * ��̨�˳�ʱ����
	 */
	public static int iDelayShutdown = 300000;
	
	//add  by zhangkun 2011.9.20 begin
	/**
	 * launcherҳ��δ����Ϣ����
	 */
	public static int launcherUnreadMsgNum= 0;
	//add  by zhangkun 2011.9.20 end
	
	/**
	 * Ȧ��δ����Ϣ����
	 */
	public static int groupUnreadMsgNum = 0;
	
	/**
	 * ��ǰ������󣨵��ġ�Ȧ�ӡ�Ⱥ������JID
	 */
	public static String currentChatWith = null;

	/**
	 * ��ǰ�Ƿ������������Activity��ջ�У�����������������ҳ��ʱ����0������������棬1���ڵ��Ľ��棬2����Ⱥ�Ľ���
	 */
	public static int activityInStack = 0;
	
	/**
	 * ��ǰҳ�棺0����������棬1�ڵ��Ľ��棬2��Ⱥ�Ľ���
	 */
	public static int currentPage = 0;
	
	/**
	 * ����Ⱥ��ҳ��Ķ��󣨴˱������������š����мɣ�
	 */
	public static Activity groupActivity = null;

	/**
	 * ���浱ǰ���л��activity
	 */
	public static Vector<Activity> mActivityList = new Vector<Activity>();

	/**
	 * �㲥��Ϣ
	 */
	public static final String Setting_Broadcast = "Setting_Broadcast";

	/**
	 * ��Ļ�ֱ���
	 */
	public static float currentDensity;
	public static int currentDensityDpi;
	public static int currentWidthPixels;
	public static int currentHeightPixels;
	public static String currentscreenSize = null;

	/**
	 * ���浱ǰnotification���Ǹ����Ļ���Ⱥ�ĵ�ID
	 */
	public static String notificationTag = null;
	/**
	 * �ֻ���
	 */
	public static String ACCOUNT = "";

	/**
	 * ��֤��
	 */
	public static String PASSWORD = "";

	/**
	 * �û���
	 */
	public static String NAME = "";

//	/**
//	 * ���ڱ�����ת��¼��list
//	 */
//	public static ArrayList<Activity> activityList = new ArrayList<Activity>();

	/**
	 * ��������Ӧ���Ƿ��ں�̨����
	 */
	public static boolean isBackgroundRun = false;
	
	
	/**
	 * ���������õ绰�Ƿ񹫿�: 1�ǹ���;0��δ����
	 */
	public static String isOpenPhone = "-1";
	/**
	 * ���������������Ƿ񹫿�
	 */
	public static String isOpenEmail = "-1";
	/**
	 * ���������Ƿ�����
	 */
	public static String isOpenSearchable = "-1";

	/**
	 * �Ƿ��Ǹ�����Ƭ
	 */
	public static boolean isUpdateCard=false;

	/**
	 * �û���hotalk��sid
	 */
	public static String HOTALK_SID = null;
	/**
	 * �������·���base64 ������� sid=dlfjslfjssdfsdf&uid=5120123
	 */
	public static String HOTALK_BASE64_SID_UID = null;
	
	/**
	 * �Խ�Hotalk��ý���������֤��
	 */
	public static String HOTALK_MCODE = null;
	
	//hkw 2011.10.26  ͳһ�˺�֧�� start
	public static String HOTALK_SERVICE_TOKEN = null;
	public static String HOTALK_BASE64_SERVICE_TOKEN = null;
	public static String accountName = null;
	public static String accountType = null;	
	//hkw 2011.10.26  ͳһ�˺�֧�� end

	/**
	 * �û���hotalk��jid
	 */
	public static String HOTALK_JID = null;

	/**
	 * �û���hotalk��id
	 */
	public static String HOTALK_ID = null;
	
	/**
	 * PUSH����ʹ�õ�Device Token
	 */
	public static String DEVICE_TOKEN = null;

	public static String HOTALK_CT = "0";

	public static String HOTALK_ROSTER_VER = "";
	
	/**
	 * ����VCard�汾�����ֵ
	 */
	public static String HOTALK_ROSTER_VCARD_MAX_VER = null;
	
	/**
	 * ��ȡ����������Ŀ
	 */
	public static int SERVICE_AVAILABLE_MAXNUM = 200;

	/**
	 * ���öԻ���İ����¼�
	 */
	public static OnKeyListener onKeyListener = new OnKeyListener()
	{
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
		{
			if (keyCode == KeyEvent.KEYCODE_BACK
					|| keyCode == KeyEvent.KEYCODE_SEARCH)
			{
				return true;
			}
			return false;
		}
	};

	/**
	 * ����ǩ������󳤶�
	 */
	public static int SIGNATURE_MAX_LEN = 20;
	
	/**
	 * ���涨���ǵĳ������õ���dialog��id
	 */

	/**
	 * ��¼ʱ�ĵȴ��� 
	 */
//	public final static int DIA_LOGIN_WAIT = 0;
//	public final static int DIA_REGISTER_WAIT = 1;
	public final static int DIA_LOADINDFRIEND_WAIT = 2;
	// add by xiao. 20110823. begin
	public final static int DIA_LOADINDCHATLOG_WAIT = 3;
	public final static int DIA_ADDFRIEND_WAIT = 4;
	public final static int DIA_INITGROUPMEMBERS_WAIT = 5;
	// add by xiao. 20110823. end
	public final static int DIA_SENDING_VERIFY_WAIT = 6;//kang.chen ������֤���ŵĵȴ���
	
	public final static int DIA_REGISTER_AUTO_WAIT = 7;//gaogy �յ���֤�����Զ���֤
	
	// ������ϵ����ʾ
	public final static int DIA_LOADING_CONTACTS_WAIT = 8;
	
	// ����Ⱥ����ʾ
	public final static int DIA_CREATE_BROADCASTCHAT_WAIT = 9;
	
	public static String verifyCode;//gaogy �����Զ���֤���ȿ���ʾ
	
	/**
	 * ȫ��Ψһ��ʱ��
	 */
	public static Timer timer = new Timer();
	
	/**
	 * ������
	 */
	public static String NSP_STATUS = "NSP_STATUS";

	/**
	 * ���糬ʱʱ��
	 */
	public final static int MAX_CONNECTION_TIMEOUT = 30000;

	/**
	 * ��ʼ���ͻ��˵�Secretֵ
	 */
	//	public static String nspSecret = "d72893b0c15aa78bf5658d89b368452c";
	public static String nspSecret = "563175cbf74b2a1f8238740682998669";
	
	//gaogy 8-23 ������־�ϴ� start
	/**
	 * ���ڱ�����־�ϴ���ϵͳsid
	 */
	public static String sysSid = "YuT85NwuuNZzuuCG45ouTWgm-WTAwvQgSW0w9rR7QU-4G42Z";
	
	public static String SN = "android 2.1";

	/**
	 * �ɹ������б�
	 */
	public static String SUCCESSLIST = "successList";

	/**
	 * ʧ�ܷ����б�
	 */
	public static String FAILLIST = "failList";

	public static final String CONNECTION_AGREENMENT = "http://";

	/**
	 * ���ع�����������ͬʱ���ص�������
	 */
	public static int maxDownloadCount = 3;

	/**
	 * sd���Ƿ���ڵı�־
	 */
	public static boolean sdCardIsExist = true;

	/**
	 * Ⱥ��Ϣ����hashmap������ÿ��Ⱥ��Ϣ����
	 * Ĭ�������Ϊopen
	 */
	public static ArrayList<String> GroupChatNotificationList = new ArrayList<String>();

	public static Activity groupChat;

	public static final int FRIEND_TYPE_NONE = 1;
	public static final int FRIEND_TYPE_FROM = 2;
	public static final int FRIEND_TYPE_TO = 3;
	public static final int FRIEND_TYPE_BOTH = 4;
	public static final int FRIEND_TYPE_REMOVE = 5;

	public static final int MAPSIZE = 280;
	//��ȡ����ͼʹ�ã�����ͼ�Ŀ������Ϊ280���߶�=�����������280,�˲���=Ĭ��ͼƬ�߶�/Ĭ��ͼƬ���
	public static final float MAP_HEIGHT_DIV_WIDTH = 0.5893f;
	public static final int MAPZOOM = 12;
	public static final int LATLONSIZE = 9;

	public static final String SYSTEMLANGUAGE = Locale.getDefault()
			.getLanguage();

	public static String VERSIONNAME = null;

	/**
	 * ���������ش洢��ַ
	 */
	public static final String UPDATE_VERSION_SAVEPATH = "/sdcard/hotalk/update/";

	/**
	 * ����ƥ��������ʽ
	 */
	public static final String EMAIL_MATCH = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*"
			+ "+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";

	public static String UPDATE_FILE_NAME = "Hotalk.apk";
	public static String UPDATE_PUSH_NAME = "Hotalk.apk";

	/**
	 * Ĭ����Ļ�ܶ�
	 */
	public static final float DEFAULT_DENSITY = 1.0f;

	/**
	 * ����Ļ�ܶ�
	 */
	public static final float HIGH_DENSITY = 1.5f;
	
	/**
	 * 640 * 960�ֱ���
	 */
	public static final float HIGH_BIG_DENSITY = 2.0f;
	
	/**
	 * ����Ļ�ܶ�
	 */
	public static final float LOW_DENSITY = 0.75f;	
	public static int bitmapSize_Vcard_hbig = 164;
	public static int bitmapSize_FriendList_hbig = 96;
	public static int bitmapSize_Zoom_hbig = 320;
	public static int bitmapSize_Image_hbig = 960;	
	public static int bitmapSize_Member_hbig = 112;
	
	public static int chat_Image_mh_hbig = 276;
	public static int chat_Image_mw_hbig = 208;
	public static int chat_Image_min_h_hbig = 192;
	public static int chat_Image_min_w_hbig = 272;
	public static int chat_Image_forward_h_hbig = 54;
	public static int chat_Image_forward_w_hbig = 116;
	public static int chat_fomulat_alter_position_min_hbig = 82; 
	public static int chat_fomulat_alter_position_max_hbig = 154;
	public static int chat_Slide_subject_w_hbig = 12;
	public static int chat_Image_Bubble_h_hbig = 13;
	
	public static int bitmapSize_Vcard_h = 126;
	public static int bitmapSize_FriendList_h = 64;
	public static int bitmapSize_Zoom_h = 240;
	public static int bitmapSize_Image_h = 800;	
	public static int bitmapSize_Member_h = 84;
	
	public static int chat_Image_mh_h = 208;
	public static int chat_Image_mw_h = 157;
	public static int chat_Image_min_h_h = 145;
	public static int chat_Image_min_w_h = 205;
	public static int chat_Image_forward_h_h = 41;
	public static int chat_Image_forward_w_h = 87;
	public static int chat_fomulat_alter_position_min_h = 62; 
	public static int chat_fomulat_alter_position_max_h = 116;
	public static int chat_Slide_subject_w_h = 9;
	public static int chat_Image_Bubble_h_h = 10;
 
	public static int bitmapSize_Vcard_m = 82;
	public static int bitmapSize_FriendList_m = 48;
	public static int bitmapSize_Zoom_m = 160;
	public static int bitmapSize_Image_m = 480;	
	public static int bitmapSize_Member_m = 56;
	
	public static int chat_Image_mh_m = 138;
	public static int chat_Image_mw_m = 104;
	public static int chat_Image_min_h_m = 96;
	public static int chat_Image_min_w_m = 136;
	public static int chat_Image_forward_h_m = 27;
	public static int chat_Image_forward_w_m = 58;
	public static int chat_fomulat_alter_position_min_m = 43; 
	public static int chat_fomulat_alter_position_max_m = 77;
	public static int chat_Slide_subject_w_m = 6;
	public static int chat_Image_Bubble_h_m = 7;
	
	public static int bitmapSize_Vcard_l = 59;
	public static int bitmapSize_FriendList_l = 34;
	public static int bitmapSize_Zoom_l = 80;
	public static int bitmapSize_Image_l = 320;	
	
	public static int chat_Image_mh_l = 106;
	public static int chat_Image_mw_l = 80;
	public static int chat_Image_min_h_l = 74;
	public static int chat_Image_min_w_l = 105;
	public static int chat_Image_forward_h_l = 20;
	public static int chat_Image_forward_w_l = 43;
	public static int bitmapSize_Member_l = 42;
	public static int chat_fomulat_alter_position_min_l = 33; 
	public static int chat_fomulat_alter_position_max_l = 59;
	public static int chat_Slide_subject_w_l = 5;
	public static int chat_Image_Bubble_h_l = 4;

	public static int ImageSize_Vcard = 59;
	public static int ImageSize_Icon = 34;
	public static int ImageSize_Zoom = 80;
	public static int bitmapSize_Image = 320;
	public static int ImageSize_Member = 84;
	/**
	 * �Ự�����ͼƬ�߶ȺͿ�ȵ�����
	 */
	public static int chat_Image_min_h = 145;
	public static int chat_Image_min_w = 205;
	public static int chat_Image_mh = 208;
	public static int chat_Image_mw = 157;
	/**
	 * �л�ģʽ�ɹ���ʾ��λ��
	 */
	public static int chat_fomulat_alter_position_min = 62;
	
	public static int chat_fomulat_alter_position_max = 116;
	/**
	 * �Ự�����ͼƬ�ײ���ʾ����ת������
	 */
	public static int chat_Image_forward_h = 41;
	public static int chat_Image_forward_w = 87;
	
	/**
	 * �Ự�����ͼƬת����������ʽ����
	 * h:�����εĸ�
	 */
	public static int chat_Image_Bubble_h = 10;
	
	/**
	 * ��������ͼƬ��Ⱥ͵�ͼ����ֵ
	 */
	public static int chat_Slide_subject_w = 9;
	
	public static String HOTALK_VERSION = null;
	
	public static String HOTALK_FILE_SIZE = null;

	public static String HOTALK_DESCRIPTION = null;
	
	/**
	 * ��Ƶ������
	 */
	public static MediaPlayer mp = new MediaPlayer();
	
	/**
	 * ������ն�
	 */
	public static HashMap<String, String> jidMap = new HashMap<String, String>();
	
	public static boolean isConnectionOK = false;
	
	public static boolean MaybeFirstTime = true;
	
	//gaogy 8-17��ʱʱ����10���Ϊ30�� start
	public static int SYSTEM_TIME_OUT = 30000;
	//gaogy 8-17��ʱʱ����10���Ϊ30�� end
	
	public static String SESSIONID = null;
	
	public static String SEQUNCE = "0";
	
	/**
	 * ����������Ϣ�Ƿ�֪ͨ
	 */
	public static boolean isSingleDelayNotification = true;
	/**
	 * Ⱥ��������Ϣ�Ƿ�֪ͨ
	 */
	public static boolean isGroupDelayNotification = true;

	/**
	 * �½���Ϣ��ҳ����תʱЯ���Ķ��󼯺�
	 * wei.han
	 */


	/**
     * sms��provider��ַ
     */
    public static String SMS_PROVIDER_URI = "content://sms/";
    
    public static String MMS_PROVIDER_URI = "content://mms/";
    
    /**
     * sms�ĵĻỰ
     */
    public static String SMS_THREAD_URI = "content://sms/conversations";
    
    /**
     * mms-sms�ĻỰ
     */
    public static String MMS_SMS_THREAD_URI = "content://mms-sms/conversations?simple=true";
    
    /**
     * mms-sms�Ự��Ӧ���ֻ�����
     */
    public static String SMS_PHONE_URI = "content://mms-sms/canonical-addresses";

	// add by xiaojie.huang 2001.9.13
	public static boolean isPhoneInService = false;
	// end by xiaojie.huang 2001.9.13

	/**
	 * ���һ�����ŵ�ID
	 */
	public static int smsLastId = 0;
	
	/**
	 * ���һ������ID
	 */
	public static int mmsLastId = 0;
	
	/**
	 * ��ǰ�Ự��Ӧ���ֻ�����
	 */
	public static String smsPhoneNumber = "";
	
	/**
	 * ��־�����Ƿ�������
	 */
	public static boolean isNetworkConntected = false;
	
	/**
	 * �����ý���ϴ���dbank��Ȩ�ķ��ؽ����
	 */
	public static String dbank_upload_host = null;
	public static String dbank_upload_secret = null;
	public static String dbank_upload_nsp_tstr = null;
	
	/**
	 * �����ж��ֻ��Ƿ�֧��google�͸ߵµ�ͼ���������ֵ�ͼ�л�
	 */
	public static boolean isSupportAllMap = false;
	
	/**
	 * ��¼�´ε����ͼ��ť��ʹ���������͵�ͼ
	 * 0  ��һ�ν����ͼ
	 * 1  ʹ��google��ͼ
	 * 2  ʹ�øߵµ�ͼ
	 */
	public static int nextUseMap = 0;
	/**
	 * �Ƿ������յ�ͼƬ
	 */
	public static String isphotograph_key="imageisphotograph"; 

	public static  final String refreshQuickReplyBroadcast = "com.hotalk.ui.chat.singleChat.QuickReplyActivity";
	
	/**
	 * message����Action
	 */
	public static String INTENT_ACTION_MESSAGE = "com.hotalk.server.receiver.MessageReceiver";
	
	public static boolean IsDeleteChat = false;

	/**
	 *  ��token��Чʱ�����������ص�ʧ����ϢΪ
     *  <failure xmlns="urn:hotalk:params:xml:ns:xmpp-sasl"><invalid-servicetoken/></failure> 
	 */
	public static String AUTH_INVALID_SERVICETOKEN_TAG = "invalid-servicetoken";
	
	/**
	 * ��ǰ����apn�Ƿ��� wei.han
	 */
	public static NetworkInfo mmsInfo = null;
	
	/**
	 * ������¼�ֻ��ź�ǿ�� wei.han
	 */
	public static SignalStrength mSignalStrength = null;
	
	/**
	 * �ж��Ƿ���ʾ���ٻظ���
	 */
	public static boolean isReplyAppear = true;
	
	/**
	 * ����ʱ�������Զ�У���¼������������5�Σ���ֹ��֧�����о�Ĭע����û�һֱ����֤
	 * ������ÿ�ζ�Ҫ��ʾ��֤
	 */
	public static final int AUTO_REGIST_MAX_TIMES = 5;
	
	/**
     * �û�nicknameû���Զ���ʱ����ʾ�û�����nickname��������ʾ�������2��
     */
    public static final int NOTIFY_SET_NICKNAME_MAX_TIMES = 2;
    
    /**
     * ��ʾ�û������ͻ��ˣ�������ʾ�������3��
     */
    public static final int NOTIFY_UPDATE_MAX_TIMES = 3;
    
    /**
     * ��ʾ�û�����nickname����չʾ
     */
    public static boolean NOTIFY_SET_NICKNAME_SHOW = false;
	
	public static boolean IMAGE_PREVIEW = true;
	
	/**
	 * ���������С�����ڻػ��б���ϵ�˵Ƚ���ĺ���������
	 */
	public static float NAME_TEXT_SIZE = 18;
	
	/**
     * ���������һ����Ϣ�ı������С�����ڻỰ�б����һ����Ϣչʾ����ϵ�˽���绰�����ı������С��
     */
	public static float CHAT_TEXT_SIZE = 14.5f;
	
	/**
     * �Ự�б�����Ϣʱ����ʾ�����С
     */
	public static float TIME_TEXT_SIZE = 11.5f;
	
	/**
     * �Ự�����ı���Ϣ�����С�����ڵ���Ⱥ�ĻỰ�ı���Ϣ�����С��
     */
	public static float CHATBODY_TEXT_SIZE = 17;
	
	/**
     * �Ự������Ϣʱ�������С�����ڵ���Ⱥ�ĻỰ������Ϣʱ����ʾ�����С��
     */
	public static float CHATTIME_TEXT_SIZE = 10;

//	public static float SET_NAME_TEXT_SIZE = 17.3f;
	
	/**
     * ���ý������ѡ�������С,�˵������С
     */
	public static float SET_TYPE_TEXT_SIZE = 16;
	
	/**
     * ���ý������ѡ���¼��˵�������С
     */
	public static float SET_SMALL_TEXT_SIZE = 12;
	
	
	public static float FAVORITES_NAME_TEXT_SIZE = 14.5f;  
	
	/**
	 * SecretKey, ���ڼ���
	 */
	public static String SECRET_KEY = null;

	/**
	 * mCode�� ������hotalk�¶�ý���ļ��������ϴ�/�����ļ���֤
	 */
	public static String MCODE = null;
	
	/**
	 * �����¼ʱ����ʾ��true ��ʾ���һ����Ϣ��ʱ��  false ��ʾ���һ��������Ϣ��ʱ��
	 */
	public static boolean changeMessageTimeDisplayMode = false;
	
	/**
	 * ������������������push agent�汾��
	 */
	public static String PUSH_VERSION = null;
	/**
	 * ����Push Service�ĵ�ַ
	 */
	public static String PUSH_UPDATE_URL = null;
	
	/**
	 * �ж��Ƿ���������ף������
	 */
	public static boolean IS_REQUEST_BLESSSMS = false;
	
    // add by jiangzhihua 2012-03-26 ���Ⱥ��ҳ��鿴
    /**
     * �Ƿ���Ⱥ��ҳ���״μ���
     */
    public static boolean IS_FIRST_LOAD = true;

    /**
     * Http������Ϣ��Я�������ڷ���������֤�ַ���
     */
    public static String HTTP_SECRET_KEY = null;
    
    /**
     * ������������handler����
     */
	public static ArrayList<Object> updateHandler = new ArrayList<Object>();
    
    /**
     * default date format
     * Ĭ�ϵ�ʱ���ʽ
     * The setting is not set; use the default.
     * We use a resource string here instead of just DateFormat.SHORT
     * so that we get a four-digit year instead a two-digit year.
     */
    public static String numeric_date_format_default = "yyyy-MM-dd EE";
    
    /**
     * �õ�Ƭ��ȱʡͼƬĬ�ϳ��Ⱥ͵���ֵ
     */
    public static final int SLIDE_DEFAULT_SIZE = 160;
    
    /**
     * ����ɾ��ʱ�ֿ�ɾ����ÿ��500����
     */
    public static final int DELETE_BLOCK_COUNT = 500;
    
    /**
     * ��λ:dp;����58dp--Loading�߶ȣ����ڷָ���Ϊ=16.67dp
     */
    public static final double SCROLL_LOADING_HEIGHT = 74.67;
}