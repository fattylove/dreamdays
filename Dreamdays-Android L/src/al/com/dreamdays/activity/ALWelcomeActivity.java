package al.com.dreamdays.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.receiver.AppAlarmReceiver;
import al.com.dreamdays.sqlite.Category;
import al.com.dreamdays.sqlite.CategoryService;
import al.com.dreamdays.sqlite.Matter;
import al.com.dreamdays.sqlite.MatterService;
import al.com.dreamdays.utils.DateUtil;
import al.com.dreamdays.utils.FileUtil;
import al.com.dreamdays.utils.ImageUtil;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */

public class ALWelcomeActivity extends BaseActivity {

	private View contentView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme();
		if(isAndroidL()){
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); 
		}
		
		// set view
		contentView = View.inflate(this, R.layout.al_dreamdays_startup_layout, null);
		setContentView(contentView);
		
		new Thread(new Runnable() {
			public void run() {
				init();
				handler.sendEmptyMessage(10010);
			}
		}).start();
		
		//限免
		if(Constant.PACKAGE_NAME.equals("com.guxiu.dreamdays")){
			getSharedPreferences("fatty", Context.MODE_PRIVATE).edit().putBoolean("isOpen", false).commit();
		}else{
			boolean isFirstStart = getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isFirstStart_", false);
			if(!isFirstStart){
				registerAppAlarmReceiverAndCheckTheDateIsSame();
				getSharedPreferences("fatty", Context.MODE_PRIVATE).edit().putBoolean("isFirstStart_", true).commit();
			}
		}
	}
	
	public void init(){
		// copy share photo
		ImageUtil.copyImageToSdrcardFromAssest(this, Constant.SHAREIMAGE , "share"+File.separator+"dreamdays.jpg");
		
		// get screen height and width
		getScreen();
		
		// create cache
		createCache(Constant.CACHE);
		createCache(Constant.TEMP_PIC);
		createCache(Constant.DREAMDAY_PIC);
		createCache(Constant.DREAMDAY_MUSIC);
		createCache(Constant.DIR_DREAMDAYS_BIGPIC);
		
		// update widget
		updateWidget(this);
		
		// data setting
		getSharedPreferences("fatty", Context.MODE_PRIVATE).edit().putBoolean("appStart", true).commit();
		
		//trip data init
		boolean isTripDataInit = getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isTripDataInit", false);
		if(!isTripDataInit){
			setTripData();
			getSharedPreferences("fatty", Context.MODE_PRIVATE).edit().putBoolean("isTripDataInit", true).commit();
		}
		
		//compress Photo
		boolean isCompressPhoto = getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isCompressPhoto", false);
		if(!isCompressPhoto){
			compressLocalImage();
			getSharedPreferences("fatty", Context.MODE_PRIVATE).edit().putBoolean("isCompressPhoto", true).commit();
		}
		
		//set coverEvent titlebar color
		boolean isTopMatterColorBar = getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isTopMatterColorBar", false);
		if(!isTopMatterColorBar){
			Matter topMatter = new MatterService().queryStickMatter(this);
			setCoverTitleBarColor(topMatter);
			getSharedPreferences("fatty", Context.MODE_PRIVATE).edit().putBoolean("isTopMatterColorBar", true).commit();
		}
	}
	
	/**
	 * where 
	 * 
	 * @param contentView
	 */
	public void whereToGo(View contentView){
		// where check
		if(!TextUtils.isEmpty(getIntent().getAction()) && getIntent().getAction().equals("Notification")){
			Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
			contentView.startAnimation(animation);
			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation arg0) {
				}
				@Override
				public void onAnimationRepeat(Animation arg0) {
				}
				@Override
				public void onAnimationEnd(Animation arg0) {
					new Handler().postDelayed(new Runnable() {
						public void run() {
							Intent intent = new Intent();
							intent.setClass(ALWelcomeActivity.this, ALHomeActivity.class);
							intent.setAction("DaBing");
							startActivity(intent);
							finish();
						}
					}, 500);
				}
			});
		}else{
			Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
			contentView.startAnimation(animation);
			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation arg0) {
				}
				@Override
				public void onAnimationRepeat(Animation arg0) {
				}
				@Override
				public void onAnimationEnd(Animation arg0) {
					new Handler().postDelayed(new Runnable() {
						public void run() {
							Intent intent = new Intent(ALWelcomeActivity.this, ALHomeActivity.class);
							startActivity(intent);
							finish();
						}
					}, 500);
				}
			});
		}
	}
	
	/**
	 * 创建缓存文件
	 * 
	 * @param name
	 */
	private void createCache(String name) {
		File file = new File(name);
		if(!file.exists()){
			file.mkdirs();
		}
	}

	/**
	 * 更新Widget
	 */
	public static void updateWidget(Context ctxt){
		AlarmManager aManager = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent();
		intent.setClass(ctxt, AppAlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(ctxt, 0, intent, 0);
		aManager.setRepeating(AlarmManager.RTC, 0, 60 * 1000, pendingIntent);
	}
	
	/**
	 * 获取屏幕分辨率
	 */
	private void getScreen(){
		DisplayMetrics displayMetrices = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrices);
		Constant.width = displayMetrices.widthPixels;
		Constant.height = displayMetrices.heightPixels;
	}
	
	/**
	 * 
	 * 增加Trip默认Category
	 * 
	 */
	public void setTripData(){
		ArrayList<Category> tempList = new ArrayList<Category>();
		ArrayList<Category> changedList = new ArrayList<Category>();
		ArrayList<Category> addCategories = new ArrayList<Category>();
		CategoryService categoryService = new CategoryService();
		MatterService matterService = new MatterService();
		ArrayList<Category> categories = categoryService.queryCategoryList(this);
		//清理Repeat
		for(Category c: categories){
			ArrayList<Matter> matters =  (ArrayList<Matter>) matterService.queryMattersByCategory(this, c.get_id());
			for(Matter matter : matters){
				if(!matter.getMatterName().equals("Christmas Day")){//判断是不是新加的事件Christams Day ， 如果是不重置repeat
					matter.setRepeat_type(0);
					matterService.insertOrUpdateNewMatter(this, matter);
				}
			}
		}
		
		//修复Trip
		for(int i = 0 ; i < categories.size() ; i++){
			Category category = categories.get(i);
			if(category.get_id() < 6){
				Category tempCategory = new Category();
				tempCategory.set_id(category.get_id());
				tempCategory.setCategoryName(category.getCategoryName());
				tempList.add(tempCategory);
			}else{
				if(category.getCategoryName().equals("Trip") && category.get_id()==6){
					
				}else{
					int categoryId = category.get_id()+ 200;
					
					ArrayList<Matter> matters =  (ArrayList<Matter>) matterService.queryMattersByCategory(this, category.get_id());
					for(Matter matter : matters){
						matter.setClassifyType(categoryId);
						matter.setRepeat_type(0);
						matterService.insertOrUpdateNewMatter(this, matter);
					}
					
					Category changedCategory = new Category();
					changedCategory.set_id(categoryId);
					changedCategory.setCategoryName(category.getCategoryName());
					changedList.add(changedCategory);
				}
			} 
		}
		addCategories.addAll(tempList);
		Category tripCategory = new Category();
		tripCategory.set_id(6);
		tripCategory.setCategoryName("Trip");
		addCategories.add(tripCategory);
		addCategories.addAll(changedList);
		categoryService.deleteCategory(this);
		categoryService.replaceCategory(this , addCategories);
		tempList.clear();
		changedList.clear();
		addCategories.clear();
	}
	
	/**
	 * 压缩本地图片
	 * compress cache image
	 */
	public void compressLocalImage(){
		ArrayList<Matter> matters = (ArrayList<Matter>) new MatterService().queryAllMatter(this);
		for(Matter matter : matters){
			if(!TextUtils.isEmpty(matter.getPicName()) && FileUtil.checkFileExists(matter.getPicName())){
				Bitmap bitmap = ImageUtil.loadSdcardDrawable(new File(matter.getPicName()), Constant.width, Constant.height);
				bitmap = ThumbnailUtils.extractThumbnail(bitmap, Constant.width , Constant.height);
				ImageUtil.saveBitmap(matter.getPicName(), bitmap);
				if(bitmap!=null && !bitmap.isRecycled()){
					bitmap.recycle();
					bitmap = null;
				}
			}
		}
	}
	
	/**
	 * set cover title bar color
	 */
	public void setCoverTitleBarColor(Matter topMatter){
		float[] colors = new float[3];
		if(topMatter!=null){
			String bgImagePath = topMatter.getPicName();
			if (!TextUtils.isEmpty(bgImagePath) && FileUtil.checkFileExists(bgImagePath)) {
				File file = new File(bgImagePath);
				Bitmap topBitmap = ImageUtil.loadSdcardDrawable(file , Constant.width , Constant.height/2);
				colors = ImageUtil.getAverageColor2(topBitmap);
			} else {
				int id = topMatter.getClassifyType();
				int iconRes = Constant.top_bgs[6];
				if (id < 7) {
					iconRes = Constant.top_bgs[id-1];
				}
				Bitmap bitmap = ImageUtil.loadResourceDrawable(this, iconRes);
				colors = ImageUtil.getAverageColor2(bitmap);
			}
			getSharedPreferences("color", Context.MODE_PRIVATE).edit().putFloat("color_h", colors[0]).commit();
			getSharedPreferences("color", Context.MODE_PRIVATE).edit().putFloat("color_s", colors[1]).commit();
			getSharedPreferences("color", Context.MODE_PRIVATE).edit().putFloat("color_v", colors[2]).commit();
		}
	}
	
	/**
	 * Handler 回调
	 */
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 10010:
				whereToGo(contentView);
				break;
			}
		}
	};
	
	/**
	 * 限免
	 */
	private String startTime = Constant.startTime;
	private String endTime = Constant.endTime;
	private SimpleDateFormat dateFormat = Constant.globalDateFormat;
	public void registerAppAlarmReceiverAndCheckTheDateIsSame(){
		Date curDate = DateUtil.stringToDate(DateUtil.getCurrentGTime(dateFormat), dateFormat);
		Date starDate = DateUtil.stringToDate(startTime, dateFormat);
		Date endDate = DateUtil.stringToDate(endTime, dateFormat);
		
		if(curDate.after(starDate) && curDate.before(endDate)){ // 在时间内
			getSharedPreferences("fatty", Context.MODE_PRIVATE).edit().putBoolean("isOpen", false).commit();
		}else{                                                  //不在时间内
			getSharedPreferences("fatty", Context.MODE_PRIVATE).edit().putBoolean("isOpen", true).commit();
		}
	}
}
