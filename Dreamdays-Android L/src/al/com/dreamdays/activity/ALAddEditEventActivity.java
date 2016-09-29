package al.com.dreamdays.activity;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import al.com.dreamdays.activity.photo.ALCameraRollActivity;
import al.com.dreamdays.activity.photo.ALFixPhotoActivity;
import al.com.dreamdays.base.AManager;
import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.base.KEY;
import al.com.dreamdays.sqlite.Category;
import al.com.dreamdays.sqlite.CategoryService;
import al.com.dreamdays.sqlite.Matter;
import al.com.dreamdays.sqlite.MatterService;
import al.com.dreamdays.sqlite.Repeat;
import al.com.dreamdays.utils.DialogUtil;
import al.com.dreamdays.utils.FileUtil;
import al.com.dreamdays.utils.ImageUtil;
import al.com.dreamdays.utils.UmengClick;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.api.alertview.AlertBuilder;
import com.api.alertview.Effectstype;
import com.guxiu.dreamdays.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 
 * @author Fatty
 *
 */
@SuppressLint("NewApi")
public class ALAddEditEventActivity extends BaseActivity implements OnClickListener  , SensorEventListener{
	
	private int repeatPosition = 0;
	private int categoryID = 0 ;
	
	// 录音播放状态
	private static final int RECORD_PLAYING = 1;
	private static final int RECORD_STOPED = 0;
	public static final int REQ_CODE_RECORDING = 3;
	private int recordStatus = 0;//录音播放状态
	private MediaPlayer mMediaPlayer;
	
	public static final int PHOTO_CAMERA = 0; // 表示图片采集
	public static final int PHOTO_GALLERY = 1; // 表示图片选取
	public static final int PHOTO_WALLPAPER = 2;
	
	private final static int WALLPAPER = 0;
	private final static int CAMERA = 1;
	
	private int matterID ;
	private String photoFileName;
	
	//Android L
	private ImageButton exitImageButton;
	private TextView titleTextView , doneTextView;
	private ProgressBar doneProgressBar;
	
	private RelativeLayout eventCategoryLayout ,eventDateLayout ,eventRepeatLayout ,eventCoverLayout ,eventNotificationLayout ,eventBackgroundLayout  ,eventRepeatDateLayout ,eventVoiceLayout;
	private EditText eventNameEditText;
	private TextView eventCategoryPreTextView ,eventDatePreTextView ,eventRepeatPreTextView ,eventCoverPreTextView ,eventNotificationPreTextView ,eventBackgroundPreTextView ,eventVoicePreTextView ,eventRepeatDatePreTextView ,eventRepeatDateTextView;
	private TextView eventCategoryTextView ,eventDateTextView ,eventRepeatTextView;
	private TextView topLine , bottomLine ;
	private ImageView eventBackgroundImageView;
	
	private CheckBox eventCoverCheckbox ,eventNotificationCheckbox;
	
	private LinearLayout eventCoverCheckboxLayout , eventNotificationCheckboxLayout;
	
	private ImageButton audiaImageButton;
	private RelativeLayout deleteLayout;
	
	//Event Data 
	private Matter globalMatter;
	private MatterService matterService;
	private CategoryService categoryService;
	private boolean isCorver = false ;
	
	//是否新建
	boolean isCreateNewEvent = false;
	
	public static final String ADD_EDIT_ACTION = "ADD_EDIT_ACTION";
	private AddEditBroadcastReveiver reveiver;

	/**
	 * 编辑广播
	 * 
	 * @author Fatty
	 *
	 */
	public class AddEditBroadcastReveiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String picName = intent.getStringExtra("picName");
			if(!TextUtils.isEmpty(picName)){
				photoTools(picName);
			}
		}
	}
	
	public static final String CATEGORY_CHANGED_ACTION = "CATEGORY_CHANGED";
	private CategoryChangedReceiver categoryChangedReceiver;
	/**
	 * 
	 * Category更改广播
	 * 
	 * @author fatty
	 *
	 */
	public class CategoryChangedReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
			int deleteId = intent.getIntExtra("deleteId", 1);
			if(deleteId == categoryID){
				categoryID = 1;
				String tag =  new CategoryService().queryCategoryById(ALAddEditEventActivity.this, 1).getCategoryName();
				eventCategoryTextView.setText(tag);
				ALHomeActivity.setDetailCenterBg(eventBackgroundImageView, ALAddEditEventActivity.this, 1);
			}
		}
	}
	
	private SensorManager sensorManager ;
	private Sensor lightSensor ;
	private AudioManager audioManager;  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme();
		setContentView(R.layout.al_dreamdays_a_add_layout);
		hiddenView(findViewById(R.id.notifyBarTextView));
		
		reveiver = new AddEditBroadcastReveiver();
		this.registerReceiver(reveiver, new IntentFilter(ADD_EDIT_ACTION));
		
		//广播可以做很多事情...例如...
		categoryChangedReceiver = new CategoryChangedReceiver();
		this.registerReceiver(categoryChangedReceiver, new IntentFilter(CATEGORY_CHANGED_ACTION));
		
		matterService = new MatterService();
		categoryService = new CategoryService();
		
		isCorver = getIntent().getBooleanExtra("isCorver", false);
		
		matterID = getIntent().getIntExtra("matter_id", 0);
		globalMatter = matterService.queryMatterById(this, matterID);
		photoFileName = "dreamdays_" + System.currentTimeMillis()+"_";
		//title
		exitImageButton = (ImageButton)this.findViewById(R.id.exitImageButton);
		titleTextView = (TextView)this.findViewById(R.id.titleTextView);
		exitImageButton.setOnClickListener(this);
		
		doneTextView = (TextView)this.findViewById(R.id.doneTextView);
		doneProgressBar = (ProgressBar)this.findViewById(R.id.doneProgressBar);
		doneTextView.setOnClickListener(ALAddEditEventActivity.this);
		titleTextView.setTypeface(BaseApplication.typeface_heavy);
		doneTextView.setTypeface(BaseApplication.typeface_heavy);
		//pre
		eventCategoryPreTextView = (TextView)this.findViewById(R.id.eventCategoryPreTextView);
		eventDatePreTextView = (TextView)this.findViewById(R.id.eventDatePreTextView);
		eventRepeatPreTextView = (TextView)this.findViewById(R.id.eventRepeatPreTextView);
		eventNotificationPreTextView = (TextView)this.findViewById(R.id.eventNotificationPreTextView);
		eventCoverPreTextView = (TextView)this.findViewById(R.id.eventCoverPreTextView);
		eventRepeatDatePreTextView = (TextView)this.findViewById(R.id.eventRepeatDatePreTextView);
		
		//layout
		eventCategoryLayout = (RelativeLayout)this.findViewById(R.id.eventCategoryLayout);
		eventDateLayout = (RelativeLayout)this.findViewById(R.id.eventDateLayout);
		eventRepeatLayout = (RelativeLayout)this.findViewById(R.id.eventRepeatLayout);
		eventCoverLayout = (RelativeLayout)this.findViewById(R.id.eventCoverLayout);
		eventNotificationLayout = (RelativeLayout)this.findViewById(R.id.eventNotificationLayout);
		eventBackgroundLayout = (RelativeLayout)this.findViewById(R.id.eventBackgroundLayout);
		eventVoiceLayout = (RelativeLayout)this.findViewById(R.id.eventVoiceLayout);
		eventVoiceLayout.setOnClickListener(this);
		audiaImageButton = (ImageButton)this.findViewById(R.id.audiaImageButton);
		audiaImageButton.setOnClickListener(this);
		eventCategoryLayout.setOnClickListener(this);
		eventDateLayout.setOnClickListener(this);
		eventRepeatLayout.setOnClickListener(this);
		eventCoverLayout.setOnClickListener(this);
		eventNotificationLayout.setOnClickListener(this);
		eventBackgroundLayout.setOnClickListener(this);
		
		deleteLayout = (RelativeLayout)this.findViewById(R.id.deleteLayout);
		deleteLayout.setOnClickListener(this);
		
		   topLine = (TextView)this.findViewById(R.id.topLine );
		   bottomLine =(TextView )this.findViewById(R.id.bottomLine);
		
		//event name
		eventNameEditText = (EditText)this.findViewById(R.id.eventNameEditText);
		eventNameEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		//pre TextView
		eventCategoryPreTextView =(TextView)this.findViewById(R.id.eventCategoryPreTextView);
		eventDatePreTextView =(TextView)this.findViewById(R.id.eventDatePreTextView);
		eventRepeatPreTextView =(TextView)this.findViewById(R.id.eventRepeatPreTextView);
		eventCoverPreTextView =(TextView)this.findViewById(R.id.eventCoverPreTextView);
		eventNotificationPreTextView =(TextView)this.findViewById(R.id.eventNotificationPreTextView);
		eventBackgroundPreTextView =(TextView)this.findViewById(R.id.eventBackgroundPreTextView);
		eventVoicePreTextView =(TextView)this.findViewById(R.id.eventVoicePreTextView);
		eventVoicePreTextView.setOnClickListener(this);
		
		//content TextView
		eventCategoryTextView = (TextView)this.findViewById(R.id.eventCategoryTextView);
		eventDateTextView = (TextView)this.findViewById(R.id.eventDateTextView);
		eventRepeatTextView = (TextView)this.findViewById(R.id.eventRepeatTextView);
		
		//background ImageView
		eventBackgroundImageView= (ImageView)this.findViewById(R.id.eventBackgroundImageView);
		
		//checkbox 
		eventCoverCheckbox = (CheckBox)this.findViewById(R.id.eventCoverCheckbox);
		eventNotificationCheckbox =(CheckBox)this.findViewById(R.id.eventNotificationCheckbox);
		
		eventRepeatDateLayout = (RelativeLayout)this.findViewById(R.id.eventRepeatDateLayout);
		eventRepeatDateLayout.setOnClickListener(this);
		eventRepeatDateTextView  = (TextView)this.findViewById(R.id.eventRepeatDateTextView);
		
		eventCoverCheckboxLayout = (LinearLayout)this.findViewById(R.id.eventCoverCheckboxLayout);
		eventCoverCheckboxLayout.setOnClickListener(this);
		eventNotificationCheckboxLayout = (LinearLayout)this.findViewById(R.id.eventNotificationCheckboxLayout);
		eventNotificationCheckboxLayout.setOnClickListener(this);
		
		//set typeface
		eventCategoryPreTextView.setTypeface(BaseApplication.typeface_medium);
		eventDatePreTextView.setTypeface(BaseApplication.typeface_medium);
		eventRepeatPreTextView.setTypeface(BaseApplication.typeface_medium);
		eventNotificationPreTextView.setTypeface(BaseApplication.typeface_medium);
		eventCoverPreTextView.setTypeface(BaseApplication.typeface_medium);
		eventRepeatDatePreTextView.setTypeface(BaseApplication.typeface_medium);
		eventBackgroundPreTextView.setTypeface(BaseApplication.typeface_medium);
		eventNameEditText.setTypeface(BaseApplication.typeface_medium);
		eventVoicePreTextView.setTypeface(BaseApplication.typeface_medium);
		eventCategoryTextView.setTypeface(BaseApplication.typeface_medium);
		eventDateTextView.setTypeface(BaseApplication.typeface_medium);
		eventRepeatTextView.setTypeface(BaseApplication.typeface_medium);
		
		eventRepeatDateTextView.setTypeface(BaseApplication.typeface_medium);
		
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		lightSensor   = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		audioManager  = (AudioManager)getSystemService(Context.AUDIO_SERVICE);  
		
		setEventData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		checkVoice();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this, lightSensor);
	}
	
	/**
	 * onDestory
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		doneProgressBar.setVisibility(View.GONE);
		doneTextView.setVisibility(View.VISIBLE);
		
		if(reveiver!=null){
			this.unregisterReceiver(reveiver);
		}
		
		if(categoryChangedReceiver!=null){
			this.unregisterReceiver(categoryChangedReceiver);
		}
		
		stopVoice() ;
	}
	
	/**
	 * onClick点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.eventNotificationCheckboxLayout:
				if(eventNotificationCheckbox.isChecked()){
					eventNotificationCheckbox.setChecked(false);
				}else{
					eventNotificationCheckbox.setChecked(true);
				}
			break;
		case R.id.eventCoverCheckboxLayout:
			if (eventCoverCheckbox.isChecked()) {
				eventCoverCheckbox.setChecked(false);
			} else {
				eventCoverCheckbox.setChecked(true);
			}
			break;
		case R.id.deleteLayout:
			
			showRemoveEventDialog(this);
			
			break;
			
		case R.id.eventDateLayout:
			showDateTimePicker() ;
			break;
			

		case R.id.eventCategoryLayout:
			Intent categoryIntent = new Intent();
			categoryIntent.putExtra("categoryID", categoryID);
			categoryIntent.setClass(ALAddEditEventActivity.this, ALCategoryActivity.class);
			startActivityForResult(categoryIntent, Constant.ADDCATEGORY);
			
			stopVoice() ;
			break;
			
		case R.id.eventBackgroundLayout:
			stopVoice() ;
			selectPhotoDialog(this);
			break;
			
		case R.id.exitImageButton:
			
			if(!isCreateNewEvent){
				stopVoice() ;
				
				updateVoice();
			}else{
				finish();
			}
			
			break;
		case R.id.doneTextView:
			stopVoice() ;
			
			doneProgressBar.setVisibility(View.VISIBLE);
			doneTextView.setVisibility(View.GONE);
			
			changedOrSubmitEvent();
			
			break;
		case R.id.audiaImageButton:
			startPlayVoice(globalMatter.getVideoName() ,audiaImageButton);
			break;
		case R.id.eventVoiceLayout:
		case R.id.eventVoicePreTextView:
			if(TextUtils.isEmpty(globalMatter.getVideoName())){
				Intent recordVoiceIntent = new Intent();
				recordVoiceIntent.setClass(this, ALVoice_EActivity.class);
				recordVoiceIntent.putExtra("matterID", globalMatter.get_id());
				startActivityForResult(recordVoiceIntent, Constant.ADDVOICE);
			}else{
				showVoiceMemoDialog(ALAddEditEventActivity.this);
			}
			break;
		case R.id.eventRepeatDateLayout:
			
			Intent repeatIntent = new Intent();
			repeatIntent.putExtra("repeatPosition", repeatPosition);
			repeatIntent.setClass(ALAddEditEventActivity.this, ALRepeatActivity.class);
			startActivityForResult(repeatIntent, Constant.REPEAT);
			
			stopVoice() ;
			break;
		default:
			break;
		}
	}
	
	/**
	 * onActivityResult
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constant.ADDCATEGORY:
			if (null != data) {
				categoryID = data.getIntExtra("backCategoryId", 0);
				CategoryService categoryService = new CategoryService();
				String tag =  categoryService.queryCategoryById(ALAddEditEventActivity.this, categoryID).getCategoryName();
				eventCategoryTextView.setText(tag);
				
				String bgImagePath = globalMatter.getPicName();
				if (!TextUtils.isEmpty(bgImagePath) && FileUtil.checkFileExists(bgImagePath)) {
					ImageLoader.getInstance().displayImage("file:///" + bgImagePath, eventBackgroundImageView);
				}else{
					int state =categoryID == 0 ? 1 : categoryID;
					ALHomeActivity.setDetailCenterBg(eventBackgroundImageView, this, state);
				}
			}
			break;
			
		case PHOTO_CAMERA:
			//拍照
			File picture = new File(Constant.DREAMDAY_PIC, photoFileName	+ ".jpg");
			if(!picture.exists()){
				return;
			}
			if( Uri.fromFile(picture)!=null){
				try {
				} catch (Exception e) {
					return ;
				}
			}else return ;
			
			String photoPath = Constant.DREAMDAY_PIC + photoFileName + ".jpg";
			Intent intent = new Intent();
			intent.putExtra("picUrl", photoPath);
			intent.setClass(ALAddEditEventActivity.this, ALFixPhotoActivity.class);
			startActivity(intent);
			
		case Constant.ADDVOICE:
			if(data == null){
				return;
			}
			String voicePath = data.getStringExtra("path");
			if(!TextUtils.isEmpty(voicePath)){
				globalMatter.setVideoName(voicePath);
				audiaImageButton.setVisibility(View.VISIBLE);
				audiaImageButton.setBackgroundResource(R.drawable.al_play_blue);
			}
			break;
			
		case Constant.REPEAT:
			if(data!=null){
				Repeat repeat = (Repeat) data.getSerializableExtra("repeat");
				eventRepeatDateTextView.setText(repeat.getName());
				eventRepeatDateTextView.setTag(repeat.getId());
				repeatPosition = repeat.getId();
			}
			break;
		}
	}
	
	/**
	 * 返回键
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(!isCreateNewEvent){
				stopVoice() ;
				
				updateVoice();
			}else{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 
	 * set Event Data
	 * 
	 * @param detailMatter
	 */
	private void setEventData(){
		if(globalMatter!=null){
			isCreateNewEvent = false;
			//set title
			titleTextView.setText("Edit");//设置标题
			deleteLayout.setVisibility(View.VISIBLE);
			
			eventBackgroundPreTextView.setTextColor(Color.WHITE);
			eventBackgroundPreTextView.setShadowLayer(1, 0, -2, Color.parseColor("#60000000"));
			
			//set event name
			eventNameEditText.setText(globalMatter.getMatterName());//设置event名字
			eventDateTextView.setText(Constant.appDateFormat.format(new Date()));//设置event时间

			//set event isNotify
			boolean isNotify = globalMatter.getIf_notify() > 0 ? true : false; //设置是否通知
			eventNotificationCheckbox.setChecked(isNotify);
			
			
			//set event isStick
			boolean isStick = globalMatter.getIfStick() > 0 ? true : false ;//设置是否顶置
			eventCoverCheckbox.setChecked(isStick);
			
			//set event category
			Category category =categoryService.queryCategoryById(this, globalMatter.getClassifyType());
			if(category!=null){
				categoryID = category.get_id();
				String cName =  category.getCategoryName();
				if(!TextUtils.isEmpty(cName)){
					eventCategoryTextView.setText(cName);
				}
			}else{
				eventCategoryTextView.setText("None");
			}
			
			//set event setbackgrond
			eventBackgroundImageView.setVisibility(View.VISIBLE);
			String bgImagePath = globalMatter.getPicName();
			if (!TextUtils.isEmpty(bgImagePath) && FileUtil.checkFileExists(bgImagePath)) {
				ImageLoader.getInstance().displayImage("file:///" + bgImagePath, eventBackgroundImageView);
			}else{
				int state = globalMatter.getClassifyType() == 0 ? 1 : globalMatter.getClassifyType();
				ALHomeActivity.setDetailCenterBg(eventBackgroundImageView, this, state);
			}
			
			//set event date
			eventDateTextView.setText(globalMatter.getMatterTime());
			
			//set event video
			if (!TextUtils.isEmpty(globalMatter.getVideoName()) && FileUtil.checkFileExists(globalMatter.getVideoName())) {
				audiaImageButton.setBackgroundResource(R.drawable.al_play_blue);
			} else {
				audiaImageButton.setVisibility(View.INVISIBLE);
			}
			
			//set event repeat
			Repeat repeat = ALRepeatActivity.initRepeatData(ALAddEditEventActivity.this).get(globalMatter.getRepeat_type());
			eventRepeatDateTextView.setText(repeat.getName());
			eventRepeatDateTextView.setTag(repeat.getId());
			repeatPosition = repeat.getId();
		}else{
			isCreateNewEvent = true;
			
			topLine.setVisibility(View.VISIBLE); 
			bottomLine.setVisibility(View.VISIBLE);
			 
			categoryID = 1;
			
			deleteLayout.setVisibility(View.GONE);
			if(globalMatter==null){
				globalMatter = new Matter();
			}
			// set title
			titleTextView.setText("Create");
			eventNameEditText.setHint(getString(R.string.AddActivity_untitled_title));
			eventDateTextView.setText(Constant.appDateFormat.format(new Date()));

				//set checkbox
				eventNotificationCheckbox.setChecked(true);
				globalMatter.setIf_notify(1);
				boolean isNotify = globalMatter.getIf_notify() > 0 ? true : false;
				eventNotificationCheckbox.setChecked(isNotify);

				if(isCorver){
					eventCoverCheckbox.setChecked(true);
				}else{
					boolean isStick = globalMatter.getIfStick() > 0 ? true : false;
					eventCoverCheckbox.setChecked(isStick);
				}
			
			
			eventCategoryTextView.setText(R.string.d_Anniversary);
			
			String bgImagePath = globalMatter.getPicName();
			if (!TextUtils.isEmpty(bgImagePath) && FileUtil.checkFileExists(bgImagePath)) {
				ImageLoader.getInstance().displayImage("file:///" + bgImagePath, eventBackgroundImageView);
			}else{
				int state = globalMatter.getClassifyType() == 0 ? 1 : globalMatter.getClassifyType();
				ALHomeActivity.setDetailCenterBg(eventBackgroundImageView, this, state);
			}
			
			new Timer().schedule(new TimerTask() {
				public void run() {
					InputMethodManager inputManager = (InputMethodManager) eventNameEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.showSoftInput(eventNameEditText, 0);
					eventNameEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
				}
			}, 500);
		}
		
		eventNotificationCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					globalMatter.setIf_notify(1);
				} else {
					globalMatter.setIf_notify(0);
				}
			}
		});
		
		eventCoverCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					globalMatter.setIfStick(1);
				} else {
					globalMatter.setIfStick(0);
				}
			}
		});
		
	}
	//1280x768
	
	/**
	 * 修改并提交事件
	 */
	private void changedOrSubmitEvent(){
		boolean isLite = getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isLite", true);
		boolean isOpen = getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isOpen", true);
		if(isLite){
			if(isOpen){//是否限免
				if(matterService.getCount(ALAddEditEventActivity.this) < 10 || matterID != 0){
					boolean isReview = this.getSharedPreferences("review",Context.MODE_PRIVATE).getBoolean("isReview", false);
					if(!isReview){
						if(matterService.getCount(ALAddEditEventActivity.this) < 7 || matterID != 0){
							insertOrUpdateMatter();
						}else{
							showRateGooglePlayDialog(ALAddEditEventActivity.this);
						}
					}else{
						insertOrUpdateMatter();
					}
				}else{
					DialogUtil.showGooglePlayDialog(ALAddEditEventActivity.this);
				}
			}else{
				insertOrUpdateMatter();
			}
		}else{
			insertOrUpdateMatter();
		}
	}
	
	/**
	 * insert Or Update Matter
	 */
	private void insertOrUpdateMatter(){
		if (globalMatter != null) {
			
			String matterTime = eventDateTextView.getText().toString();
			//setMatterTime
			globalMatter.setMatterTime(matterTime);
			globalMatter.setCreateTime(Constant.appDateFormat.format(new Date()));
			
			//setMatterName
			if (TextUtils.isEmpty(eventNameEditText.getText().toString())) {
				eventNameEditText.setText(R.string.AddActivity_untitled);
			}
			globalMatter.setMatterName(eventNameEditText.getText().toString());
			
			//setClassityType
			globalMatter.setClassifyType(categoryService.queryIdByName(this, eventCategoryTextView.getText().toString()));
			
			//setPicName
			if (TextUtils.isEmpty(globalMatter.getPicName())) {
				globalMatter.setPicName("");
			}
			
			//setVideoName
			if (TextUtils.isEmpty(globalMatter.getVideoName())) {
				globalMatter.setVideoName("");
			}
			
				//setIf_notify
				if (eventNotificationCheckbox.isChecked()) {
					globalMatter.setIf_notify(1);
				} else {
					globalMatter.setIf_notify(0);
				}
				
				//setIfStick
				if (eventCoverCheckbox.isChecked()) {
					globalMatter.setIfStick(1);
				} else {
					globalMatter.setIfStick(0);
				}
			
			//setRepeat_type
			int repeat_type = 0;
			if(eventRepeatDateTextView.getTag() != null){
				repeat_type = (Integer) eventRepeatDateTextView.getTag();
			}
			globalMatter.setRepeat_type(repeat_type);
			
			//set repeat matter date 
			try {
				Date getedMatterDate = Constant.appDateFormat.parse(matterTime);
				String nowTime = Constant.appDateFormat.format(new Date());
				Date nowDate = Constant.appDateFormat.parse(nowTime);
				Calendar calender = Calendar.getInstance();
				calender.setTime(getedMatterDate);
				switch (repeat_type) {
					case 1://Weekly   		7天
						while (getedMatterDate.before(nowDate) || getedMatterDate.equals(nowDate)) {
							calender.add(Calendar.DATE, 7);
							getedMatterDate = calender.getTime();
						}
						globalMatter.setMatterTime(Constant.appDateFormat.format(getedMatterDate));
						break;
					case 2://Bi-Weekly  	14天
						while (getedMatterDate.before(nowDate) || getedMatterDate.equals(nowDate)) {
							calender.add(Calendar.DATE, 14);
							getedMatterDate = calender.getTime();
						}
						globalMatter.setMatterTime(Constant.appDateFormat.format(getedMatterDate));
						break;
					case 3://Monthly   		1个月
						while (getedMatterDate.before(nowDate) || getedMatterDate.equals(nowDate)) {
							calender.add(Calendar.MONTH, 1);
							getedMatterDate = calender.getTime();
						}
						globalMatter.setMatterTime(Constant.appDateFormat.format(getedMatterDate));
						break;
					case 4://Quarterly   	4个月
						while (getedMatterDate.before(nowDate) || getedMatterDate.equals(nowDate)) {
							calender.add(Calendar.MONTH, 4);
							getedMatterDate = calender.getTime();
						}
						globalMatter.setMatterTime(Constant.appDateFormat.format(getedMatterDate));
						break;
					case 5://Semi-annually  6个月
						while (getedMatterDate.before(nowDate) || getedMatterDate.equals(nowDate)) {
							calender.add(Calendar.MONTH, 6);
							getedMatterDate = calender.getTime();
						}
						globalMatter.setMatterTime(Constant.appDateFormat.format(getedMatterDate));
						break;
					case 6://Annually       12个月
						while (getedMatterDate.before(nowDate) || getedMatterDate.equals(nowDate)) {
							calender.add(Calendar.YEAR, 1);
							getedMatterDate = calender.getTime();
						}
						globalMatter.setMatterTime(Constant.appDateFormat.format(getedMatterDate));
						break;
					}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			matterService.insertOrUpdateNewMatter(this, globalMatter);
		}
		
		if(globalMatter.getIfStick() == 1){
			new Thread(new Runnable() {
				@Override
				public void run() {
					setCoverTitleBarColor(globalMatter);
					
					if(!isCreateNewEvent){
						handler.sendEmptyMessage(10086);
					}else{
						handler.sendEmptyMessage(10087);
					}
				}
			}).start();
		}else{
			if(!isCreateNewEvent){
				Intent closeDetailIntent = new Intent();
				closeDetailIntent.setAction(ALDetailActivity.ACTION);
				sendBroadcast(closeDetailIntent);
				
				Intent intent = new Intent();
				intent.setClass(ALAddEditEventActivity.this, ALDetailActivity.class);
				intent.putExtra("matter_id", matterID);
				startActivity(intent);
				
				finish();
			}else{
				
				finish();
			}
		}
	}
	
	/**
	 * Handler
	 */
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 10086:
				
				Intent closeDetailIntent = new Intent();
				closeDetailIntent.setAction(ALDetailActivity.ACTION);
				sendBroadcast(closeDetailIntent);
				
				Intent intent = new Intent();
				intent.setClass(ALAddEditEventActivity.this, ALDetailActivity.class);
				intent.putExtra("matter_id", matterID);
				startActivity(intent);
				
				finish();
				break;
			case 10087:
				
				finish();
				break;
			}
		}
	};
	
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
	 * 刷新录音
	 */
	private void updateVoice(){
		if (globalMatter != null) {
			matterService.updateVoiceMatter(this, globalMatter);
			
			Intent closeDetailIntent = new Intent();
			closeDetailIntent.setAction(ALDetailActivity.ACTION);
			sendBroadcast(closeDetailIntent);
			
			Intent intent = new Intent();
			intent.setClass(ALAddEditEventActivity.this, ALDetailActivity.class);
			intent.putExtra("matter_id", matterID);
			startActivity(intent);	
			
			finish();
		}
	}
	
	/**
	 * 检查是否有memo
	 */
	private void checkVoice(){
		if(TextUtils.isEmpty(globalMatter.getVideoName())){
			eventVoicePreTextView.setText(R.string.add_voice_memo);
			audiaImageButton.setVisibility(View.INVISIBLE);
		}else{
			eventVoicePreTextView.setText(R.string.remove_voice_memo);
			audiaImageButton.setBackgroundResource(R.drawable.al_play_blue);
		}
	}
	
	/**
	 * 图片处理
	 * 
	 * @param imgPath
	 */
	private void photoTools(String imgPath){
		if (!TextUtils.isEmpty(imgPath)) {
			
			eventBackgroundImageView.setVisibility(View.VISIBLE);
			eventBackgroundPreTextView.setTextColor(Color.WHITE);
			eventBackgroundPreTextView.setShadowLayer(1, 0, -2, Color.parseColor("#60000000"));
			
			ImageSize targetImageSize = new ImageSize(Constant.width , ImageUtil.dip2px(ALAddEditEventActivity.this, 100));
			ImageLoader.getInstance().loadImage("file:///" + imgPath, targetImageSize, new ImageLoadingListener() {
				public void onLoadingStarted(String arg0, View arg1) {
				}
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				}
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					eventBackgroundImageView.setImageBitmap(arg2);
				}
				public void onLoadingCancelled(String arg0, View arg1) {
				}
			});
			
			try {
				if(ImageUtil.saveAndCompressImageInCache(imgPath, Constant.DREAMDAY_PIC, photoFileName + Constant.BIG_IMG_EXT)){
					globalMatter.setPicName(Constant.DREAMDAY_PIC + photoFileName + Constant.BIG_IMG_EXT+ ".jpg");
				}else{
					Toast.makeText(this, "Your Photo is so large", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(ALAddEditEventActivity.this, "SDCard not found", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 播放
	 * 
	 * @param path
	 * @param playButton
	 */
	private void startPlayVoice(String path ,final ImageButton playButton) {
		if (FileUtil.checkFileExists(path)) {
			if (recordStatus == RECORD_STOPED) {
				if(sensorManager!=null){
					sensorManager.registerListener(this , lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
				}
				playButton.setBackgroundResource(R.drawable.al_stop_blue);
				try {
					mMediaPlayer = new MediaPlayer();
					mMediaPlayer.setDataSource(path);
					mMediaPlayer.prepare();
					mMediaPlayer.start();
				} catch (Exception e) {
				}
				mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {
						playButton.setBackgroundResource(R.drawable.al_play_blue);
						stopVoice();
						setScreenBrightness(255, ALAddEditEventActivity.this);
					}
				});
				recordStatus = RECORD_PLAYING;
			} else {
				playButton.setBackgroundResource(R.drawable.al_play_blue);
				pauseVoice();
			}
		} else {
			Toast.makeText(this, R.string.app_record_notfound, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 停止
	 * 
	 * stop
	 */
	private void stopVoice() {
		if (null != mMediaPlayer) {
			mMediaPlayer.stop();
			recordStatus = RECORD_STOPED;
			audiaImageButton.setBackgroundResource(R.drawable.al_play_blue);
			if(sensorManager!=null){
				sensorManager.unregisterListener(this, lightSensor);
			}
		}
	}

	/**
	 * 暂停
	 * 
	 * pause
	 */
	private void pauseVoice() {
		try {
			mMediaPlayer.pause();
			recordStatus = RECORD_STOPED;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * set DateTimer dialog
	 */
	private void showDateTimePicker() {
		String matterTime = eventDateTextView.getText().toString().trim();
		Date date =null;
		try {
			date = Constant.appDateFormat.parse(matterTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar mycalendar=Calendar.getInstance(Locale.ENGLISH);
        mycalendar.setTime(date);
        int year=mycalendar.get(Calendar.YEAR); 
        int month=mycalendar.get(Calendar.MONTH);
        int day=mycalendar.get(Calendar.DAY_OF_MONTH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        	new DatePickerDialog(ALAddEditEventActivity.this, android.R.style.Theme_Material_Light_Dialog ,new OnDateSetListener() {
    			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    				Calendar calendar = Calendar.getInstance();
    				calendar.set(year,monthOfYear, dayOfMonth);
    				Date date = calendar.getTime();
    				eventDateTextView.setText(Constant.appDateFormat.format(date));
    			};
    		}, year, month, day).show();
        }else{
        	// DatePickerDialog style
        	new DatePickerDialog(ALAddEditEventActivity.this, DatePickerDialog.THEME_HOLO_LIGHT ,new OnDateSetListener() {
    			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    				Calendar calendar = Calendar.getInstance();
    				calendar.set(year,monthOfYear, dayOfMonth);
    				Date date = calendar.getTime();
    				eventDateTextView.setText(Constant.appDateFormat.format(date));
    			};
    		}, year, month, day).show();
        }
	}
	
	/**
     * 去评分dialog
     * 
     * @param context
     */
	private void showRateGooglePlayDialog(final Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			AlertDialog.Builder builder= new Builder(context , android.R.style.Theme_Material_Light_Dialog);
			
			builder.setMessage(context.getString(R.string.w_want_to_get_more));
			builder.setTitle(context.getString(R.string.w_free_now));
			builder.setPositiveButton(context.getString(R.string.w_go_google_play),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							UmengClick.OnClick(ALAddEditEventActivity.this, KEY.GO_RATE);
							String str = "market://details?id="+Constant.PACKAGE_NAME;
							try {
								ALAddEditEventActivity.this.getSharedPreferences("review",Context.MODE_PRIVATE).edit().putBoolean("isReview", true).commit();
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setData(Uri.parse(str));
								startActivity(intent);
							} catch (Exception e) {
							}
							dialog.dismiss();
						}
					});

			builder.setNegativeButton(context.getString(R.string.w_give_up),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}else{
			final AlertBuilder dialogBuilder = AlertBuilder.getInstance(context);
			dialogBuilder
			.withTitle(context.getString(R.string.w_free_now))
			.withMessage(context.getString(R.string.w_want_to_get_more))
			.isCancelableOnTouchOutside(true) 
			.withDuration(300) 
			.withEffect(Effectstype.SlideBottom) 
			.withOkButtonText(context.getString(R.string.w_go_google_play)) 
			.withCancelButtonText(context.getString(R.string.w_give_up))
			.setOnOkButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					UmengClick.OnClick(ALAddEditEventActivity.this, KEY.GO_RATE);
					String str = "market://details?id="+Constant.PACKAGE_NAME;
					try {
						ALAddEditEventActivity.this.getSharedPreferences("review",Context.MODE_PRIVATE).edit().putBoolean("isReview", true).commit();
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(str));
						startActivity(intent);
					} catch (Exception e) {
					}
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
	 * 删除Event Dialog
	 * 
	 * @param context
	 */
	private void showRemoveEventDialog(final Context context) {
		stopVoice();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			AlertDialog.Builder builder= new Builder(context , android.R.style.Theme_Material_Light_Dialog);
			builder.setMessage(R.string.al_delete_event_content);
			builder.setTitle(R.string.AddActivity_delete);
			builder.setPositiveButton(R.string.al_delete_btn,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							matterService.deleteMatterById(ALAddEditEventActivity.this, matterID);finish();
							AManager.getAppManager().finishActivity(ALDetailActivity.class);
						}
					});

			builder.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}else{
			final AlertBuilder dialogBuilder = AlertBuilder.getInstance(context);
			dialogBuilder
			.withTitle(context.getString(R.string.AddActivity_delete))
			.isCancelableOnTouchOutside(true) 
			.withDuration(300) 
			.withEffect(Effectstype.SlideBottom) 
			.withOkButtonText(context.getString(R.string.al_delete_btn)) 
			.withCancelButtonText(context.getString(R.string.cancel))
			.setOnOkButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					matterService.deleteMatterById(ALAddEditEventActivity.this, matterID);finish();
					AManager.getAppManager().finishActivity(ALDetailActivity.class);
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
	 * 删除Event Dialog
	 * 
	 * @param context
	 */
	private void showVoiceMemoDialog(final Context context) {
		stopVoice();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			AlertDialog.Builder builder= new Builder(context , android.R.style.Theme_Material_Light_Dialog);
			builder.setMessage(R.string.al_delete_memo_content);
			builder.setTitle(R.string.al_delete_memo_title);
			builder.setPositiveButton(R.string.al_delete_btn,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							globalMatter.setVideoName("");
							checkVoice();
						}
					});
			builder.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}else{
			final AlertBuilder dialogBuilder = AlertBuilder.getInstance(context);
			dialogBuilder
			.withTitle(context.getString(R.string.al_delete_memo_title))
			.isCancelableOnTouchOutside(true) 
			.withDuration(300) 
			.withEffect(Effectstype.SlideBottom) 
			.withOkButtonText(context.getString(R.string.al_delete_btn)) 
			.withCancelButtonText(context.getString(R.string.cancel))
			.setOnOkButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					globalMatter.setVideoName("");
					checkVoice();
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
	 * 选择图片方式Dialog
	 * 
	 * @param context
	 */
	public void selectPhotoDialog(final Context context){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			AlertDialog.Builder builder= new Builder(context , android.R.style.Theme_Material_Light_Dialog);
			builder.setTitle(R.string.wallpaper_title);
			String[] provinces = new String[] {context.getString(R.string.wallpaper_cameraroll) , context.getString(R.string.wallpaper_takephoto)};
			builder.setItems(provinces, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case WALLPAPER:
						Intent intent = new Intent();
						intent.setClass(ALAddEditEventActivity.this, ALCameraRollActivity.class);
						startActivity(intent);
						UmengClick.OnClick(ALAddEditEventActivity.this, KEY.BACKGROUND_WALLPAPER);
						break;
					case CAMERA:
						Intent cIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						File file = new File(Constant.DREAMDAY_PIC);
						if (!file.exists()) {
							file.mkdirs();
						}
						cIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(file, photoFileName + ".jpg")));
						startActivityForResult(cIntent, PHOTO_CAMERA);
						UmengClick.OnClick(ALAddEditEventActivity.this, KEY.BACKGROUND_TAKE_A_PHOTO);
						break;
					}
				}
			});
			builder.create().show();
		}else{
			final AlertBuilder dialogBuilder = AlertBuilder.getInstance(context);
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_list_layout, null);
			TextView alertTitle = (TextView)view.findViewById(R.id.alertTitle);
			Button button1 = (Button)view.findViewById(R.id.button1);
			Button button2 = (Button)view.findViewById(R.id.button2);
			
			alertTitle.setTypeface(BaseApplication.typeface_heavy);
			button1.setTypeface(BaseApplication.typeface_roman);
			button2.setTypeface(BaseApplication.typeface_roman);
			
			alertTitle.setText(R.string.wallpaper_title);
			button1.setText(R.string.wallpaper_cameraroll);
			button2.setText(R.string.wallpaper_takephoto);
			button1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(ALAddEditEventActivity.this, ALCameraRollActivity.class);
					startActivity(intent);
					UmengClick.OnClick(ALAddEditEventActivity.this, KEY.BACKGROUND_WALLPAPER);
					dialogBuilder.cancel();
				}
			});
			button2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent cIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file = new File(Constant.DREAMDAY_PIC);
					if (!file.exists()) {
						file.mkdirs();
					}
					cIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(file, photoFileName + ".jpg")));
					startActivityForResult(cIntent, PHOTO_CAMERA);
					UmengClick.OnClick(ALAddEditEventActivity.this, KEY.BACKGROUND_TAKE_A_PHOTO);
					dialogBuilder.cancel();
				}
			});
			dialogBuilder.setCustomView(view, context)
			.isCancelableOnTouchOutside(true) 
			.withDuration(300) 
			.show();
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			float[] values = event.values;
			if(values[0]<15){
				setScreenBrightness(1,this);
				audioManager.setMode(AudioManager.MODE_IN_CALL); 
			}else{
				setScreenBrightness(255,this);
				audioManager.setMode(AudioManager.MODE_NORMAL); 
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
	
	/**
	 * 保存当前的屏幕亮度值，并使之生效
	 */
	private void setScreenBrightness(int paramInt ,Activity mContext) {
		Window localWindow = mContext.getWindow();
		WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
		float f = paramInt / 255.0F;
		localLayoutParams.screenBrightness = f;
		localWindow.setAttributes(localLayoutParams);
	}
	
	
	
}
