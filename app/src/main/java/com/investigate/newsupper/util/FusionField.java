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
	 * 锟斤拷锟芥当前锟斤拷Activity
	 */
	public static Activity currentActivity = null;
	
	public static Activity videoActivity = null;

	/**
	 * 锟斤拷锟芥当前锟斤拷context
	 */
	public static Context currentContext = null;
	
	/**
	 * 锟斤拷锟斤拷activity锟斤拷状态锟斤拷锟斤拷锟节诧拷捉HOME锟斤拷锟斤拷使锟矫筹拷锟津到猴拷台锟斤拷N锟斤拷锟接猴拷锟斤拷顺锟�
	 */
	public static AbstractMap<Activity, String> activityStatus = new ConcurrentHashMap<Activity, String>();

	/**
	 * 锟斤拷台锟剿筹拷时锟斤拷锟斤拷
	 */
	public static int iDelayShutdown = 300000;
	
	//add  by zhangkun 2011.9.20 begin
	/**
	 * launcher页锟斤拷未锟斤拷锟斤拷息锟斤拷锟斤拷
	 */
	public static int launcherUnreadMsgNum= 0;
	//add  by zhangkun 2011.9.20 end
	
	/**
	 * 圈锟斤拷未锟斤拷锟斤拷息锟斤拷锟斤拷
	 */
	public static int groupUnreadMsgNum = 0;
	
	/**
	 * 锟斤拷前锟斤拷锟斤拷锟斤拷螅ǖ锟斤拷摹锟饺︼拷印锟饺猴拷锟斤拷锟斤拷锟絁ID
	 */
	public static String currentChatWith = null;

	/**
	 * 锟斤拷前锟角凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟紸ctivity锟斤拷栈锟叫ｏ拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷页锟斤拷时锟斤拷锟斤拷0锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷妫�1锟斤拷锟节碉拷锟侥斤拷锟芥，2锟斤拷锟斤拷群锟侥斤拷锟斤拷
	 */
	public static int activityInStack = 0;
	
	/**
	 * 锟斤拷前页锟芥：0锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷妫�1锟节碉拷锟侥斤拷锟芥，2锟斤拷群锟侥斤拷锟斤拷
	 */
	public static int currentPage = 0;
	
	/**
	 * 锟斤拷锟斤拷群锟斤拷页锟斤拷亩锟斤拷螅ù吮锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷拧锟斤拷锟斤拷屑桑锟�
	 */
	public static Activity groupActivity = null;

	/**
	 * 锟斤拷锟芥当前锟斤拷锟叫活动锟斤拷activity
	 */
	public static Vector<Activity> mActivityList = new Vector<Activity>();

	/**
	 * 锟姐播锟斤拷息
	 */
	public static final String Setting_Broadcast = "Setting_Broadcast";

	/**
	 * 锟斤拷幕锟街憋拷锟斤拷
	 */
	public static float currentDensity;
	public static int currentDensityDpi;
	public static int currentWidthPixels;
	public static int currentHeightPixels;
	public static String currentscreenSize = null;

	/**
	 * 锟斤拷锟芥当前notification锟斤拷锟角革拷锟斤拷锟侥伙拷锟斤拷群锟侥碉拷ID
	 */
	public static String notificationTag = null;
	/**
	 * 锟街伙拷锟斤拷
	 */
	public static String ACCOUNT = "";

	/**
	 * 锟斤拷证锟斤拷
	 */
	public static String PASSWORD = "";

	/**
	 * 锟矫伙拷锟斤拷
	 */
	public static String NAME = "";

//	/**
//	 * 锟斤拷锟节憋拷锟斤拷锟斤拷转锟斤拷录锟斤拷list
//	 */
//	public static ArrayList<Activity> activityList = new ArrayList<Activity>();

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷应锟斤拷锟角凤拷锟节猴拷台锟斤拷锟斤拷
	 */
	public static boolean isBackgroundRun = false;
	
	
	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟矫电话锟角否公匡拷: 1锟角癸拷锟斤拷;0锟斤拷未锟斤拷锟斤拷
	 */
	public static String isOpenPhone = "-1";
	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟角否公匡拷
	 */
	public static String isOpenEmail = "-1";
	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟角凤拷锟斤拷锟斤拷
	 */
	public static String isOpenSearchable = "-1";

	/**
	 * 锟角凤拷锟角革拷锟斤拷锟斤拷片
	 */
	public static boolean isUpdateCard=false;

	/**
	 * 锟矫伙拷锟斤拷hotalk锟斤拷sid
	 */
	public static String HOTALK_SID = null;
	/**
	 * 锟斤拷锟斤拷锟斤拷锟铰凤拷锟斤拷base64 锟斤拷锟斤拷锟斤拷锟� sid=dlfjslfjssdfsdf&uid=5120123
	 */
	public static String HOTALK_BASE64_SID_UID = null;
	
	/**
	 * 锟皆斤拷Hotalk锟斤拷媒锟斤拷锟斤拷锟斤拷锟斤拷锟街わ拷锟�
	 */
	public static String HOTALK_MCODE = null;
	
	//hkw 2011.10.26  统一锟剿猴拷支锟斤拷 start
	public static String HOTALK_SERVICE_TOKEN = null;
	public static String HOTALK_BASE64_SERVICE_TOKEN = null;
	public static String accountName = null;
	public static String accountType = null;	
	//hkw 2011.10.26  统一锟剿猴拷支锟斤拷 end

	/**
	 * 锟矫伙拷锟斤拷hotalk锟斤拷jid
	 */
	public static String HOTALK_JID = null;

	/**
	 * 锟矫伙拷锟斤拷hotalk锟斤拷id
	 */
	public static String HOTALK_ID = null;
	
	/**
	 * PUSH锟斤拷锟斤拷使锟矫碉拷Device Token
	 */
	public static String DEVICE_TOKEN = null;

	public static String HOTALK_CT = "0";

	public static String HOTALK_ROSTER_VER = "";
	
	/**
	 * 锟斤拷锟斤拷VCard锟芥本锟斤拷锟斤拷锟街�
	 */
	public static String HOTALK_ROSTER_VCARD_MAX_VER = null;
	
	/**
	 * 锟斤拷取锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷目
	 */
	public static int SERVICE_AVAILABLE_MAXNUM = 200;

	/**
	 * 锟斤拷锟矫对伙拷锟斤拷陌锟斤拷锟斤拷录锟�
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
	 * 锟斤拷锟斤拷签锟斤拷锟斤拷锟斤拷蟪ざ锟�
	 */
	public static int SIGNATURE_MAX_LEN = 20;
	
	/**
	 * 锟斤拷锟芥定锟斤拷锟角的筹拷锟斤拷锟斤拷锟矫碉拷锟斤拷dialog锟斤拷id
	 */

	/**
	 * 锟斤拷录时锟侥等达拷锟斤拷 
	 */
//	public final static int DIA_LOGIN_WAIT = 0;
//	public final static int DIA_REGISTER_WAIT = 1;
	public final static int DIA_LOADINDFRIEND_WAIT = 2;
	// add by xiao. 20110823. begin
	public final static int DIA_LOADINDCHATLOG_WAIT = 3;
	public final static int DIA_ADDFRIEND_WAIT = 4;
	public final static int DIA_INITGROUPMEMBERS_WAIT = 5;
	// add by xiao. 20110823. end
	public final static int DIA_SENDING_VERIFY_WAIT = 6;//kang.chen 锟斤拷锟斤拷锟斤拷证锟斤拷锟脚的等达拷锟斤拷
	
	public final static int DIA_REGISTER_AUTO_WAIT = 7;//gaogy 锟秸碉拷锟斤拷证锟斤拷锟斤拷锟皆讹拷锟斤拷证
	
	// 锟斤拷锟斤拷锟斤拷系锟斤拷锟斤拷示
	public final static int DIA_LOADING_CONTACTS_WAIT = 8;
	
	// 锟斤拷锟斤拷群锟斤拷锟斤拷示
	public final static int DIA_CREATE_BROADCASTCHAT_WAIT = 9;
	
	public static String verifyCode;//gaogy 锟斤拷锟斤拷锟皆讹拷锟斤拷证锟斤拷锟饺匡拷锟斤拷示
	
	/**
	 * 全锟斤拷唯一锟斤拷时锟斤拷
	 */
	public static Timer timer = new Timer();
	
	/**
	 * 锟斤拷锟斤拷锟斤拷
	 */
	public static String NSP_STATUS = "NSP_STATUS";

	/**
	 * 锟斤拷锟界超时时锟斤拷
	 */
	public final static int MAX_CONNECTION_TIMEOUT = 30000;

	/**
	 * 锟斤拷始锟斤拷锟酵伙拷锟剿碉拷Secret值
	 */
	//	public static String nspSecret = "d72893b0c15aa78bf5658d89b368452c";
	public static String nspSecret = "563175cbf74b2a1f8238740682998669";
	
	//gaogy 8-23 锟斤拷锟斤拷锟斤拷志锟较达拷 start
	/**
	 * 锟斤拷锟节憋拷锟斤拷锟斤拷志锟较达拷锟斤拷系统sid
	 */
	public static String sysSid = "YuT85NwuuNZzuuCG45ouTWgm-WTAwvQgSW0w9rR7QU-4G42Z";
	
	public static String SN = "android 2.1";

	/**
	 * 锟缴癸拷锟斤拷锟斤拷锟叫憋拷
	 */
	public static String SUCCESSLIST = "successList";

	/**
	 * 失锟杰凤拷锟斤拷锟叫憋拷
	 */
	public static String FAILLIST = "failList";

	public static final String CONNECTION_AGREENMENT = "http://";

	/**
	 * 锟斤拷锟截癸拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷同时锟斤拷锟截碉拷锟斤拷锟斤拷锟斤拷
	 */
	public static int maxDownloadCount = 3;

	/**
	 * sd锟斤拷锟角凤拷锟斤拷诘谋锟街�
	 */
	public static boolean sdCardIsExist = true;

	/**
	 * 群锟斤拷息锟斤拷锟斤拷hashmap锟斤拷锟斤拷锟斤拷每锟斤拷群锟斤拷息锟斤拷锟斤拷
	 * 默锟斤拷锟斤拷锟斤拷锟轿猳pen
	 */
	public static ArrayList<String> GroupChatNotificationList = new ArrayList<String>();

	public static Activity groupChat;

	public static final int FRIEND_TYPE_NONE = 1;
	public static final int FRIEND_TYPE_FROM = 2;
	public static final int FRIEND_TYPE_TO = 3;
	public static final int FRIEND_TYPE_BOTH = 4;
	public static final int FRIEND_TYPE_REMOVE = 5;

	public static final int MAPSIZE = 280;
	//锟斤拷取锟斤拷锟斤拷图使锟矫ｏ拷锟斤拷锟斤拷图锟侥匡拷锟斤拷锟斤拷锟轿�280锟斤拷锟竭讹拷=锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�280,锟剿诧拷锟斤拷=默锟斤拷图片锟竭讹拷/默锟斤拷图片锟斤拷锟�
	public static final float MAP_HEIGHT_DIV_WIDTH = 0.5893f;
	public static final int MAPZOOM = 12;
	public static final int LATLONSIZE = 9;

	public static final String SYSTEMLANGUAGE = Locale.getDefault()
			.getLanguage();

	public static String VERSIONNAME = null;

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟截存储锟斤拷址
	 */
	public static final String UPDATE_VERSION_SAVEPATH = "/sdcard/hotalk/update/";

	/**
	 * 锟斤拷锟斤拷匹锟斤拷锟斤拷锟斤拷锟斤拷式
	 */
	public static final String EMAIL_MATCH = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*"
			+ "+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";

	public static String UPDATE_FILE_NAME = "Hotalk.apk";
	public static String UPDATE_PUSH_NAME = "Hotalk.apk";

	/**
	 * 默锟斤拷锟斤拷幕锟杰讹拷
	 */
	public static final float DEFAULT_DENSITY = 1.0f;

	/**
	 * 锟斤拷锟斤拷幕锟杰讹拷
	 */
	public static final float HIGH_DENSITY = 1.5f;
	
	/**
	 * 640 * 960锟街憋拷锟斤拷
	 */
	public static final float HIGH_BIG_DENSITY = 2.0f;
	
	/**
	 * 锟斤拷锟斤拷幕锟杰讹拷
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
	 * 锟结话锟斤拷锟斤拷锟酵计拷叨群涂锟饺碉拷锟斤拷锟斤拷
	 */
	public static int chat_Image_min_h = 145;
	public static int chat_Image_min_w = 205;
	public static int chat_Image_mh = 208;
	public static int chat_Image_mw = 157;
	/**
	 * 锟叫伙拷模式锟缴癸拷锟斤拷示锟斤拷位锟斤拷
	 */
	public static int chat_fomulat_alter_position_min = 62;
	
	public static int chat_fomulat_alter_position_max = 116;
	/**
	 * 锟结话锟斤拷锟斤拷锟酵计拷撞锟斤拷锟绞撅拷锟斤拷锟阶拷锟斤拷锟斤拷锟�
	 */
	public static int chat_Image_forward_h = 41;
	public static int chat_Image_forward_w = 87;
	
	/**
	 * 锟结话锟斤拷锟斤拷锟酵计拷锟斤拷锟斤拷锟斤拷锟斤拷锟绞斤拷锟斤拷锟�
	 * h:锟斤拷锟斤拷锟轿的革拷
	 */
	public static int chat_Image_Bubble_h = 10;
	
	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷图片锟斤拷群偷锟酵硷拷锟斤拷锟街�
	 */
	public static int chat_Slide_subject_w = 9;
	
	public static String HOTALK_VERSION = null;
	
	public static String HOTALK_FILE_SIZE = null;

	public static String HOTALK_DESCRIPTION = null;
	
	/**
	 * 锟斤拷频锟斤拷锟斤拷锟斤拷
	 */
	public static MediaPlayer mp = new MediaPlayer();
	
	/**
	 * 锟斤拷锟斤拷锟斤拷斩锟�
	 */
	public static HashMap<String, String> jidMap = new HashMap<String, String>();
	
	public static boolean isConnectionOK = false;
	
	public static boolean MaybeFirstTime = true;
	
	//gaogy 8-17锟斤拷时时锟斤拷锟斤拷10锟斤拷锟轿�30锟斤拷 start
	public static int SYSTEM_TIME_OUT = 30000;
	//gaogy 8-17锟斤拷时时锟斤拷锟斤拷10锟斤拷锟轿�30锟斤拷 end
	
	public static String SESSIONID = null;
	
	public static String SEQUNCE = "0";
	
	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷息锟角凤拷通知
	 */
	public static boolean isSingleDelayNotification = true;
	/**
	 * 群锟斤拷锟斤拷锟斤拷锟斤拷息锟角凤拷通知
	 */
	public static boolean isGroupDelayNotification = true;

	/**
	 * 锟铰斤拷锟斤拷息锟斤拷页锟斤拷锟斤拷转时携锟斤拷锟侥讹拷锟襟集猴拷
	 * wei.han
	 */


	/**
     * sms锟斤拷provider锟斤拷址
     */
    public static String SMS_PROVIDER_URI = "content://sms/";
    
    public static String MMS_PROVIDER_URI = "content://mms/";
    
    /**
     * sms锟侥的会话
     */
    public static String SMS_THREAD_URI = "content://sms/conversations";
    
    /**
     * mms-sms锟侥会话
     */
    public static String MMS_SMS_THREAD_URI = "content://mms-sms/conversations?simple=true";
    
    /**
     * mms-sms锟结话锟斤拷应锟斤拷锟街伙拷锟斤拷锟斤拷
     */
    public static String SMS_PHONE_URI = "content://mms-sms/canonical-addresses";

	// add by xiaojie.huang 2001.9.13
	public static boolean isPhoneInService = false;
	// end by xiaojie.huang 2001.9.13

	/**
	 * 锟斤拷锟揭伙拷锟斤拷锟斤拷诺锟絀D
	 */
	public static int smsLastId = 0;
	
	/**
	 * 锟斤拷锟揭伙拷锟斤拷锟斤拷锟絀D
	 */
	public static int mmsLastId = 0;
	
	/**
	 * 锟斤拷前锟结话锟斤拷应锟斤拷锟街伙拷锟斤拷锟斤拷
	 */
	public static String smsPhoneNumber = "";
	
	/**
	 * 锟斤拷志锟斤拷锟斤拷锟角凤拷锟斤拷锟斤拷锟斤拷
	 */
	public static boolean isNetworkConntected = false;
	
	/**
	 * 锟斤拷锟斤拷锟矫斤拷锟斤拷洗锟斤拷锟絛bank锟斤拷权锟侥凤拷锟截斤拷锟斤拷锟�
	 */
	public static String dbank_upload_host = null;
	public static String dbank_upload_secret = null;
	public static String dbank_upload_nsp_tstr = null;
	
	/**
	 * 锟斤拷锟斤拷锟叫讹拷锟街伙拷锟角凤拷支锟斤拷google锟酵高德碉拷图锟斤拷锟斤拷锟斤拷锟斤拷锟街碉拷图锟叫伙拷
	 */
	public static boolean isSupportAllMap = false;
	
	/**
	 * 锟斤拷录锟铰次碉拷锟斤拷锟酵硷拷锟脚ワ拷锟绞癸拷锟斤拷锟斤拷锟斤拷锟斤拷偷锟酵�
	 * 0  锟斤拷一锟轿斤拷锟斤拷锟酵�
	 * 1  使锟斤拷google锟斤拷图
	 * 2  使锟矫高德碉拷图
	 */
	public static int nextUseMap = 0;
	/**
	 * 锟角凤拷锟斤拷锟斤拷锟秸碉拷图片
	 */
	public static String isphotograph_key="imageisphotograph"; 

	public static  final String refreshQuickReplyBroadcast = "com.hotalk.ui.chat.singleChat.QuickReplyActivity";
	
	/**
	 * message锟斤拷锟斤拷Action
	 */
	public static String INTENT_ACTION_MESSAGE = "com.hotalk.server.receiver.MessageReceiver";
	
	public static boolean IsDeleteChat = false;

	/**
	 *  锟斤拷token锟斤拷效时锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟截碉拷失锟斤拷锟斤拷息为
     *  <failure xmlns="urn:hotalk:params:xml:ns:xmpp-sasl"><invalid-servicetoken/></failure> 
	 */
	public static String AUTH_INVALID_SERVICETOKEN_TAG = "invalid-servicetoken";
	
	/**
	 * 锟斤拷前锟斤拷锟斤拷apn锟角凤拷锟斤拷 wei.han
	 */
	public static NetworkInfo mmsInfo = null;
	
	/**
	 * 锟斤拷锟斤拷锟斤拷录锟街伙拷锟脚猴拷强锟斤拷 wei.han
	 */
	public static SignalStrength mSignalStrength = null;
	
	/**
	 * 锟叫讹拷锟角凤拷锟斤拷示锟斤拷锟劫回革拷锟斤拷
	 */
	public static boolean isReplyAppear = true;
	
	/**
	 * 锟斤拷锟斤拷时锟斤拷锟斤拷锟斤拷锟皆讹拷校锟斤拷锟铰硷拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�5锟轿ｏ拷锟斤拷止锟斤拷支锟斤拷锟斤拷锟叫撅拷默注锟斤拷锟斤拷没锟揭恢憋拷锟斤拷锟街�
	 * 锟斤拷锟斤拷锟斤拷每锟轿讹拷要锟斤拷示锟斤拷证
	 */
	public static final int AUTO_REGIST_MAX_TIMES = 5;
	
	/**
     * 锟矫伙拷nickname没锟斤拷锟皆讹拷锟斤拷时锟斤拷锟斤拷示锟矫伙拷锟斤拷锟斤拷nickname锟斤拷锟斤拷锟斤拷锟斤拷示锟斤拷锟斤拷锟斤拷锟�2锟斤拷
     */
    public static final int NOTIFY_SET_NICKNAME_MAX_TIMES = 2;
    
    /**
     * 锟斤拷示锟矫伙拷锟斤拷锟斤拷锟酵伙拷锟剿ｏ拷锟斤拷锟斤拷锟斤拷示锟斤拷锟斤拷锟斤拷锟�3锟斤拷
     */
    public static final int NOTIFY_UPDATE_MAX_TIMES = 3;
    
    /**
     * 锟斤拷示锟矫伙拷锟斤拷锟斤拷nickname锟斤拷锟斤拷展示
     */
    public static boolean NOTIFY_SET_NICKNAME_SHOW = false;
	
	public static boolean IMAGE_PREVIEW = true;
	
	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟叫★拷锟斤拷锟斤拷诨鼗锟斤拷斜锟斤拷锟较碉拷说冉锟斤拷锟侥猴拷锟斤拷锟斤拷锟斤拷锟斤拷
	 */
	public static float NAME_TEXT_SIZE = 18;
	
	/**
     * 锟斤拷锟斤拷锟斤拷锟斤拷锟揭伙拷锟斤拷锟较拷谋锟斤拷锟斤拷锟斤拷小锟斤拷锟斤拷锟节会话锟叫憋拷锟斤拷锟揭伙拷锟斤拷锟较⒄故撅拷锟斤拷锟较碉拷私锟斤拷锟界话锟斤拷锟斤拷锟侥憋拷锟斤拷锟斤拷锟叫★拷锟�
     */
	public static float CHAT_TEXT_SIZE = 14.5f;
	
	/**
     * 锟结话锟叫憋拷锟斤拷锟斤拷息时锟斤拷锟斤拷示锟斤拷锟斤拷锟叫�
     */
	public static float TIME_TEXT_SIZE = 11.5f;
	
	/**
     * 锟结话锟斤拷锟斤拷锟侥憋拷锟斤拷息锟斤拷锟斤拷锟叫★拷锟斤拷锟斤拷诘锟斤拷锟饺猴拷幕峄帮拷谋锟斤拷锟较拷锟斤拷锟斤拷小锟斤拷
     */
	public static float CHATBODY_TEXT_SIZE = 17;
	
	/**
     * 锟结话锟斤拷锟斤拷锟斤拷息时锟斤拷锟斤拷锟斤拷锟叫★拷锟斤拷锟斤拷诘锟斤拷锟饺猴拷幕峄帮拷锟斤拷锟斤拷锟较⑹憋拷锟斤拷锟绞撅拷锟斤拷锟斤拷小锟斤拷
     */
	public static float CHATTIME_TEXT_SIZE = 10;

//	public static float SET_NAME_TEXT_SIZE = 17.3f;
	
	/**
     * 锟斤拷锟矫斤拷锟斤拷锟斤拷锟窖★拷锟斤拷锟斤拷锟斤拷小,锟剿碉拷锟斤拷锟斤拷锟叫�
     */
	public static float SET_TYPE_TEXT_SIZE = 16;
	
	/**
     * 锟斤拷锟矫斤拷锟斤拷锟斤拷锟窖★拷锟斤拷录锟斤拷说锟斤拷锟斤拷锟斤拷锟叫�
     */
	public static float SET_SMALL_TEXT_SIZE = 12;
	
	
	public static float FAVORITES_NAME_TEXT_SIZE = 14.5f;  
	
	/**
	 * SecretKey, 锟斤拷锟节硷拷锟斤拷
	 */
	public static String SECRET_KEY = null;

	/**
	 * mCode锟斤拷 锟斤拷锟斤拷锟斤拷hotalk锟铰讹拷媒锟斤拷锟侥硷拷锟斤拷锟斤拷锟斤拷锟较达拷/锟斤拷锟斤拷锟侥硷拷锟斤拷证
	 */
	public static String MCODE = null;
	
	/**
	 * 锟斤拷锟斤拷锟铰际憋拷锟斤拷锟绞撅拷锟絫rue 锟斤拷示锟斤拷锟揭伙拷锟斤拷锟较拷锟绞憋拷锟�  false 锟斤拷示锟斤拷锟揭伙拷锟斤拷锟斤拷锟斤拷锟较拷锟绞憋拷锟�
	 */
	public static boolean changeMessageTimeDisplayMode = false;
	
	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷push agent锟芥本锟斤拷
	 */
	public static String PUSH_VERSION = null;
	/**
	 * 锟斤拷锟斤拷Push Service锟侥碉拷址
	 */
	public static String PUSH_UPDATE_URL = null;
	
	/**
	 * 锟叫讹拷锟角凤拷锟斤拷锟斤拷锟斤拷锟斤拷祝锟斤拷锟斤拷锟斤拷
	 */
	public static boolean IS_REQUEST_BLESSSMS = false;
	
    // add by jiangzhihua 2012-03-26 锟斤拷锟饺猴拷锟揭筹拷锟介看
    /**
     * 锟角凤拷锟斤拷群锟斤拷页锟斤拷锟阶次硷拷锟斤拷
     */
    public static boolean IS_FIRST_LOAD = true;

    /**
     * Http锟斤拷锟斤拷锟斤拷息锟斤拷携锟斤拷锟斤拷锟斤拷锟节凤拷锟斤拷锟斤拷锟斤拷锟斤拷证锟街凤拷锟斤拷
     */
    public static String HTTP_SECRET_KEY = null;
    
    /**
     * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷handler锟斤拷锟斤拷
     */
	public static ArrayList<Object> updateHandler = new ArrayList<Object>();
    
    /**
     * default date format
     * 默锟较碉拷时锟斤拷锟绞�
     * The setting is not set; use the default.
     * We use a resource string here instead of just DateFormat.SHORT
     * so that we get a four-digit year instead a two-digit year.
     */
    public static String numeric_date_format_default = "yyyy-MM-dd EE";
    
    /**
     * 锟矫碉拷片锟斤拷缺省图片默锟较筹拷锟饺和碉拷锟斤拷值
     */
    public static final int SLIDE_DEFAULT_SIZE = 160;
    
    /**
     * 锟斤拷锟斤拷删锟斤拷时锟街匡拷删锟斤拷锟斤拷每锟斤拷500锟斤拷锟斤拷
     */
    public static final int DELETE_BLOCK_COUNT = 500;
    
    /**
     * 锟斤拷位:dp;锟斤拷锟斤拷58dp--Loading锟竭度ｏ拷锟斤拷锟节分革拷锟斤拷为=16.67dp
     */
    public static final double SCROLL_LOADING_HEIGHT = 74.67;
}