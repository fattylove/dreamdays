package al.com.dreamdays.activity.photo;

import al.com.dreamdays.activity.ALAddEditEventActivity;
import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.BaseApplication;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guxiu.dreamdays.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 
 * @author Fatty
 *
 */
public class ALLocalPhotosSettingActivity extends BaseActivity implements
		OnClickListener {

	//TODO 图片处理、裁剪
	
	private ImageView bgImageView;
	private Button addbg_return_btn;
	private Button addbg_ok_btn;
	private Bitmap bgBitmap;
	private String imgName;
	
	private ProgressBar juhua;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme();
		setContentView(R.layout.fatty_wall_paper_settings_layout);
		hiddenView(findViewById(R.id.notifyBarTextView));

		juhua = (ProgressBar)this.findViewById(R.id.juhua);
		bgImageView = (ImageView) this.findViewById(R.id.bgImageView);
		addbg_return_btn = (Button) this.findViewById(R.id.addbg_return_btn);
		addbg_ok_btn = (Button) this.findViewById(R.id.addbg_ok_btn);
		addbg_return_btn.setOnClickListener(this);
		addbg_ok_btn.setOnClickListener(this);
		
		((TextView)findViewById(R.id.titleTextView)).setTypeface(BaseApplication.typeface_heavy);
		addbg_ok_btn.setTypeface(BaseApplication.typeface_heavy);

		imgName = getIntent().getStringExtra("imgName");
		
		//TODO 图片裁剪  裁剪与屏幕一直的存储在本地
		
		if (!TextUtils.isEmpty(imgName)) {
			ImageLoader.getInstance().displayImage("file:///"+imgName, bgImageView , new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					juhua.setVisibility(View.VISIBLE);
				}
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					juhua.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					juhua.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					juhua.setVisibility(View.GONE);
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addbg_return_btn:
			finish();
			break;

		case R.id.addbg_ok_btn:
			
			Intent intent = new Intent();
			intent.putExtra("picName", imgName);
			intent.setAction(ALAddEditEventActivity.ADD_EDIT_ACTION);
			ALLocalPhotosSettingActivity.this.sendBroadcast(intent);
			
			Intent otherIntent = new Intent();
			otherIntent.setAction(ALCameraRollActivity.ACTION);
			ALLocalPhotosSettingActivity.this.sendBroadcast(otherIntent);
			
			finish();
			break;
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
		
		ImageLoader.getInstance().clearMemoryCache();
	}
}
