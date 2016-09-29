package al.com.dreamdays.fragment;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import al.com.dreamdays.activity.ALHomeActivity;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.sqlite.Matter;
import al.com.dreamdays.utils.DateUtil;
import al.com.dreamdays.utils.FileUtil;
import al.com.dreamdays.utils.ImageUtil;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class DetailFragment extends Fragment implements OnClickListener , SensorEventListener{
	
	// 录音播放状态
	private static final int RECORD_PLAYING = 1;
	private static final int RECORD_STOPED = 0;
	public static final int REQ_CODE_RECORDING = 3;

	private int recordStatus = 0;//录音播放状态
	private MediaPlayer mMediaPlayer;
	
	private TextView topNameTextView ,topDaysTextView;
	private ImageView topPointImageView ,bottomPointImageView ,centerImageView;
	private ImageButton playAudioImageButton;
	private TextView centerLine1 ,centerLine2 ;
	
	private RelativeLayout dayNumsLayout;
	private LinearLayout   dayYMDsLayout;
	
	//adapter Center View line
	private TextView c_clickYearTextView,c_yyTagTextView,c_clickMonthTextView ,c_mmTagTextView ,c_clickDayTextView ,c_ddTagTextView;
	private LinearLayout bottomViewLayout;
	
	//计算year  month day
	private long matterDateLong =0L;
	private int years;
	private int months;
	private int days;
	
	//文本
	private String yearsText;
	private String monthsText;
	private String daysText;
	
	private Matter matter;
	private Activity mContext;
	private Bitmap bgBitmap;
	
	private boolean isClick ;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	public void setMatter(Matter matter){
		this.matter = matter;
	}
	
	private SensorManager sensorManager ;
	private Sensor lightSensor ;
	private AudioManager audioManager;  
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
		preferences = mContext.getSharedPreferences("CLICK", Context.MODE_PRIVATE);
		editor = preferences.edit();
		
		sensorManager = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
		lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		
		audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);  
	}
	
	@Override
	public void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this, lightSensor);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.al_dreamdays_detail_center_layout, container, false);
	    topDaysTextView = (TextView)rootView.findViewById(R.id.topDaysTextView);
	    topPointImageView = (ImageView)rootView.findViewById(R.id.topPointImageView);
	    bottomPointImageView = (ImageView)rootView.findViewById(R.id.bottomPointImageView);
	    centerImageView = (ImageView)rootView.findViewById(R.id.centerImageView);
	    topNameTextView = (TextView)rootView.findViewById(R.id.topNameTextView);
	    playAudioImageButton = (ImageButton)rootView.findViewById(R.id.playAudioImageButton);
	    centerLine1 = (TextView)rootView.findViewById(R.id.centerLine1);
	    centerLine2 = (TextView)rootView.findViewById(R.id.centerLine2);
	    
	    bottomViewLayout = (LinearLayout)rootView.findViewById(R.id.bottomViewLayout);
	    dayNumsLayout= (RelativeLayout)rootView.findViewById(R.id.dayNumsLayout);
	    dayYMDsLayout = (LinearLayout)rootView.findViewById(R.id.dayYMDsLayout);
	    dayNumsLayout.setOnClickListener(this);
	    dayYMDsLayout.setOnClickListener(this);
	    bottomViewLayout.setOnClickListener(this);
	    
	    c_clickYearTextView = (TextView)rootView.findViewById(R.id.c_clickYearTextView);
	    c_clickMonthTextView = (TextView)rootView.findViewById(R.id.c_clickMonthTextView);
	    c_clickDayTextView = (TextView)rootView.findViewById(R.id.c_clickDayTextView);
	    
	    c_yyTagTextView = (TextView)rootView.findViewById(R.id.c_yyTagTextView);
	    c_mmTagTextView = (TextView)rootView.findViewById(R.id.c_mmTagTextView);
	    c_ddTagTextView = (TextView)rootView.findViewById(R.id.c_ddTagTextView);
	    
	    //set typeface
	    c_clickYearTextView.setTypeface(BaseApplication.typeface_heavy);
	    c_clickMonthTextView.setTypeface(BaseApplication.typeface_heavy);
	    c_clickDayTextView.setTypeface(BaseApplication.typeface_heavy);
	    
	    c_yyTagTextView.setTypeface(BaseApplication.typeface_heavy);
	    c_mmTagTextView.setTypeface(BaseApplication.typeface_heavy);
	    c_ddTagTextView.setTypeface(BaseApplication.typeface_heavy);
		topDaysTextView.setTypeface(BaseApplication.typeface_heavy);
		topNameTextView.setTypeface(BaseApplication.typeface_heavy);
	    
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		refreshData(matter);
	}
	
	/**
	 * 刷新数据
	 * 
	 * @param pagerMatter
	 */
	public void refreshData(final Matter pagerMatter){
		if(pagerMatter!=null){
			
			//set event name
			topNameTextView.setText(pagerMatter.getMatterName());
			//set icon
			ALHomeActivity.setWhiteIcon(centerImageView,  mContext, pagerMatter.getClassifyType());
			
			//set event date
			try {
				matterDateLong = DateUtil.getMatterDay(Constant.appDateFormat.parse(pagerMatter.getMatterTime()));
				if (matterDateLong > 0) {
					bottomPointImageView.setVisibility(View.VISIBLE);
					topPointImageView.setVisibility(View.GONE);
				} else {
					matterDateLong = Math.abs(matterDateLong);
					bottomPointImageView.setVisibility(View.GONE);
					topPointImageView.setVisibility(View.VISIBLE);
				}
				topDaysTextView.setText(matterDateLong + "");
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//set voice
			if ( !TextUtils.isEmpty(pagerMatter.getVideoName())  && FileUtil.checkFileExists(pagerMatter.getVideoName())) {
				playAudioImageButton.setVisibility(View.VISIBLE);
			} else {
				playAudioImageButton.setVisibility(View.INVISIBLE);
			}
			
			playAudioImageButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					startPlayVoice(pagerMatter.getVideoName() ,playAudioImageButton);
				}
			});
			
			/*********************        set event date UI           ********************/
			int[] datas = new int[3] ;
			
			//Matter Date
			String startDateStr = pagerMatter.getMatterTime();
			Date startDate = null;
			String matterDate = null;
			try {
				startDate = Constant.appDateFormat.parse(startDateStr);
				matterDate = Constant.globalDateFormat.format(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//nowDate
			Date nowDate = new Date();
			String nowDateStr = Constant.equalDateFormat_2.format(nowDate);
			String nowMatterDate = nowDateStr+" 00:00:00";
			
			try {
				startDate = Constant.globalDateFormat.parse(matterDate);
				nowDate = Constant.globalDateFormat.parse(nowMatterDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(startDate);
			Calendar nowCalendar = Calendar.getInstance();
			nowCalendar.setTime(nowDate);
			
			if(startDate.before(nowDate)){
				datas = DateUtil.getNeturalAge(startCalendar ,nowCalendar);
			}else if(startDate.after(nowDate)){
				datas = DateUtil.getNeturalAge(nowCalendar , startCalendar);
			}
			years = datas[0];
			months = datas[1];
			days = datas[2];
			
			//没年
			if(years == 0){
				c_clickYearTextView.setVisibility(View.GONE);
				c_yyTagTextView.setVisibility(View.GONE);
			}
			
			//有年，没月日
			if(years != 0 && months == 0 && days == 0){
				yearsText = years +"";
				monthsText = "00";
				daysText = "00";
			}
			
			//有年月，没日
			if(years != 0 && months != 0 && days == 0){
				yearsText = years +"";
				if(months<10){
					monthsText = "0" + months;
				}else{
					monthsText = "" + months;
				}
				daysText = "00";
			}
			
			//有年日，没月
			if(years != 0 && months == 0 && days != 0){
				yearsText = years +"";
				monthsText = "00";
				if(days<10){
					daysText = "0" + days;
				}else{
					daysText = days+"";
				}
			}
			
			//有年月日
			if(years != 0 && months != 0 && days != 0){
				yearsText = years +"";
				if(months<10){
					monthsText = "0" + months;
				}else{
					monthsText = "" + months;
				}
				if(days<10){
					daysText = "0" + days;
				}else{
					daysText = days+"";
				}
			}
			
			//没年，有月日
			if(years == 0  && months != 0 && days != 0){
				yearsText = "";
				monthsText = months +"";
				if(days<10){
					daysText = "0" + days;
				}else{
					daysText = days+"";
				}
			}
			
			//没年日，有月
			if(years == 0  && months != 0 && days == 0){
				yearsText = "";
				monthsText = months +"";
				daysText = "00" ;
			}
			
			//没年月，有日
			if(years == 0  && months == 0 && days != 0){
				yearsText = "";
				monthsText = "";
				daysText = days+"";
			}
			
			c_clickYearTextView.setText(yearsText);
			c_clickMonthTextView.setText(monthsText);
			c_clickDayTextView.setText(daysText);
			
			
			if(preferences.contains("isClick_"+ matter.get_id())){
				isClick = preferences.getBoolean("isClick_"+ matter.get_id(), false);
				if(isClick){
					dayYMDsLayout.setVisibility(View.VISIBLE);
					dayNumsLayout.setVisibility(View.GONE);
				}else{
					dayYMDsLayout.setVisibility(View.GONE);
					dayNumsLayout.setVisibility(View.VISIBLE);
				}
			}else{
				editor.putBoolean("isClick_"+ matter.get_id(), false);
				editor.commit();
			}

			isClick = preferences.getBoolean("isClick_"+ matter.get_id(), false);
			
			adapterLineLengthOrFontSize(isClick);
			
			//处理详情日期切换问题
			if(matterDateLong < 31){  // 31
				dayYMDsLayout.setVisibility(View.GONE);
				dayNumsLayout.setVisibility(View.VISIBLE);
				adapterLineLengthOrFontSize(false);
			}
		}
	}

	/**
	 * 开始播放
	 * 
	 * @param path
	 * @param playButton
	 */
	private void startPlayVoice(String path ,final ImageButton playButton) {
		if (FileUtil.checkFileExists(path)) {
			if (recordStatus == RECORD_STOPED) {
				playButton.setBackgroundResource(R.drawable.al_stop_white);
				if(sensorManager!=null){
					sensorManager.registerListener(this , lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
				}
				try {
					mMediaPlayer = new MediaPlayer();
					mMediaPlayer.setDataSource(path);
					mMediaPlayer.prepare();
					mMediaPlayer.start();
				} catch (Exception e) {
				}
				mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {
						playButton.setBackgroundResource(R.drawable.al_play_white);
						setScreenBrightness(255,mContext);
						stopVoice();
					}
				});
				recordStatus = RECORD_PLAYING;
			} else {
				playButton.setBackgroundResource(R.drawable.al_play_white);
				pauseVoice();
			}
		} else {
			Toast.makeText(mContext, R.string.app_record_notfound, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 停止
	 * 
	 * stop
	 */
	public void stopVoice() {
		if (null != mMediaPlayer) {
			
			mMediaPlayer.stop();
			playAudioImageButton.setBackgroundResource(R.drawable.al_play_white);
			recordStatus = RECORD_STOPED;
			
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
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(bgBitmap!=null && !bgBitmap.isRecycled()){
			bgBitmap.recycle();
			bgBitmap = null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bottomViewLayout:
			
			break;
		case R.id.dayNumsLayout:
		case R.id.dayYMDsLayout:
			if(years == 0 && months == 0 && days <= 28){
				return;
			}
		
			dayNumsLayout.setClickable(false);
			dayYMDsLayout.setClickable(false);
			dayNumsLayout.setOnClickListener(null);
			dayYMDsLayout.setOnClickListener(null);
			
			isClick = preferences.getBoolean("isClick_"+ matter.get_id(), false);
			
			//  switch view
 			show(isClick);
			
			//  adapter line length
			adapterLineLengthOrFontSize(!isClick);
			break;
		default:
			break;
		}
	}
	
	/**
	 * event 日期显示的类型
	 * 
	 * @param isClick
	 */
	public void show(boolean isClick){
		if(!isClick){
			editor.putBoolean("isClick_"+ matter.get_id(), true);
			editor.commit();
			dayNumsLayout.startAnimation(out());
			new Handler().postDelayed(new Runnable() {
				public void run() {
					dayNumsLayout.setVisibility(View.GONE);
					dayNumsLayout.setClickable(false);
					dayNumsLayout.setOnClickListener(null);
				}
			}, 200);
			new Handler().postDelayed(new Runnable() {
				public void run() {
					dayYMDsLayout.setVisibility(View.VISIBLE);
					dayYMDsLayout.startAnimation(in());
					dayYMDsLayout.setClickable(true);
					dayYMDsLayout.setOnClickListener(DetailFragment.this);
				}
			}, 200);
		}else{
			editor.putBoolean("isClick_"+ matter.get_id(), false);
			editor.commit();
			dayYMDsLayout.startAnimation(out());
			new Handler().postDelayed(new Runnable() {
				public void run() {
					dayYMDsLayout.setVisibility(View.GONE);
					dayYMDsLayout.setClickable(false);
					dayYMDsLayout.setOnClickListener(null);
				}
			}, 200);
			new Handler().postDelayed(new Runnable() {
				public void run() {
					dayNumsLayout.setVisibility(View.VISIBLE);
					dayNumsLayout.startAnimation(in());
					dayNumsLayout.setClickable(true);
					dayNumsLayout.setOnClickListener(DetailFragment.this);
				}
			}, 200);
		}
	}
	
	/**
	 * 适配线的长度
	 * 
	 * @param isClick
	 */
	public void adapterLineLengthOrFontSize(boolean isClick){
		int w = ImageUtil.dip2px(mContext, 55.0f);
		int h = ImageUtil.dip2px(mContext, 2.0f);
		int l1 = (matterDateLong + "").length();
		StringBuffer buffer = new StringBuffer();
		if(!TextUtils.isEmpty(yearsText)){
			buffer.append(yearsText);
		}
		if(!TextUtils.isEmpty(monthsText)){
			buffer.append(monthsText);
		}
		if(!TextUtils.isEmpty(daysText)){
			buffer.append(daysText);
		}
		int l2 = buffer.length();

		if (!isClick) {
			switch (l1) {
			case 1:
				w = ImageUtil.dip2px(mContext, 35.0f);
				topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 85.0f);
				break;
			case 2:
				w = ImageUtil.dip2px(mContext, 45.0f);
				topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 85.0f);
				break;
			case 3:
				w = ImageUtil.dip2px(mContext, 65.0f);
				topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 85.0f);
				break;
			case 4:
				w = ImageUtil.dip2px(mContext, 85.0f);
				topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80.0f);
				break;
			case 5:
				w = ImageUtil.dip2px(mContext, 105.0f);
				topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80.0f);
				break;
			case 6:
				w = ImageUtil.dip2px(mContext, 120.0f);
				topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80.0f);
				break;
			case 7:
				w = ImageUtil.dip2px(mContext, 145.0f);
				topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80.0f);
				break;
			default:
				w = ImageUtil.dip2px(mContext, 145.0f);
				topDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80.0f);
				break;
			}

		} else {
			switch (l2) {
			case 1:
				w = ImageUtil.dip2px(mContext, 35.0f);
				c_clickYearTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,75.0f);
				c_clickMonthTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,75.0f);
				c_clickDayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,75.0f);
				break;
			case 2:
				w = ImageUtil.dip2px(mContext, 45.0f);
				c_clickYearTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,75.0f);
				c_clickMonthTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,75.0f);
				c_clickDayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,75.0f);
				break;
			case 3:
				w = ImageUtil.dip2px(mContext, 70.0f);
				c_clickYearTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,75.0f);
				c_clickMonthTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,75.0f);
				c_clickDayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,75.0f);
				break;
			case 4:
				w = ImageUtil.dip2px(mContext, 85.0f);
				c_clickYearTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				c_clickMonthTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				c_clickDayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				break;
			case 5:
				w = ImageUtil.dip2px(mContext, 105.0f);
				c_clickYearTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				c_clickMonthTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				c_clickDayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				break;
			case 6:
				w = ImageUtil.dip2px(mContext, 125.0f);
				c_clickYearTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				c_clickMonthTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				c_clickDayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				break;
			case 7:
				w = ImageUtil.dip2px(mContext, 135.0f);
				c_clickYearTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				c_clickMonthTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				c_clickDayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				break;
			default:
				w = ImageUtil.dip2px(mContext, 135.0f);
				c_clickYearTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				c_clickMonthTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				c_clickDayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,70.0f);
				break;
			}
		}
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w,h);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(w,h);
		centerLine1.setLayoutParams(params);
		centerLine2.setLayoutParams(params2);
	}
	
	
	/**
	 * 动画出现
	 * 
	 * @return
	 */
	public Animation in(){
		Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);  
		alphaAnimation.setDuration(200);
		return alphaAnimation;
	}
	
	/**
	 * 动画隐藏
	 * 
	 * @return
	 */
	public Animation out(){
		Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);  
		alphaAnimation.setDuration(200);
		return alphaAnimation;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			float[] values = event.values;
			if(values[0]<15){
				setScreenBrightness(1 , mContext);
				audioManager.setMode(AudioManager.MODE_IN_CALL); 
			}else{
				setScreenBrightness(255 , mContext);
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
	
	@Override
	public void onDetach() {
		super.onDetach();
		stopVoice();
	}
}
