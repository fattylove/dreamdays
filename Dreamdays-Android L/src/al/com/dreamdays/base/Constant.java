package al.com.dreamdays.base;

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
	
	public static final int[] detail_center_bgs = new int[]{
		R.drawable.detail_center_1,
		R.drawable.detail_center_2,
		R.drawable.detail_center_3,
		R.drawable.detail_center_4,
		R.drawable.detail_center_5,
		R.drawable.detail_center_6,
		R.drawable.detail_center_7
	};
	
	public static final int[] detail_bgs = new int[]{
		R.drawable.detail_bg_1,
		R.drawable.detail_bg_2,
		R.drawable.detail_bg_3,
		R.drawable.detail_bg_4,
		R.drawable.detail_bg_5,
		R.drawable.detail_bg_6,
		R.drawable.detail_bg_7
	};
	
	public static final int[] top_bgs = new int[]{
		R.drawable.top_bg_1,
		R.drawable.top_bg_2,
		R.drawable.top_bg_3,
		R.drawable.top_bg_4,
		R.drawable.top_bg_5,
		R.drawable.top_bg_6,
		R.drawable.top_bg_7
	};
	
	public static final int[] event_black_icons = new int[]{
		R.drawable.category_icon0,
		R.drawable.category_icon2,
		R.drawable.category_icon3,
		R.drawable.category_icon4,
		R.drawable.category_icon5,
		R.drawable.category_icon6,
		R.drawable.category_icon7
	};
	
	public static final int[] event_black_red_icons = new int[]{
		R.drawable.category_icon1,
		R.drawable.category_icon2,
		R.drawable.category_icon3,
		R.drawable.category_icon4,
		R.drawable.category_icon5,
		R.drawable.category_icon6,
		R.drawable.category_icon7
	};
	
	public static final int[] event_white_icons = new int[]{
		R.drawable.category_white_icon1,
		R.drawable.category_white_icon2,
		R.drawable.category_white_icon3,
		R.drawable.category_white_icon4,
		R.drawable.category_white_icon5,
		R.drawable.category_white_icon6,
		R.drawable.category_white_icon7
	};
	
	/**facebook**/
	public static String FACEBOOK_APP_ID ;
	
	/**twitter**/
	public static String TWITTER_OAuthConsumerKey ;
	public static String TWITTER_OAuthConsumerSecret ;
	public static String TWITTER_OAuthAccessTokenSecret ;
	public static String TWITTER_OAuthAccessToken;
	
	/**
	 * pagename
	 * 
	 * facebook 
	 * 
	 * twitter
	 * 
	 * @param isLite
	 */
	public static void isLite(boolean isLite){
		if(isLite){
			PACKAGE_NAME = "com.guxiu.dreamdays_l";
			FACEBOOK_APP_ID = "1458425121085339";
			TWITTER_OAuthConsumerKey = "N2Uma11PcCYZCRVGFiKKAzuTj";
			TWITTER_OAuthConsumerSecret = "AFhWjRT3vvGSpLNvXKgUMMtPL1hd1f4lUz8F4k7zNIRPRRlOmc";
			TWITTER_OAuthAccessTokenSecret = "A96i4OnxvjMy6WQagfQA8OJ9vTsFqlUN0Lqc6EBEe4yPO";
			TWITTER_OAuthAccessToken = "1052093226-PXb2bqDNdqcA1jkOB5HPhCbvR83C40LhjduQUUn";
		}else{
			PACKAGE_NAME = "com.guxiu.dreamdays";
			FACEBOOK_APP_ID = "336407913190255";
			TWITTER_OAuthConsumerKey = "VN8ja11bYucFRpBsZd7eJFL2m";
			TWITTER_OAuthConsumerSecret = "rbAVf9MK8WosCA501VVqt9z1YSJnM65EPTxzI5oZKBqW5aFl6H";
			TWITTER_OAuthAccessTokenSecret = "ISiknuxrnolwYK0Q9NBEBZwXEAI6tKd3Gy3DQZwYZ8oQJ";
			TWITTER_OAuthAccessToken = "1052093226-e6G2QrX4Bv0noePrwTJX9jrDt1LleepK20wd1AT";
		}
	}
	
	// 屏幕宽度
	public static int width = 0;
	// 屏幕高度
	public static int height = 0;
	
	// color keys
	public static long RGBcolors[][] = new long[][]{
			{244 , 67 , 54},
			{233 , 30 , 99},
			{156 , 39 , 176},
			{103 , 58 , 183},
			{63 , 81 , 181},
			{33 , 150 , 243},
			{0 , 188 , 212},
			{0 , 150 , 136},
			{139 , 195 , 74},
			{205 , 220 , 57},
			{255 , 235 , 59},
			{255 , 152 , 0},
			{255 , 84 , 34},
			{121 , 85 , 72},
			{158 , 158 , 158},
			{96 , 125 , 139}
	};
	
	// color keys
	public static float hsvColors[][] = new float[][]{
		{340.0F , 87.0F , 91.0F},
		{291.0F ,78.0F ,69.0F},
		{262.0F , 68.0F , 72.0F},
		{231.0F ,65.0F ,71.0F},
		{207.0F , 86.0F , 95.0F},
		{199.0F , 99.0F , 96.0F},
		{197.0F , 100.0F , 83.0F},
		{174.0F , 100.0F , 59.0F},
		{122.0F , 57.0F , 69.0F},
		{88.0F , 62.0F , 76.0F},
		{66.0F , 74.0F , 86.0F},
		{54.0F , 77.0F , 100.0F},
		{45.0F , 97.0F , 100.0F},
		{36.0F , 100.0F , 100.0F},
		{14.0F  ,87.0F ,100.0F},
		{16.0F , 41.0F , 47.0F},
		{4.0F , 78.0F , 96.0F},
	};
	
	public static float hsvGray[][] = new float[][] {
		{0.0F ,0.0F ,62.0F},//灰
		{0.0F  ,0.0F ,98.0F},//白
		{0.0F  ,0.0F ,13.0F} //黑
	};
	
	// { 12 , 66, 234}
}
