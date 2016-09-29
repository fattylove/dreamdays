package al.com.dreamdays.activity;

import java.io.File;

import al.com.dreamdays.activity.lock.ALPasscodeUnlockActivity;
import al.com.dreamdays.activity.lock.ALPasswordNewPasscodeActivity;
import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.base.KEY;
import al.com.dreamdays.utils.UmengClick;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.api.alertview.AlertBuilder;
import com.api.alertview.Effectstype;
import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class ALSettingActivity extends BaseActivity implements OnClickListener{

	private static final int ENABLE_PASSLOCK = 0;
	private static final int DISABLE_PASSLOCK = 1;
	private SharedPreferences ifSortByTime;
	
	private boolean isLogin;
	private boolean isLite ;
	private boolean isOpen;
	
	private TextView titleTextView;
	private ImageButton exitImageButton;
	private RelativeLayout settingCalendarLayout ,settingRestoreLayout ,settingRateLayout ,settingFeedbackLayout ,settingFullVersionLayout;
	private TextView settingSortTextView ,settingSyncDataTextView,settingPinLockTextView ,settingCalendarTextView ,settingRestoreTextView ,settingRateTextView ,settingFeedbackTextView;
	
	private CheckBox settingSyncDataCheckbox , settingPinLockCheckbox ,settingSortCheckbox;
	
	private LinearLayout settingSortCheckboxLayout,settingSyncDataCheckboxLayout ,settingPinLockCheckboxLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme();
		setContentView(R.layout.al_dreamdays_setting_layout);
		hiddenView(findViewById(R.id.notifyBarTextView));
		
		ifSortByTime = getSharedPreferences("IF_SORTBYTIME", Context.MODE_PRIVATE);
		isLite = getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isLite", true);
		isLogin = getSharedPreferences("Login", Context.MODE_PRIVATE).getBoolean("isLogin", false);
		isOpen = getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isOpen", true);
		
		settingSortTextView = (TextView)this.findViewById(R.id.settingSortTextView);
		settingSyncDataTextView = (TextView)this.findViewById(R.id.settingSyncDataTextView);
		settingPinLockTextView = (TextView)this.findViewById(R.id.settingPinLockTextView);
		settingCalendarTextView = (TextView)this.findViewById(R.id.settingCalendarTextView);
		settingRestoreTextView = (TextView)this.findViewById(R.id.settingRestoreTextView);
		settingRateTextView = (TextView)this.findViewById(R.id.settingRateTextView);
		settingFeedbackTextView = (TextView)this.findViewById(R.id.settingFeedbackTextView);
		
		titleTextView = (TextView)this.findViewById(R.id.titleTextView);
		titleTextView.setTypeface(BaseApplication.typeface_heavy);
		
		//设置字体
		settingSortTextView.setTypeface(BaseApplication.typeface_medium);
		settingSyncDataTextView.setTypeface(BaseApplication.typeface_medium);
		settingPinLockTextView.setTypeface(BaseApplication.typeface_medium);
		settingCalendarTextView.setTypeface(BaseApplication.typeface_medium);
		settingRestoreTextView.setTypeface(BaseApplication.typeface_medium);
		settingRateTextView.setTypeface(BaseApplication.typeface_medium);
		settingFeedbackTextView.setTypeface(BaseApplication.typeface_medium);
		
		settingCalendarLayout = (RelativeLayout)this.findViewById(R.id.settingCalendarLayout);
		settingRestoreLayout = (RelativeLayout)this.findViewById(R.id.settingRestoreLayout);
		settingRateLayout = (RelativeLayout)this.findViewById(R.id.settingRateLayout);
		settingFeedbackLayout = (RelativeLayout)this.findViewById(R.id.settingFeedbackLayout);
		settingFullVersionLayout = (RelativeLayout)this.findViewById(R.id.settingFullVersionLayout);
		
		if(isLite){
			if(isOpen){
				settingFullVersionLayout.setVisibility(View.VISIBLE);
			}else{
				settingFullVersionLayout.setVisibility(View.GONE);
			}
		}else{
			settingFullVersionLayout.setVisibility(View.GONE);
		}
		
		settingFullVersionLayout.setOnClickListener(this);
		settingCalendarLayout.setOnClickListener(this);
		settingRestoreLayout.setOnClickListener(this);
		settingRateLayout.setOnClickListener(this);
		settingFeedbackLayout.setOnClickListener(this);
		
		settingSortCheckbox = (CheckBox)this.findViewById(R.id.settingSortCheckbox);
		settingSyncDataCheckbox = (CheckBox)this.findViewById(R.id.settingSyncDataCheckbox);
		settingPinLockCheckbox = (CheckBox)this.findViewById(R.id.settingPinLockCheckbox);
		
		settingSortCheckboxLayout = (LinearLayout)this.findViewById(R.id.settingSortCheckboxLayout);
		settingSyncDataCheckboxLayout = (LinearLayout)this.findViewById(R.id.settingSyncDataCheckboxLayout);
		settingPinLockCheckboxLayout = (LinearLayout)this.findViewById(R.id.settingPinLockCheckboxLayout);
		settingSortCheckboxLayout.setOnClickListener(this);
		settingSyncDataCheckboxLayout.setOnClickListener(this);
		settingPinLockCheckboxLayout.setOnClickListener(this);
		
			//主页列表排序
			if (ifSortByTime.getBoolean("ifSortByTime", true)) {
				settingSortCheckbox.setChecked(true);
			} else {
				settingSortCheckbox.setChecked(false);
			}
			settingSortCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						ifSortByTime.edit().putBoolean("ifSortByTime", true).commit();
					} else {
						ifSortByTime.edit().putBoolean("ifSortByTime", false).commit();
					}
				}
			});
			
			//数据同步开关
			settingSyncDataCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					isLogin = ALSettingActivity.this.getSharedPreferences("Login", Context.MODE_PRIVATE).getBoolean("isLogin", false);
					if(isLogin){
						if (!isChecked) {
							ALSettingActivity.this.getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putBoolean("isLogin", false).commit();
							ALSettingActivity.this.getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putString("passcode", "").commit();
							if(ALSettingActivity.this.getSharedPreferences("fatty_time", Context.MODE_PRIVATE).contains("backupTime")){
								ALSettingActivity.this.getSharedPreferences("fatty_time", Context.MODE_PRIVATE).edit().remove("backupTime").commit();
							}
						}
					}else{
						if (isChecked) {
							Intent intent = new Intent();
							intent.setClass(ALSettingActivity.this, ALSignInUpActivity.class);
							ALSettingActivity.this.startActivity(intent);
						}
					}
					UmengClick.OnClick(ALSettingActivity.this, KEY.BACKUP);
				}
			});
			
			//passcode 开关
			if(!TextUtils.isEmpty(getSharedPreferences("PASSCODE_INFO", 0).getString("passcode", ""))){
				settingPinLockCheckbox.setChecked(true);
			}else{
				settingPinLockCheckbox.setChecked(false);
			}
			settingPinLockCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isOpen){//是否限免
						if (isChecked) {
							if(isLite){
								showGooglePlayDialog(ALSettingActivity.this);
							}else{
								if(TextUtils.isEmpty(getSharedPreferences("PASSCODE_INFO", 0).getString("passcode", ""))){
									Intent i = new Intent(ALSettingActivity.this, ALPasswordNewPasscodeActivity.class);
									i.putExtra("type", ENABLE_PASSLOCK);
									i.putExtra("message", getString(R.string.passcode_enter_passcode));
									startActivityForResult(i, ENABLE_PASSLOCK);
								}
							}
						} else {
							if(!isLite){
								if(!TextUtils.isEmpty(getSharedPreferences("PASSCODE_INFO", 0).getString("passcode", ""))){
									Intent i = new Intent(ALSettingActivity.this, ALPasscodeUnlockActivity.class);
									i.putExtra("type", DISABLE_PASSLOCK);
									i.putExtra("message", getString(R.string.passcode_enter_passcode));
									startActivityForResult(i, DISABLE_PASSLOCK);
								}
							}
						}
					}else{
						if (isChecked) {
							if(TextUtils.isEmpty(getSharedPreferences("PASSCODE_INFO", 0).getString("passcode", ""))){
								Intent i = new Intent(ALSettingActivity.this, ALPasswordNewPasscodeActivity.class);
								i.putExtra("type", ENABLE_PASSLOCK);
								i.putExtra("message", getString(R.string.passcode_enter_passcode));
								startActivityForResult(i, ENABLE_PASSLOCK);
							}
						} else {
							if(!TextUtils.isEmpty(getSharedPreferences("PASSCODE_INFO", 0).getString("passcode", ""))){
								Intent i = new Intent(ALSettingActivity.this, ALPasscodeUnlockActivity.class);
								i.putExtra("type", DISABLE_PASSLOCK);
								i.putExtra("message", getString(R.string.passcode_enter_passcode));
								startActivityForResult(i, DISABLE_PASSLOCK);
							}
						}
					}
					UmengClick.OnClick(ALSettingActivity.this, KEY.PASSCODE);
				}
			});
		
		
		
		exitImageButton = (ImageButton)this.findViewById(R.id.exitImageButton);
		exitImageButton.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		isLogin = getSharedPreferences("Login", Context.MODE_PRIVATE).getBoolean("isLogin", false);
		
			if(isLogin){
				settingSyncDataCheckbox.setChecked(true);
			}else{
				settingSyncDataCheckbox.setChecked(false);
			}
			
			if(!TextUtils.isEmpty(getSharedPreferences("PASSCODE_INFO", 0).getString("passcode", ""))){
				settingPinLockCheckbox.setChecked(true);
			}else{
				settingPinLockCheckbox.setChecked(false);
			}
	
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.settingSortCheckboxLayout:
				if(settingSortCheckbox.isChecked()){
					settingSortCheckbox.setChecked(false);
				}else{
					settingSortCheckbox.setChecked(true);
				}
			break;
		case R.id.settingSyncDataCheckboxLayout:
				if(settingSyncDataCheckbox.isChecked()){
					settingSyncDataCheckbox.setChecked(false);
				}else{
					settingSyncDataCheckbox.setChecked(true);
				}
			break;
		case R.id.settingPinLockCheckboxLayout:
				if(settingPinLockCheckbox.isChecked()){
					settingPinLockCheckbox.setChecked(false);
				}else{
					settingPinLockCheckbox.setChecked(true);
				}
			break;
		case R.id.exitImageButton:
			finish();
			break;
		case R.id.settingCalendarLayout:
			 share();
			break;
		case R.id.settingFeedbackLayout:
			feedback();
			break;
		case R.id.settingRestoreLayout:
			backup();
			break;
		case R.id.settingRateLayout:
			rate();
			break;
		case R.id.settingFullVersionLayout:
			fullversion();
			break;
		default:
			break;  
		}
	}
	
	/**
	 * 跳转google play对话框
	 */
	private void showGooglePlayDialog(final Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			AlertDialog.Builder builder= new Builder(context , android.R.style.Theme_Material_Light_Dialog);
			builder.setMessage(context.getString(R.string.google_buy_content_passcode));
			builder.setTitle(context.getString(R.string.upgrade));
			builder.setPositiveButton(context.getString(R.string.upgrade),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							try {
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setData(Uri.parse(Constant.GO_GOOGLE_PLAY_BUY_DREAMDAYS));
								context.startActivity(intent);
							} catch (Exception e) {
								Toast.makeText(ALSettingActivity.this, R.string.no_found_markets, Toast.LENGTH_SHORT).show();
							}
								if(settingPinLockCheckbox.isChecked()){
									settingPinLockCheckbox.setChecked(false);
								}
							dialog.dismiss();
						}
					});
			builder.setNegativeButton(context.getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
								if(settingPinLockCheckbox.isChecked()){
									settingPinLockCheckbox.setChecked(false);
								}
							dialog.dismiss();
						}
					});
			builder.create().show();
		}else{
			final AlertBuilder dialogBuilder = AlertBuilder.getInstance(context);
			Effectstype effect = Effectstype.SlideBottom;
			dialogBuilder
			.withTitle(context.getString(R.string.upgrade))
			.withMessage(context.getString(R.string.google_buy_content_passcode))
			.isCancelableOnTouchOutside(true) 
			.withDuration(300) 
			.withEffect(effect) 
			.withOkButtonText(context.getString(R.string.upgrade)) 
			.withCancelButtonText(context.getString(R.string.cancel))
			.setOnOkButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(Constant.GO_GOOGLE_PLAY_BUY_DREAMDAYS));
						context.startActivity(intent);
					} catch (Exception e) {
						Toast.makeText(ALSettingActivity.this, R.string.no_found_markets, Toast.LENGTH_SHORT).show();
					}
						if(settingPinLockCheckbox.isChecked()){
							settingPinLockCheckbox.setChecked(false);
						}
					dialogBuilder.cancel();
				}
			}).setOnCacnelButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
						if(settingPinLockCheckbox.isChecked()){
							settingPinLockCheckbox.setChecked(false);
						}
					dialogBuilder.cancel();
				}
			}).show();
		}
		
	}
	
	/**
	 * Dreamdays分享
	 */
	private void share(){
		Intent intent2 = new Intent(Intent.ACTION_SEND);
		File imageFile = new File(Constant.SHAREIMAGE);
		if (null != imageFile && imageFile.exists() && imageFile.isFile()) {
			intent2.setType("image/*");
			Uri u = Uri.fromFile(imageFile);
			intent2.putExtra(Intent.EXTRA_STREAM, u);
		} else {
			intent2.setType("text/plain"); // 纯文本
		}
		intent2.putExtra(Intent.EXTRA_SUBJECT,  "Dreamdays http://bit.ly/1kDHyce");
		intent2.putExtra(Intent.EXTRA_TEXT,     "Dreamdays http://bit.ly/1kDHyce");
		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent2,getString(R.string.MenuFragment_share_dreamdays)));
		UmengClick.OnClick(ALSettingActivity.this ,KEY.SHARE_DREAMDAYS);
	}
	
	/**
	 * 评价
	 */
	private void rate(){
		String str = "market://details?id="+Constant.PACKAGE_NAME;
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(str));
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(this, R.string.no_found_markets, Toast.LENGTH_SHORT).show();
		}
		UmengClick.OnClick(this, KEY.RATE_AND_REVIEW);
	}
	
	/**
	 * full version
	 */
	private void fullversion(){
		String str = "market://details?id=com.guxiu.dreamdays";
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(str));
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(this, R.string.no_found_markets, Toast.LENGTH_SHORT).show();
		}
		UmengClick.OnClick(this, KEY.UPGRADE_TO_FULL_VERSION);
	}

	/**
	 * 反馈
	 */
	private void feedback(){
		Intent email = new Intent(android.content.Intent.ACTION_SEND);
		email.setType("plain/text");
		String[] emailReciver = new String[] { "Feedback@guxiu.ca" };
		String[] emailCReciver = new String[] { "hi@guxiu.ca" };
		String emailSubject;
		if(Constant.PACKAGE_NAME.equals("com.guxiu.dreamdays")){
			emailSubject = getString(R.string.app_feedback_title) +" L";
		}else{
			emailSubject = getString(R.string.app_feedback_title) +" L";
		}
		email.putExtra(android.content.Intent.EXTRA_EMAIL,   emailReciver);
		email.putExtra(android.content.Intent.EXTRA_CC,      emailCReciver);
		email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);
		startActivity(Intent.createChooser(email, getString(R.string.app_choice_email_title)));
		UmengClick.OnClick(this, KEY.SEND_FEEDBACK);
	}

	/**
	 * 备份
	 */
	private void backup(){
		isLogin = getSharedPreferences("Login", Context.MODE_PRIVATE).getBoolean("isLogin", false);
		if(!isLogin){
			Intent intent = new Intent();
			intent.setClass(this, ALSignInUpActivity.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent();
			intent.setClass(this, ALRestoreTimeActivity.class);
			startActivity(intent);
		}
		UmengClick.OnClick(this, KEY.RESTORE_FROM_BACKUP);
	}
}
