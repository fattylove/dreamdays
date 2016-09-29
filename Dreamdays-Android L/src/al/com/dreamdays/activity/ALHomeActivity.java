package al.com.dreamdays.activity;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import al.com.dreamdays.activity.lock.ALPasscodeGoHomeActivity;
import al.com.dreamdays.adapter.HomeEventAdapter;
import al.com.dreamdays.adapter.HomePopoAdapter;
import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.base.LINKS;
import al.com.dreamdays.sqlite.Category;
import al.com.dreamdays.sqlite.CategoryService;
import al.com.dreamdays.sqlite.Matter;
import al.com.dreamdays.sqlite.MatterService;
import al.com.dreamdays.utils.Base64Util;
import al.com.dreamdays.utils.DateUtil;
import al.com.dreamdays.utils.FileUtil;
import al.com.dreamdays.utils.ImageUtil;
import al.com.dreamdays.utils.event.LeftEventCompare;
import al.com.dreamdays.utils.event.SinceEventCompare;
import al.com.dreamdays.view.UILinearLayout;
import al.com.dreamdays.view.UILinearLayout.OnGiveUpTouchEventListener;
import al.com.dreamdays.view.UILinearLayout.OnUILayoutScrollListener;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.api.alertview.AlertBuilder;
import com.api.alertview.Effectstype;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.guxiu.dreamdays.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import fatty.library.http.core.CallBack;
import fatty.library.http.core.HttpService;
import fatty.library.http.core.Parameters;

/**
 * 
 * @author Fatty
 *
 */
@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class ALHomeActivity extends BaseActivity implements OnGiveUpTouchEventListener , OnClickListener{

	//open 
	protected boolean isLite;
	private String[] menus = new String[]{"Edit","Settings"};
	
	//popo position
	private int choicePosition ;
	private int categoryId ;
	
	// data
	private ListView allEventListView;
	private HomeEventAdapter adapter;
	
	private Bitmap topBitmap;
	private Matter topMatter;
	private ArrayList<Matter> matters = new ArrayList<Matter>();
	private MatterService matterService;
	
	//title
	private static RelativeLayout homeTitleLayout;
	private RelativeLayout eventBarLayout;
	private TextView eventBarTextView;
	private ImageButton plusImageButton;
	private ImageButton menuImageButton;
	
	//noEvent
	private LinearLayout noEventsLayout;
	private Button createEventBtn;
	//content
	private RelativeLayout eventTopLayout;
	private LinearLayout nothingLayout;
	
	private UILinearLayout uiLayout;
	
	private LinearLayout topLayout;
	private ImageView topPointImageView;
	private ImageView bottomPointImageView;
	private TextView topDaysTextView;
	private ImageView topBgImageView;
	private ImageView centerImageView;
	private TextView topNameTextView;
	private TextView deleteTextView;
	private TextView centerLine1 ,centerLine2 ;
	
	private View coverContentView;
	
	//Notify
	private RelativeLayout topNotifyLayout;
	private ViewPager viewPager;
	private LinearLayout viewGroup;
	
	//is delete
	private boolean isDelete = false ;
	
	// change title color
	private static TextView notifyBarTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme();
		setContentView(R.layout.al_dreamdays_home_layout);
		hiddenView(findViewById(R.id.notifyBarTextView));
		
		//passcode settings
		passcodeSetting();
		//
		menus= new String[]{getString(R.string.edit), getString(R.string.settings)};
		//
		matterService = new MatterService();
		
		allEventListView = (ListView) this.findViewById(R.id.allEventListView);
		adapter = new HomeEventAdapter(this);
		
		//title view
		notifyBarTextView = (TextView)this.findViewById(R.id.notifyBarTextView);
		homeTitleLayout = (RelativeLayout)this.findViewById(R.id.homeTitleLayout);
		
		eventBarLayout = (RelativeLayout)this.findViewById(R.id.eventBarLayout);
		eventBarTextView = (TextView)this.findViewById(R.id.eventBarTextView);
		eventBarTextView.setTypeface(BaseApplication.typeface_heavy);
		plusImageButton = (ImageButton)this.findViewById(R.id.plusImageButton);
		menuImageButton = (ImageButton)this.findViewById(R.id.menuImageButton);
		
		eventBarLayout.setOnClickListener(this);
		plusImageButton.setOnClickListener(this);
		menuImageButton.setOnClickListener(this);
		
		//top content view
		uiLayout = (UILinearLayout)findViewById(R.id.uiLayout);
		uiLayout.setOnGiveUpTouchEventListener(this);
		uiLayout.setOnUILayoutScrollListener(new OnUILayoutScrollListener() {
			public void scroll(int y) {
			}
		});

		noEventsLayout = (LinearLayout)this.findViewById(R.id.noEventsLayout);
		createEventBtn = (Button)this.findViewById(R.id.createEventBtn);
		createEventBtn.setOnClickListener(this);
		
		topLayout = (LinearLayout)this.findViewById(R.id.topLayout);
		
		initTopView();
		
		boolean isLite = getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isLite", true);
		boolean isOpen = getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isOpen", true);
		
		if(isLite)
			if(isOpen)
				addAdmod();
		
		refreshNotification();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//set data
		topMatter = matterService.queryStickMatter(this);
		matters = (ArrayList<Matter>) matterService.queryAllMatterExitStickMatter(this);
		
		if(topMatter == null){
			homeTitleLayout.setBackgroundColor(Color.parseColor("#607d8b"));
			eventBarLayout.setVisibility(View.GONE);
			nothingLayout.setVisibility(View.VISIBLE);
			eventTopLayout.setVisibility(View.INVISIBLE);
			topLayout.setOnClickListener(null);
		}else{
			homeTitleLayout.setBackgroundColor(Color.parseColor("#00BCD4"));
			eventBarLayout.setVisibility(View.VISIBLE);
			nothingLayout.setVisibility(View.GONE);
			eventTopLayout.setVisibility(View.VISIBLE);
			topLayout.setOnClickListener(this);
		}
		
		refreshListView(categoryId , topMatter);
		
		backup();
		
		if(adView!=null){
			adView.resume();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(adView!=null){
			adView.pause();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(topBitmap!=null && !topBitmap.isRecycled()){
			topBitmap.recycle();
			topBitmap = null;
		}
		
		if(adView!=null){
			adView.destroy();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.eventBarLayout:
			cancelDelelteStyle();
			showCategoryPopwindow();
			break;
		case R.id.menuImageButton:
			cancelDelelteStyle();
			showSettingPopwindow() ;
			break;
		case R.id.createEventBtn:
		case R.id.plusImageButton: 
			cancelDelelteStyle();
			Intent addIntent = new Intent();
			addIntent.putExtra("isCreate", true);
			addIntent.setClass(this, ALAddEditEventActivity.class);
			ALHomeActivity.this.startActivity(addIntent);
			break;
		case R.id.topLayout:
			cancelDelelteStyle();
			Intent detailIntent = new Intent();
			detailIntent.setClass(this, ALDetailActivity.class);
			detailIntent.putExtra("matter_id", topMatter.get_id());
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
				startActivity(detailIntent, ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight()).toBundle());
			}else{
				startActivity(detailIntent);
			}
			break;
		case R.id.nothingLayout:
			cancelDelelteStyle();
			Intent addMainIntent = new Intent();
			addMainIntent.putExtra("isCreate", true);
			addMainIntent.putExtra("isCorver", true);
			addMainIntent.setClass(this, ALAddEditEventActivity.class);
			ALHomeActivity.this.startActivity(addMainIntent);
			break;
		default:
			break;
		}
	}
	
	private ImageView yuanbingImageView;
	private TextView heheTextView;
	private TextView  xixiTextView;
	/**
	 * 初始化TopView
	 * 
	 * @param stickMatter
	 */
	public void initTopView(){
		coverContentView = LayoutInflater.from(this).inflate(R.layout.al_dreamdays_home_top_layout, null);
		eventTopLayout = (RelativeLayout)coverContentView.findViewById(R.id.eventTopLayout);
		nothingLayout = (LinearLayout)coverContentView.findViewById(R.id.nothingLayout);
		heheTextView = (TextView)coverContentView.findViewById(R.id.heheTextView);
		xixiTextView = (TextView)coverContentView.findViewById(R.id.xixiTextView);
		heheTextView.setTypeface(BaseApplication.typeface_heavy);
		xixiTextView.setTypeface(BaseApplication.typeface_heavy);
		nothingLayout.setOnClickListener(this);
		
		yuanbingImageView = (ImageView)coverContentView.findViewById(R.id.yuanbingImageView);
		
		topDaysTextView = (TextView)coverContentView.findViewById(R.id.topDaysTextView);
		topPointImageView = (ImageView)coverContentView.findViewById(R.id.topPointImageView);
		bottomPointImageView = (ImageView)coverContentView.findViewById(R.id.bottomPointImageView);
		topBgImageView = (ImageView)coverContentView.findViewById(R.id.topBgImageView);
		topNameTextView = (TextView)coverContentView.findViewById(R.id.topNameTextView);
		centerImageView  = (ImageView)coverContentView.findViewById(R.id.centerImageView);
		deleteTextView = (TextView)coverContentView.findViewById(R.id.deleteTextView);
		deleteTextView.setTypeface(BaseApplication.typeface_medium);
		
		centerLine1 = (TextView)coverContentView.findViewById(R.id.centerLine1);
		centerLine2 = (TextView)coverContentView.findViewById(R.id.centerLine2);
		
		topDaysTextView.setTypeface(BaseApplication.typeface_heavy);
		topNameTextView.setTypeface(BaseApplication.typeface_heavy);
	
		topLayout.addView(coverContentView);
	}
	
	/**
	 * 初始化cover event data
	 * 
	 * @param stickMatter
	 */
	public void setCoverData(Matter stickMatter){
		
		setTitleBarColor();
		
		if (stickMatter != null ) {
			//set event name
			yuanbingImageView.setVisibility(View.VISIBLE);
			topNameTextView.setText(stickMatter.getMatterName());
			
			//set event icon
			ALHomeActivity.setWhiteIcon(centerImageView, this, stickMatter.getClassifyType());
			
			String bgImagePath = stickMatter.getPicName();
			if (!TextUtils.isEmpty(bgImagePath) && FileUtil.checkFileExists(bgImagePath)) {
				File file = new File(bgImagePath);
				topBitmap = ImageUtil.loadSdcardDrawable(file , Constant.width , Constant.height/2);
				topBgImageView.setImageBitmap(topBitmap);
			} else {
				setCover(topBgImageView, this, stickMatter.getClassifyType());
			}
			
			//set event date
			try {
				long MatterDay = DateUtil.getMatterDay(Constant.appDateFormat.parse(stickMatter.getMatterTime()));
				if (MatterDay > 0) {
					topDaysTextView.setText(" " + MatterDay + " ");
					bottomPointImageView.setVisibility(View.VISIBLE);
					topPointImageView.setVisibility(View.GONE);
				} else {
					MatterDay = Math.abs(MatterDay);
					topDaysTextView.setText(" " + MatterDay + " ");
					bottomPointImageView.setVisibility(View.GONE);
					topPointImageView.setVisibility(View.VISIBLE);
				}
				
				int w = ImageUtil.dip2px(this, 55.0f);
				int h = ImageUtil.dip2px(this, 2.0f);
				int l = (MatterDay + "").length();
				switch (l) {
				case 1:
					w = ImageUtil.dip2px(this, 35.0f);
					topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 85.0f);
					break;
				case 2:
					w = ImageUtil.dip2px(this, 45.0f);
					topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 85.0f);
					break;
				case 3:
					w = ImageUtil.dip2px(this, 65.0f);
					topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 85.0f);
					break;
				case 4:
					w = ImageUtil.dip2px(this, 85.0f);
					topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 75.0f);
					break;
				case 5:
					w = ImageUtil.dip2px(this, 105.0f);
					topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 75.0f);
					break;
				case 6:
					w = ImageUtil.dip2px(this, 120.0f);
					topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 75.0f);
					break;
				case 7:
					w = ImageUtil.dip2px(this, 145.0f);
					topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 75.0f);
					break;
				default:
					w = ImageUtil.dip2px(this, 145.0f);
					topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 75.0f);
					break;
				}
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w,h);
				LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(w,h);
				centerLine1.setLayoutParams(params);
				centerLine2.setLayoutParams(params2);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			yuanbingImageView.setVisibility(View.GONE);
			homeTitleLayout.setBackgroundColor(Color.parseColor("#607d8b"));
			eventBarLayout.setVisibility(View.GONE);
			nothingLayout.setVisibility(View.VISIBLE);
			eventTopLayout.setVisibility(View.INVISIBLE);
			topLayout.setOnClickListener(null);
			
			homeTitleLayout.setBackgroundColor(Color.parseColor("#607d8b"));
			notifyBarTextView.setBackgroundColor(Color.parseColor("#607d8b"));
		}
	}

	/**
	 * 删除
	 */
	public void deleteByListView(){
		refreshListView(categoryId , topMatter);
	}
	
	/**
	 * 刷新主页列表
	 * 
	 * @param categoryId
	 * @param stickMatter
	 */
	private void refreshListView(final int categoryId , Matter stickMatter) {
		//cover event
		setCoverData(stickMatter);
		
		deleteTextView.setVisibility(View.GONE);
		adapter.clear();
		// 排序
		SharedPreferences ifSortByTime = getSharedPreferences("IF_SORTBYTIME",	0);
		if (ifSortByTime.getBoolean("ifSortByTime", true)) {
			matters = (ArrayList<Matter>) matterService.queryMatterByCategory(this, categoryId);
			ArrayList<Matter> leftList = new ArrayList<Matter>();
			ArrayList<Matter> sinceList = new ArrayList<Matter>();
			ArrayList<Matter> todayList = new ArrayList<Matter>();
			for (Matter currentMatter : matters) {
				try {
					long MatterDay = DateUtil.getMatterDay(Constant.appDateFormat.parse(currentMatter.getMatterTime()));
					if (MatterDay == 0) {
						todayList.add(currentMatter);
					} else if (MatterDay > 0) {
						leftList.add(currentMatter);
					} else {
						sinceList.add(currentMatter);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			matters.clear();
			matters.addAll(todayList);

			LeftEventCompare leftCom = new LeftEventCompare();
			Collections.sort(leftList, leftCom);
			for (Matter matter : leftList) {
				matters.add(matter);
			}
			leftList.clear();
			SinceEventCompare sinceCom = new SinceEventCompare();
			Collections.sort(sinceList, sinceCom);
			for (Matter matter : sinceList) {
				matters.add(matter);
			}
			sinceList.clear();
		} else {
			matters = (ArrayList<Matter>) matterService.queryMatterByCategory(this, categoryId);
		}
		
		adapter.addMatters(matters);
		allEventListView.setAdapter(adapter);
		
		//进入详情
		allEventListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Matter matter = (Matter) allEventListView.getItemAtPosition(position);
				Intent detailIntent = new Intent();
				detailIntent.setClass(ALHomeActivity.this, ALDetailActivity.class);
				detailIntent.putExtra("matter_id", matter.get_id());
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
					startActivity(detailIntent, ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight()).toBundle());
				}else{
					startActivity(detailIntent);
				}
			}
		});
		
		//长按删除
		allEventListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Matter matter  = (Matter) allEventListView.getItemAtPosition(position);
				showRemoveEventDialog(ALHomeActivity.this,matter.get_id());
				return true;
			}
		});
		
		//cover event长按删除
		topLayout.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				showRemoveEventDialog(ALHomeActivity.this, topMatter.get_id());
				return true;
			}
		});
		
		refreshTopView();
		
		refreshEventView();
	}
	
	/**
	 * 分类列表Popwindow
	 * 
	 * category popo
	 */
	private void showCategoryPopwindow() {
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View contentView = mLayoutInflater.inflate(R.layout.al_dreamdays_home_popo_listview_layout, null);
		final ListView mPopListView = (ListView) contentView.findViewById(R.id.popoListView);
		ArrayList<Category> categories = new ArrayList<Category>();
		
		CategoryService categoryService = new CategoryService();
		
		Category allCategory = new Category();
		allCategory.set_id(0);
		allCategory.setCategoryName(getString(R.string.all_events));
		categories.add(allCategory);
		
		ArrayList<Category> sqlCategories = categoryService.queryHavedCategory(this);
		categories.addAll(sqlCategories);
		
		HomePopoAdapter homePopoAdapter = new HomePopoAdapter(this, categories, choicePosition);
		mPopListView.setAdapter(homePopoAdapter);
		
		final PopupWindow mPopupWindow = new PopupWindow(contentView, ImageUtil.dip2px(this, 190), ImageUtil.dip2px(this, 400));
		mPopupWindow.setAnimationStyle(R.style.popwindow_show_categary_list);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setFocusable(true);
		mPopupWindow.update();
		mPopupWindow.showAsDropDown((View)findViewById(R.id.popoPointView), 0, 0);
		mPopListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Category category = (Category) mPopListView.getItemAtPosition(position);
				eventBarTextView.setText(category.getCategoryName());
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
				choicePosition = position;
				if (choicePosition == 0) {
					categoryId = 0;
				} else {
					categoryId = category.get_id();
				}
				refreshListView(categoryId , topMatter);
			}
		});
	}
	
	/**
	 * 设置Popwindow
	 * 
	 * menu popo
	 */
	private void showSettingPopwindow() {
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View contentView = mLayoutInflater.inflate(R.layout.al_dreamdays_home_popo_listview_layout, null);
		final ListView mPopListView = (ListView) contentView.findViewById(R.id.popoListView);
		mPopListView.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView = LayoutInflater.from(ALHomeActivity.this).inflate(R.layout.al_dreamdays_home_popo_item, null);
				TextView groupItemTextView = (TextView) convertView.findViewById(R.id.tv_group_item);
				groupItemTextView.setText(menus[position]);
				groupItemTextView.setTextColor(Color.parseColor("#607d8b"));
				groupItemTextView.setTypeface(BaseApplication.typeface_medium);
				return convertView;
			}
			public long getItemId(int position) {
				return position;
			}
			public Object getItem(int position) {
				return menus[position];
			}
			public int getCount() {
				return menus.length;
			}
		});
		final PopupWindow mPopupWindow = new PopupWindow(contentView, ImageUtil.dip2px(this, 150), LinearLayout.LayoutParams.WRAP_CONTENT);
		mPopupWindow.setAnimationStyle(R.style.popwindow_show_categary_list);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setFocusable(true);
		mPopupWindow.update();
		mPopupWindow.showAsDropDown((View)findViewById(R.id.menuPopoPointView));
		mPopListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
				switch (position) {
				case 0:
					if(isDelete){
						isDelete = false;
						setDeleteStyle(isDelete);
					}else{
						isDelete = true;
						setDeleteStyle(isDelete);
					}
					
					break;
				case 1:
					Intent goSettingIntent = new Intent();
					goSettingIntent.setClass(ALHomeActivity.this, ALSettingActivity.class);
					ALHomeActivity.this.startActivity(goSettingIntent);
					break;
				default:
					break;
				}
			}
		});
	}
	
	/**
	 * 设置delete模式
	 * 
	 * @param isDelete
	 */
	private void setDeleteStyle(boolean isDelete){
		if(isDelete){
			ArrayList<Matter> deleteMatters = new ArrayList<Matter>();
			if(!matters.isEmpty()){
				for(Matter matter : matters){
					matter.setDeleteType(true);
					deleteMatters.add(matter);
				}
				adapter.clear();
				adapter.addMatters(deleteMatters);
			}
			if(topMatter!=null){
				deleteTextView.setVisibility(View.VISIBLE);
				deleteTextView.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
//						matterService.deleteMatterById(ALHomeActivity.this, topMatter.get_id());
//						refreshListView(categoryId, null);
						showRemoveEventDialog(ALHomeActivity.this , topMatter.get_id());
					}
				});
			}else{
				deleteTextView.setVisibility(View.INVISIBLE);
			}
		}else{
			refreshListView(categoryId, topMatter);
		}
	}
	
	/**
	 * 取消删除模式
	 */
	private void cancelDelelteStyle(){
		if(isDelete){
			isDelete=false;
			setDeleteStyle(false);
		}
	}
	
	@Override
	public boolean giveUpTouchEvent(MotionEvent event) {
		if (allEventListView.getFirstVisiblePosition() == 0) {
            return true;
        }
		return false;
	}

	/**
	 * 检测passcode设置
	 */
	private void passcodeSetting() {
		isLite = getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isLite", true);
		if(!isLite){
			SharedPreferences passCodeInfo = getSharedPreferences("PASSCODE_INFO", 0);
			if(passCodeInfo.contains("passcode") && passCodeInfo.contains("passcode_one") && passCodeInfo.contains("passcode_two")){ 
				if(!TextUtils.isEmpty(passCodeInfo.getString("passcode", "")) && !TextUtils.isEmpty(passCodeInfo.getString("passcode_two", "")) && !TextUtils.isEmpty(passCodeInfo.getString("passcode_one", ""))){
					Intent intent = new Intent(this, ALPasscodeGoHomeActivity.class);
					startActivity(intent);
				}
			}
		}else{
			boolean isOpen = getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isOpen", true);
			if(!isOpen){
				SharedPreferences passCodeInfo = getSharedPreferences("PASSCODE_INFO", 0);
				if(passCodeInfo.contains("passcode") && passCodeInfo.contains("passcode_one") && passCodeInfo.contains("passcode_two")){ 
					if(!TextUtils.isEmpty(passCodeInfo.getString("passcode", "")) && !TextUtils.isEmpty(passCodeInfo.getString("passcode_two", "")) && !TextUtils.isEmpty(passCodeInfo.getString("passcode_one", ""))){
						Intent intent = new Intent(this, ALPasscodeGoHomeActivity.class);
						startActivity(intent);
					}
				}
			}
		}
	}

	/**
	 * 备份
	 */
	private void backup(){
		boolean isLogin = this.getSharedPreferences("Login", Context.MODE_PRIVATE).getBoolean("isLogin", false);
		if(isLogin){
			String backupTime = getSharedPreferences("fatty_time", Context.MODE_PRIVATE).getString("backupTime", "");
			if(TextUtils.isEmpty(backupTime)){
				new Handler().postDelayed(new Runnable() {
					public void run() {
						backupEvents();
					}
				}, 3000);
			}else{
				Date nowTime = new Date(System.currentTimeMillis());
				if(!TextUtils.equals(backupTime, Constant.appDateFormat.format(nowTime))){
					new Handler().postDelayed(new Runnable() {
						public void run() {
							backupEvents();
						}
					}, 3000);
				}
			}
		}
	}
	
	/**
	 * 备份数据
	 */
	private void backupEvents() {
		String passport = getSharedPreferences("Login", Context.MODE_PRIVATE).getString("passport", "");
		boolean  isLogin = getSharedPreferences("Login", Context.MODE_PRIVATE).getBoolean("isLogin", false);
		
		ArrayList<Matter> matters = (ArrayList<Matter>) matterService.queryAllMatter(this);
		CategoryService service = new CategoryService();
		
		if(matters.size() > 0 && isLogin){
			JSONObject root = new JSONObject();
			try {
				JSONArray arr = new JSONArray();
				for (Matter matter : matters) {
					JSONObject itemObject = new JSONObject();
					itemObject.put("matter_name", Base64Util.BASE64_encode(matter.getMatterName()));
					Date matterDate = Constant.appDateFormat.parse(matter.getMatterTime());
					Date createTime = Constant.appDateFormat.parse(matter.getCreateTime());
					itemObject.put("matter_time", Constant.appDateFormat_2.format(matterDate));
					itemObject.put("create_time", Constant.appDateFormat_2.format(createTime));
					itemObject.put("acc_video_name", matter.getVideoName());
					itemObject.put("pic_name", matter.getPicName());
					
					itemObject.put("sort_id", matter.getSort_id()+"");
					itemObject.put("matter_time_type", "0");
					itemObject.put("classify_type", matter.getClassifyType()+"");
					itemObject.put("if_stict", matter.getIfStick()+"");
					itemObject.put("repeat_type", matter.getRepeat_type()+"");
					
					Category category =  service.queryCategoryById(this, matter.getClassifyType());
					itemObject.put("classify_name",Base64Util.BASE64_encode(category.getCategoryName()) );
					arr.put(itemObject);
				}
				root.put("matters", arr);
			} catch (Exception e) {
			}
			
			if (!TextUtils.isEmpty(root.toString())) {
				Parameters parameters = new Parameters();
				parameters.put("passport", passport);
				parameters.put("usermatter", root.toString());
				new HttpService().post(LINKS.SAVE, parameters, new CallBack<String>() {
					public void onSuccess(String t ,int code) {
						super.onSuccess(t ,code);
						Date nowTime = new Date(System.currentTimeMillis());
						getSharedPreferences("fatty_time", Context.MODE_PRIVATE).edit().putString("backupTime", Constant.appDateFormat.format(nowTime)).commit();
					}
					public void onFailure(Throwable t, int errorNo, String strMsg) {
						super.onFailure(t, errorNo, strMsg);
					}
				});
			}
		}else{
			
		}
	}
	
	/**
	 * 刷新 Notification
	 */
	public void refreshNotification(){
		//判断notificationIds 是否存在
		final SharedPreferences notifyPreferences = getSharedPreferences("NOTIFICATION", Context.MODE_PRIVATE);
		NotificationManagerCompat notificationManager =  NotificationManagerCompat.from(this);
		notificationManager.cancelAll();
		
		String idStr = notifyPreferences.getString("notificationIds", "");
		if(!TextUtils.isEmpty(idStr)){
			final ArrayList<Integer> idArrayList = new ArrayList<Integer>();
			String ids = idStr.substring(0, idStr.length()-1);
			String[] id = ids.split("@");
			for(String i : id){
				idArrayList.add(Integer.valueOf(i));
			}
			new Handler().postDelayed(new Runnable(	) {
				public void run() {
					showNotificationView(idArrayList);
					notifyPreferences.edit().putString("notificationIds","").commit();
				}
			}, 1000);
		}
	}
	
	/**
	 * 显示大饼
	 * 
	 * @param eventId
	 */
	private void showNotificationView(ArrayList<Integer> ids){
		
		topNotifyLayout = (RelativeLayout) this.findViewById(R.id.topNotifyLayout);
		viewPager = (ViewPager)this.findViewById(R.id.viewPager);
		viewGroup = (LinearLayout)this.findViewById(R.id.viewGroup);
		
		ArrayList<View> contentViews = new ArrayList<View>();
		ArrayList<Matter> notifyMatters = new ArrayList<Matter>();
		
		//去重
		ArrayList<Integer> tempIds= new ArrayList<Integer>(); 
		for(Integer i:ids){  
	        if(!tempIds.contains(i)){  
	            tempIds.add(i);  
	        }  
	    }  
		
		for(Integer id :tempIds){
			View cView = LayoutInflater.from(this).inflate(R.layout.al_dreamdays_home_top_notify_item_layout, null);
			contentViews.add(cView);
			notifyMatters.add(matterService.queryMatterById(this, id));
		}
		
		viewPager.setAdapter(new BingAdapter(ALHomeActivity.this,contentViews, notifyMatters));
		final ArrayList<ImageView> pointViews = new ArrayList<ImageView>();
	
		for (int i = 0; i < contentViews.size(); i++) {
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ImageUtil.dip2px(ALHomeActivity.this, 5), ImageUtil.dip2px(ALHomeActivity.this, 5));
			params.setMargins(20, 0, 0, 0);
			imageView.setLayoutParams(params);
			if (i == 0) {
				imageView.setBackgroundResource(R.drawable.al_circle_blank);
			} else {
				imageView.setBackgroundResource(R.drawable.al_circle_white);
			}
			viewGroup.addView(imageView);
			pointViews.add(imageView);
		}
		if(contentViews.size() > 1){
			viewGroup.setVisibility(View.VISIBLE);
		}else{
			viewGroup.setVisibility(View.INVISIBLE);
		}
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				for (int i = 0; i < pointViews.size(); i++) {
					pointViews.get(arg0).setBackgroundResource(R.drawable.al_circle_blank);
					if (arg0 != i) {
						pointViews.get(i).setBackgroundResource(R.drawable.al_circle_white);
					}
				}
			}
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				topLayout.setVisibility(View.GONE);
				topNotifyLayout.setVisibility(View.VISIBLE);
				topLayout.startAnimation(AnimationUtils.loadAnimation(ALHomeActivity.this, R.anim.alpha_out_500));
				topNotifyLayout.startAnimation(AnimationUtils.loadAnimation(ALHomeActivity.this, R.anim.alpha_in_500));
			}
		}, 300);
	}
	
	/**
	 * Repeat ViewPager Adapter
	 */
	private class BingAdapter extends  PagerAdapter{
		
		private ArrayList<View> views;
		private ArrayList<Matter> matters;
		
		public BingAdapter(Context context, ArrayList<View> views , ArrayList<Matter> matters){
			this.views = views;
			this.matters = matters;
		}

        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  

        @Override  
        public int getCount() {  
            return views.size();  
        }  

        @Override  
        public void destroyItem(ViewGroup container, int position,   Object object) {  
            container.removeView(views.get(position));  
        }  

        @Override  
        public int getItemPosition(Object object) {  
            return super.getItemPosition(object);  
        } 

        @Override  
        public Object instantiateItem(ViewGroup container,final int position) {  
        	container.addView(views.get(position));  
        	View view = views.get(position);
        	TextView tv = (TextView)view.findViewById(R.id.topNameTextView);
        	ImageView topBgImageView =(ImageView)view.findViewById(R.id.topBgImageView);
        	
        	final Matter itemMatter = matters.get(position);
        	if(itemMatter!=null){
            	String bgImagePath = itemMatter.getPicName();
    			if (!TextUtils.isEmpty(bgImagePath) && FileUtil.checkFileExists(bgImagePath)) {
    				ImageLoader.getInstance().displayImage("file:///" + bgImagePath, topBgImageView);
    			}else{
    				int state = itemMatter.getClassifyType() == 0 ? 1 : itemMatter.getClassifyType();
    				ALHomeActivity.setCover(topBgImageView, ALHomeActivity.this, state);
    			}
            	
            	tv.setText(itemMatter.getMatterName());
            	view.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(final View v) {
    					topLayout.setVisibility(View.VISIBLE);
    					topNotifyLayout.setVisibility(View.GONE);
    					topLayout.startAnimation(AnimationUtils.loadAnimation(ALHomeActivity.this, R.anim.alpha_in_500));
    					topNotifyLayout.startAnimation(AnimationUtils.loadAnimation(ALHomeActivity.this, R.anim.alpha_out_500));
    					
    					new Handler().postDelayed(new Runnable() {
    						public void run() {
    							Intent detailIntent = new Intent();
    							detailIntent.setClass(ALHomeActivity.this, ALDetailActivity.class);
    							detailIntent.putExtra("matter_id", itemMatter.get_id());
    							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
    								startActivity(detailIntent, ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight()).toBundle());
    							}else{
    								startActivity(detailIntent);
    							}
    						}
    					}, 300);
    				}
    			});
        	}
            return views.get(position);  
        }  
	}
	
	/****************************************广告 ************************************/
	private AdView adView;
	private void addAdmod(){
		adView = new AdView(this);
		adView.setAdUnitId(Constant.ADMOD_KEY);
		adView.setAdSize(AdSize.BANNER);
		((LinearLayout)this.findViewById(R.id.googleAdsLayout)).setVisibility(View.VISIBLE);
		((LinearLayout)this.findViewById(R.id.googleAdsLayout)).addView(adView);
	    adView.loadAd(new AdRequest.Builder().build());
	}
	
	/**
	 * 刷新TopView
	 */
	private void refreshTopView(){
		CategoryService categoryDB = new CategoryService();
		ArrayList<Category> categories = categoryDB.queryHavedCategory(this);
		boolean isHidden = categories.size() < 2 ? true : false ;
		if(isHidden){
			eventBarLayout.setVisibility(View.GONE);
		}else{
			eventBarLayout.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 刷新Event的View
	 */
	private void refreshEventView(){
		if(topMatter==null && matters.isEmpty()){
			noEventsLayout.setVisibility(View.VISIBLE);
		}else{
			noEventsLayout.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 删除Event Dialog
	 * 
	 * @param context
	 */
	public void showRemoveEventDialog(final Context context , final int matterID) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			String[] provinces = new String[] { getString(R.string.al_delete_btn), getString(R.string.cancel)} ;
			AlertDialog.Builder builder= new Builder(context , android.R.style.Theme_Material_Light_Dialog);
			builder.setTitle(R.string.AddActivity_delete);
			builder.setItems(provinces, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						dialog.dismiss();
						
						Matter matter = matterService.queryMatterById(context, matterID);
						if(matter.getIfStick() == 1)
							topMatter = null;
						matterService.deleteMatterById(context, matterID);
						refreshListView(categoryId, topMatter);
						break;
					case 1:
						dialog.dismiss();
						break;
					default:
						break;
					}
				}
			});
			builder.create().show();
		}else{
			final AlertBuilder dialogBuilder = AlertBuilder.getInstance(context);
			dialogBuilder
			.isCancelableOnTouchOutside(true) 
			.withDuration(300) 
			.withEffect(Effectstype.SlideBottom) 
			.withTitle(context.getString(R.string.AddActivity_delete))
			.withOkButtonText(context.getString(R.string.al_delete_btn)) 
			.withCancelButtonText(context.getString(R.string.cancel))
			.setOnOkButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Matter matter = matterService.queryMatterById(context, matterID);
					if(matter.getIfStick() == 1)
						topMatter = null;
					matterService.deleteMatterById(context, matterID);
					refreshListView(categoryId, topMatter);
					dialogBuilder.cancel();
				}
			}).setOnCacnelButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialogBuilder.cancel();
				}
			}).show();
		}
		
	}

	/**
	 * 
	 * 设置灰色icon
	 * 
	 * @param imgView
	 * @param context
	 * @param matter
	 */
	public static void setBlackIcon(ImageView imgView , Context context , int id){
		int iconRes = Constant.event_black_icons[6];
		if (id < 7) {
			iconRes = Constant.event_black_icons[id-1];
		}
		imgView.setImageResource(iconRes);
	}
	
	/**
	 * 设置灰色以及红色icon
	 * 
	 * @param imgView
	 * @param context
	 * @param id
	 */
	public static void setBlackRedIcon(ImageView imgView , Context context , int id){
		int iconRes = Constant.event_black_red_icons[6];
		if (id < 7) {
			iconRes = Constant.event_black_red_icons[id-1];
		}
		imgView.setImageResource(iconRes);
	}
	
	/**
	 * 
	 * 设置白色icon
	 * 
	 * @param imgView
	 * @param context
	 * @param matter
	 */
	public static void setWhiteIcon(ImageView imgView , Context context ,int id){
		int iconRes = Constant.event_white_icons[6];
		if (id < 7) {
			iconRes = Constant.event_white_icons[id-1];
		}
		imgView.setImageResource(iconRes);
	}
	
	/**
	 * 设置详情页背景
	 * 
	 * @param imgView
	 * @param context
	 * @param matter
	 */
	public static void setDetailCenterBg(ImageView imgView , Context context , int id){
		int iconRes = Constant.detail_center_bgs[6];
		if (id < 7) {
			iconRes = Constant.detail_center_bgs[id-1];
		}
		imgView.setImageBitmap(ImageUtil.loadResourceDrawable(context, iconRes));
	}
	
	/**
	 *  设置详情页背景
	 * @param context
	 * @param id
	 * @return
	 */
	public static int setDetailBg_int(Context context , int id){
		int iconRes = Constant.detail_bgs[6];
		if (id < 7) {
			iconRes = Constant.detail_bgs[id-1];
		}
		return iconRes;
	}
	
	/**
	 * 设置cover背景
	 * 
	 * @param imgView
	 * @param context
	 * @param matter
	 */
	public static void setCover(ImageView imgView , Context context ,int id){
		int iconRes = Constant.top_bgs[6];
		if (id < 7) {
			iconRes = Constant.top_bgs[id-1];
		}
		Bitmap bitmap = ImageUtil.loadResourceDrawable(context, iconRes);
		imgView.setImageBitmap(bitmap);
	}
	
	public void setTitleBarColor(){
		float color_h = getSharedPreferences("color", Context.MODE_PRIVATE).getFloat("color_h", 0.0f);
		float color_s = getSharedPreferences("color", Context.MODE_PRIVATE).getFloat("color_s", 0.0f);
		float color_v = getSharedPreferences("color", Context.MODE_PRIVATE).getFloat("color_v", 0.0f);
		float[] colors = new float[3];
		colors[0] = color_h;
		colors[1] = color_s;
		colors[2] = color_v;
		homeTitleLayout.setBackgroundColor(Color.HSVToColor(colors));
		notifyBarTextView.setBackgroundColor(Color.HSVToColor(colors));
	}
	
	
	
	
	
	
	
	
	
}
