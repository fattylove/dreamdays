package al.com.dreamdays.activity.photo;

import java.io.File;

import al.com.dreamdays.activity.ALAddEditEventActivity;
import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.utils.ImageUtil;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class ALFixPhotoActivity extends BaseActivity implements OnClickListener {

	private ImageView centerImageView;
	private ImageView leftBtn;
	private ImageView rightBtn;
	private ImageView saveBtn;
	private ImageView cancelBtn;

	private String picUrl;

	private Bitmap bgBitmap;
	private Bitmap tempBitmap;
	private int myWidth;
	private int myHeight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fatty_fix_photo_layout);

		centerImageView = (ImageView) this.findViewById(R.id.centerImageView);
		leftBtn = (ImageView) this.findViewById(R.id.fix_left);
		leftBtn.setOnClickListener(this);
		rightBtn = (ImageView) this.findViewById(R.id.fix_right);
		rightBtn.setOnClickListener(this);
		saveBtn = (ImageView) this.findViewById(R.id.fix_save);
		saveBtn.setOnClickListener(this);
		cancelBtn = (ImageView) this.findViewById(R.id.fix_cancel);
		cancelBtn.setOnClickListener(this);

		picUrl = getIntent().getStringExtra("picUrl");
		if (!TextUtils.isEmpty(picUrl)) {
			File file = new File(picUrl);
			bgBitmap = ImageUtil.loadSdcardDrawable(file , Constant.width , Constant.height);
			tempBitmap = bgBitmap;
			centerImageView.setImageBitmap(tempBitmap);
			myWidth = tempBitmap.getWidth();
			myHeight = tempBitmap.getHeight();
		} else {

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fix_left:
			tempBitmap = rotaingImageView(-90, tempBitmap);
			break;
		case R.id.fix_right:
			tempBitmap = rotaingImageView(+90, tempBitmap);
			break;
		case R.id.fix_save:
			ImageUtil.saveBitmap(picUrl, tempBitmap);
			
			Intent intent = new Intent();
			intent.setAction(ALAddEditEventActivity.ADD_EDIT_ACTION);
			intent.putExtra("picName", picUrl);
			sendBroadcast(intent);
			
			finish();
			break;
		case R.id.fix_cancel:
			finish();
			break;
		}
	}

	private Bitmap rotaingImageView(int a, Bitmap tBitmap) {
		myWidth = tBitmap.getWidth();
		myHeight = tBitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postRotate(a);
		Bitmap resizedBitmap = Bitmap.createBitmap(tempBitmap, 0, 0, myWidth, myHeight, matrix, true);
		centerImageView.setImageBitmap(resizedBitmap);
		return resizedBitmap;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bgBitmap != null && !bgBitmap.isRecycled()) {
			bgBitmap.recycle();
			bgBitmap = null;
		}

		if (tempBitmap != null && !tempBitmap.isRecycled()) {
			tempBitmap.recycle();
			tempBitmap = null;
		}
	}
}
