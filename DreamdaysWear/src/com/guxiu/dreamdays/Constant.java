package com.guxiu.dreamdays;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class Constant {
	
	public static String PACKAGE_NAME ;
	public final static int KEY_FIRSTPAGE = 1;
	public final static int KEY_SECONDPAGE = 2;
	public final static int KEY_LAYOUT_ID = 3;
	public static final String ADMOD_KEY = "ca-app-pub-3987342603461697/5608234164";
	
	//限免时间
	public static final String  startTime = "2014-08-20 09:00:00";
	public static final String  endTime = "2014-08-22 17:00:00";
	
	public static SimpleDateFormat globalDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" ,Locale.ENGLISH);
	public static SimpleDateFormat appDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
	public static SimpleDateFormat appDateFormat_2 = new SimpleDateFormat("MMMM dd,yyyy", Locale.ENGLISH);
	public static SimpleDateFormat equalDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm" ,Locale.ENGLISH);
	public static SimpleDateFormat equalDateFormat_3 = new SimpleDateFormat("HH:mm" ,Locale.ENGLISH);
	public static SimpleDateFormat equalDateFormat_2 = new SimpleDateFormat("yyyy-MM-dd" ,Locale.ENGLISH);
	
	public static final String GO_GOOGLE_PLAY_BUY_DREAMDAYS = "https://play.google.com/store/apps/details?id=com.guxiu.dreamdays";
	
	public static final String DIR_DREAMDAYS = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/.dreamdays";

	/** file */
	public static final String CACHE = DIR_DREAMDAYS + "/Cache/";
	public static final String TEMP_PIC = DIR_DREAMDAYS + "/Temp/";
	public static final String DREAMDAY_PIC = DIR_DREAMDAYS + "/Pic/";
	public static final String DREAMDAY_MUSIC = DIR_DREAMDAYS + "/Music/";
	public static String DIR_DREAMDAYS_BIGPIC = DIR_DREAMDAYS + "/original/";
	
	public static String SHAREIMAGE = DIR_DREAMDAYS_BIGPIC + "DreamdaysShareImage.jpg";
	public static String SHAREDETAILIMAGE = DIR_DREAMDAYS_BIGPIC + "CutDreamdaysDetailShareImage.jpg";

	public static final String BIG_IMG_EXT = "_b";
	public static final String SMALL_IMG_EXT = "_s";
	
	/** state */
	public static final int ADDVOICE = 3; // 添加录音
	public static final int ADDCATEGORY = 4; // 添加事件
	public static final int MODIFY_MATTER = 5; // 修改事件
	public static final int REPEAT = 6;
	public static final int ANDROID = 4;
	
}
