package al.com.dreamdays.activity.photo;

import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.utils.ImageUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class ALCameraRollActivity extends BaseActivity implements OnClickListener{
	
	public static final String ACTION = "FINISH_ACTION";
	private FinishThisActivityReceiver finishThisActivityReceiver ;
	/**
	 * 
	 * @author Fatty
	 * 
	 * 结束当前Activity
	 *
	 */
	public class FinishThisActivityReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
			ALCameraRollActivity.this.finish();
		}
	}
	
	private RelativeLayout dreamdayLayout;
	private ImageView dreamdayImageView;
	private TextView dreamdayTextView;
	
	private ListView dreamdaysListView;
    private AlbumAdapter listViewAdapter;
    private TextView titleTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme();
		setContentView(R.layout.fatty_camera_roll_layout);
		hiddenView(findViewById(R.id.notifyBarTextView));
		
		titleTextView = (TextView)this.findViewById(R.id.titleTextView);
		titleTextView.setTypeface(BaseApplication.typeface_heavy);
		
		dreamdayTextView = (TextView)this.findViewById(R.id.dreamdayTextView);
		dreamdayTextView.setTypeface(BaseApplication.typeface_medium);
		
		dreamdayLayout = (RelativeLayout)this.findViewById(R.id.dreamdayLayout);
		dreamdayLayout.setOnClickListener(this);
		
		dreamdayImageView = (ImageView)this.findViewById(R.id.dreamdayImageView);
		
		String cameraPath = "img/wall_paper_img_s_1.jpg";
		dreamdayImageView.setImageBitmap(ImageUtil.loadAssetsDrawable(this, cameraPath));
		dreamdaysListView = (ListView)this.findViewById(R.id.dreamdaysListView);
		
		listViewAdapter = new AlbumAdapter(this);
		if( ImageUtil.getAlbums(this).size()>0){
			listViewAdapter.setAlbumsList(ImageUtil.getAlbums(this));
		}
		
		dreamdaysListView.setAdapter(listViewAdapter);
		dreamdaysListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
						Intent intent = new Intent();
						intent.setClass(ALCameraRollActivity.this, ALLocalPhotosActivity.class);
						intent.putExtra("position", i);
						ALCameraRollActivity.this.startActivity(intent);
					}
				});

		findViewById(R.id.exitImageButton).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		finishThisActivityReceiver = new FinishThisActivityReceiver();
		registerReceiver(finishThisActivityReceiver, new IntentFilter(ACTION));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dreamdayLayout:
			Intent intent = new Intent();
			intent.setClass(this, ALWallPaperActivity.class);
			startActivity(intent);
			break;
		case R.id.cameraRollLayout:
			Intent cIntent = new Intent();
			cIntent.setClass(this, ALLocalPhotosActivity.class);
			startActivity(cIntent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(finishThisActivityReceiver!=null){
			unregisterReceiver(finishThisActivityReceiver);
		}
	}
	
	
}
