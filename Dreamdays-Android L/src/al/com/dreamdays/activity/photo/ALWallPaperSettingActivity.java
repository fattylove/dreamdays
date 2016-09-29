package al.com.dreamdays.activity.photo;

import java.io.File;
import java.util.Date;

import al.com.dreamdays.activity.ALAddEditEventActivity;
import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.base.KEY;
import al.com.dreamdays.utils.ImageUtil;
import al.com.dreamdays.utils.UmengClick;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;
import com.api.alertview.AlertBuilder;
import com.api.alertview.Effectstype;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class ALWallPaperSettingActivity extends BaseActivity implements
		OnClickListener {

	private ImageView bgImageView;
	private Button addbg_return_btn;
	private Button addbg_ok_btn;
	private ProgressBar doneProgressBar;

	private String picName;
	private Bitmap bgBitmap;

	private IabHelper mHelper;
	
	private boolean isBuy = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme();
		setContentView(R.layout.fatty_wall_paper_settings_layout);
		hiddenView(findViewById(R.id.notifyBarTextView));

		bgImageView = (ImageView) this.findViewById(R.id.bgImageView);
		addbg_return_btn = (Button) this.findViewById(R.id.addbg_return_btn);
		addbg_ok_btn = (Button) this.findViewById(R.id.addbg_ok_btn);
		doneProgressBar = (ProgressBar)this.findViewById(R.id.doneProgressBar);
		addbg_return_btn.setOnClickListener(this);
		addbg_ok_btn.setOnClickListener(this);
		
		((TextView)findViewById(R.id.titleTextView)).setTypeface(BaseApplication.typeface_heavy);
		addbg_ok_btn.setTypeface(BaseApplication.typeface_heavy);

		String imgName = getIntent().getStringExtra("imgName");
		imgName = imgName.replace('s', 'b');

		//图片放在了本地 ， assects下的imgs改成img  ,  s替换成b
		if (!TextUtils.isEmpty(imgName)) {
			picName = Constant.DREAMDAY_PIC + new Date(System.currentTimeMillis()).getTime() + ".jpg";
			if(!new File(Constant.DREAMDAY_PIC).exists()){
				new File(Constant.DREAMDAY_PIC).mkdirs();
			}
			if(ImageUtil.copyImageToSdrcardFromAssest(this, picName, imgName)){
				bgBitmap = ImageUtil.loadAssetsDrawable(ALWallPaperSettingActivity.this ,imgName);
				bgImageView.setImageBitmap(bgBitmap);
			}
		}
		
		//检查是否有Google Service
		if(!checkGooglePlayServicesAvailable(this)){
			Toast.makeText(this, "Google Play Service unavailable.", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
//		isBuy = this.getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isBuy", false);
//		if(!isBuy){
		
		// Update Version
		//Google Iap
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkLtQ4+GGiIAy0Q0M89BRO10iVbVrSbzSHfnqsQX+SMlMLirWMnUorOn8sSeoBmfcNmRH4CxyChkqDUSOiIoOtjpVbi+Ft0kYSfdTMBXNvMYsazSelVhb3jcjKXRlgb9ghk4BzU/+m6HzERpAEh3pUlQaZpSOub5Acj4fXLEQOMGmMbfdr2A3EXZUzl/79+Qwbbi4nWsHuHYQbhZqZnxTu/VpR/waSKl1mGww3zfrwFb9nS7Bm7jCUA8mO3gg8W8vaIt0l78hfFBKtiVV/c456fMH9u1zUcPvIy1g9iMYDwhkhq9OdBdcSNB8XrHk5akyzTyk/TBOdZo6RhrGB9T6NQIDAQAB";
		mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
			public void onIabSetupFinished(IabResult result) {
            	if (mHelper == null) {
                	return;
                }
                if (!result.isSuccess()) {
                    return;
                }
                if(result.isFailure()){
                }
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
		
//		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addbg_return_btn:
			finish();
			break;

		case R.id.addbg_ok_btn:
			
			addbg_ok_btn.setVisibility(View.GONE);
			doneProgressBar.setVisibility(View.VISIBLE);
			
			isBuy = this.getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isBuy", false);
			if(isBuy){
				Intent intent = new Intent();
				intent.putExtra("picName", picName);
				intent.setAction(ALAddEditEventActivity.ADD_EDIT_ACTION);
				ALWallPaperSettingActivity.this.sendBroadcast(intent);
				
				Intent otherIntent = new Intent();
				otherIntent.setAction(ALCameraRollActivity.ACTION);
				ALWallPaperSettingActivity.this.sendBroadcast(otherIntent);
				
				doneProgressBar.setVisibility(View.GONE);
				addbg_ok_btn.setVisibility(View.VISIBLE);
				
				finish();
			}else{
				if (!mHelper.subscriptionsSupported()) {
		            return;
		        }
				mHelper.launchPurchaseFlow(this, "wallpaper" , 10001,  mPurchaseFinishedListener, "wallpaper");
			}
			UmengClick.OnClick(ALWallPaperSettingActivity.this, KEY.BUY_WALLPAPER);
				
			break;
		}
	}

	/**
	 * Iap finished listener
	 */
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
	        
			doneProgressBar.setVisibility(View.GONE);
			addbg_ok_btn.setVisibility(View.VISIBLE);
			
	        //已经购买
	        if(result.getResponse()  == IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED){
	        	Intent intent = new Intent();
				intent.putExtra("picName", picName);
				intent.setAction(ALAddEditEventActivity.ADD_EDIT_ACTION);
				ALWallPaperSettingActivity.this.sendBroadcast(intent);
				
				Intent otherIntent = new Intent();
				otherIntent.setAction(ALCameraRollActivity.ACTION);
				ALWallPaperSettingActivity.this.sendBroadcast(otherIntent);
				
				finish();
				return;
	        }
	        
	        if (result.isFailure()) {
	        	showRateGooglePlayDialog(ALWallPaperSettingActivity.this);
	            return;
	        }
	        
	        if(result.isSuccess()){
	        	ALWallPaperSettingActivity.this.getSharedPreferences("fatty", Context.MODE_PRIVATE).edit().putBoolean("isBuy", true).commit();
		        return;
	        }
	    }
	};

	/**
	 * Iap listener
	 */
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		@Override
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			if (mHelper == null)
				return;
			if (result.isFailure()) {
				return;
			}
		}
	};

/******************************************************************************************************/
    /**
     * 去评分
     * 
     * @param context
     */
	public void showRateGooglePlayDialog(final Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			AlertDialog.Builder builder= new Builder(context , android.R.style.Theme_Material_Light_Dialog);
			builder.setMessage(context.getString(R.string.w_want_to_get));
			builder.setTitle(context.getString(R.string.w_free_now));
			builder.setPositiveButton(context.getString(R.string.w_go_google_play),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							UmengClick.OnClick(ALWallPaperSettingActivity.this, KEY.GO_RATE);
							String str = "market://details?id="+Constant.PACKAGE_NAME;
							try {
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
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}else{
			final AlertBuilder dialogBuilder = AlertBuilder.getInstance(context);
			Effectstype effect = Effectstype.SlideBottom;
			dialogBuilder
			.withTitle(context.getString(R.string.w_free_now))
			.withMessage(context.getString(R.string.w_want_to_get))
			.isCancelableOnTouchOutside(true) 
			.withDuration(300) 
			.withEffect(effect) 
			.withOkButtonText(context.getString(R.string.w_go_google_play)) 
			.withCancelButtonText(context.getString(R.string.w_give_up))
			.setOnOkButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					UmengClick.OnClick(ALWallPaperSettingActivity.this, KEY.GO_RATE);
					String str = "market://details?id="+Constant.PACKAGE_NAME;
					try {
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
	 * 评分Congatulations Dialog
	 * 
	 * @param context
	 */
	void showRateAlert(final Context context) {
		doneProgressBar.setVisibility(View.GONE);
		addbg_ok_btn.setVisibility(View.VISIBLE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			AlertDialog.Builder builder= new Builder(context , android.R.style.Theme_Material_Light_Dialog);
			builder.setMessage(context.getString(R.string.w_congatulations));
			builder.setPositiveButton(context.getString(R.string.w_go),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ALWallPaperSettingActivity.this.getSharedPreferences("fatty", Context.MODE_PRIVATE).edit().putBoolean("isBuy", true).commit();
							dialog.dismiss();
						}
					});
			builder.create().show();
		}else{
			final AlertBuilder dialogBuilder = AlertBuilder.getInstance(context);
			Effectstype effect = Effectstype.SlideBottom;
			dialogBuilder
			.withTitle(context.getString(R.string.w_congatulations))
			.isCancelableOnTouchOutside(true) 
			.withDuration(300) 
			.withEffect(effect) 
			.withOkButtonText(context.getString(R.string.w_go)) 
			.setOnOkButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ALWallPaperSettingActivity.this.getSharedPreferences("fatty", Context.MODE_PRIVATE).edit().putBoolean("isBuy", true).commit();
					dialogBuilder.cancel();
				}
			}).show();
		}
		
    }
	
/******************************************************************************************************/
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bgBitmap != null && !bgBitmap.isRecycled()) {
			bgBitmap.recycle();
			bgBitmap = null;
		}
	}
	
	long start = 0;
	long end = 0;
	@Override
	protected void onStop() {
		super.onStop();
		start = System.currentTimeMillis();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		end = System.currentTimeMillis();
		if((end-start) >= 5000){
			showRateAlert(ALWallPaperSettingActivity.this);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mHelper == null)
			return;
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		} else {
		}
	}
	
	
	/**
	 * check google service
	 * 
	 * @param context
	 * @return
	 */
	boolean checkGooglePlayServicesAvailable(Context context) {
		final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
			return false;
		}
		return true;
	}

}
