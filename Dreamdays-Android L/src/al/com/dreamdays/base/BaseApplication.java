package al.com.dreamdays.base;

import al.com.dreamdays.exception.CrashHandler;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;

import com.guxiu.dreamdays.R;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.AnalyticsConfig;

/**
 * 
 * @author Fatty
 *
 */
public class BaseApplication extends Application {

	
	public static Typeface typeface_heavy;//粗
	public static Typeface typeface_light;//细
	public static Typeface typeface_medium;//中等的
	public static Typeface typeface_roman;//斜体
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			setTheme(R.style.AndroidLAppBaseTheme);
		}else{
			setTheme(R.style.AppBaseTheme);
		}
		
		// CrashHandler
		CrashHandler crashHandler = CrashHandler.getInstance();  
        crashHandler.init(getApplicationContext());  
		
		// ImageLoader config
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
		        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
		        .diskCacheExtraOptions(480, 800, null)
		        .threadPoolSize(3) // default
		        .threadPriority(Thread.NORM_PRIORITY - 2) // default
		        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
		        .denyCacheImageMultipleSizesInMemory()
		        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
		        .memoryCacheSize(2 * 1024 * 1024)
		        .memoryCacheSizePercentage(13) // default
		        .diskCacheSize(50 * 1024 * 1024)
		        .diskCacheFileCount(100)
		        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
		        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
		        .writeDebugLogs()
		        .build();
		ImageLoader.getInstance().init(config);
		
		// set fonts
		setFont();
		
		// check pagename
		if(getPackageName().equals("com.guxiu.dreamdays")){
			Constant.isLite(false);
			getSharedPreferences("fatty", Context.MODE_PRIVATE).edit().putBoolean("isLite", false).commit();
			AnalyticsConfig.setAppkey("53015d5656240b0437251d3a");
		}else{
			Constant.isLite(true);
			getSharedPreferences("fatty", Context.MODE_PRIVATE).edit().putBoolean("isLite", true).commit();
			AnalyticsConfig.setAppkey("532696f256240bcdd2246d66");
		}
		AnalyticsConfig.setChannel("GOOGLE MARKET");
	}

	/**
	 * 设置字体
	 */
	public void setFont(){
		typeface_heavy = Typeface.createFromAsset(getAssets(), "fonts/avenir_heavy.ttf" );
		typeface_light = Typeface.createFromAsset(getAssets(), "fonts/avenir_light.ttf" );
		typeface_medium = Typeface.createFromAsset(getAssets(), "fonts/avenir_medium.ttf" );
		typeface_roman = Typeface.createFromAsset(getAssets(), "fonts/avenir_roman.ttf" );
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	
	
}
