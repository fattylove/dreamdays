package al.com.dreamdays.activity;

import al.com.dreamdays.base.Constant;
import al.com.dreamdays.sqlite.Matter;
import al.com.dreamdays.sqlite.MatterService;
import al.com.dreamdays.utils.RecordUtil;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.guxiu.dreamdays.R;


/**
 * 
 * @author Fatty
 * 
 * _D =  Detail
 *
 */
//TODO style nothing
public class ALVoice_DActivity extends Activity implements OnClickListener{

	private Intent intent = null;
	private RecordUtil mRecordUtil;
	
	private Button startButton;
	private ImageButton stopButton;
	private RelativeLayout mainLayout;

	private static final int MAX_TIME = 60 * 5;
	private static final int MIN_TIME = 1;
	
	private static final int RECORD_NO = 0; 
	private static final int RECORD_ING = 1; 
	private static final int RECORD_END = 2;
	
	private int mRecordState ;
	private float mRecordTime;
	private int matterID ;
	private Matter matter ;
	
	private final String PATH = Constant.DREAMDAY_MUSIC;
	private String mRecordPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_dreamdays_record_voice);
		
		intent = getIntent();
		matterID = intent.getIntExtra("matterID", 0);
		intent.putExtra("path", "");
		if (matterID == 0) {
			MatterService matterDB = new MatterService();
			matterID = matterDB.getMaxId(ALVoice_DActivity.this) + 1;
		}
		mRecordPath = PATH + System.currentTimeMillis() +"_voice_" + matterID + ".amr";
		
		mRecordUtil = new RecordUtil(mRecordPath);
		
		mainLayout = (RelativeLayout)findViewById(R.id.recording_main_layout);
		startButton = (Button) findViewById(R.id.recording_start_btn);
		stopButton = (ImageButton) findViewById(R.id.recording_stop_btn);
		mainLayout.setOnClickListener(this);
		startButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);
		startButton.setVisibility(View.VISIBLE);
		stopButton.setVisibility(View.GONE);
	}

	/**
	 * 用来控制录音
	 */
	Handler mRecordHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (mRecordState == RECORD_ING) {
					mRecordState = RECORD_END;
					try {
						mRecordUtil.stop();
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
				}
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.recording_main_layout:
			if (mRecordState == RECORD_NO) {
				finish();
			}
			break;
		
		case R.id.recording_start_btn:
			startButton.setVisibility(View.GONE);
			stopButton.setVisibility(View.VISIBLE);
			stopButton.setImageResource(R.drawable.progress_round);  
			AnimationDrawable  animationDrawable = (AnimationDrawable) stopButton.getDrawable();
            animationDrawable.start();  
			
        	mRecordPath = PATH + System.currentTimeMillis() +"_voice_" + matterID + ".amr";
    		mRecordUtil = new RecordUtil(mRecordPath);
            
			if (mRecordState != RECORD_ING) {
				mRecordState = RECORD_ING;
				try {
					mRecordUtil.start();
				} catch (Exception e) {
					e.printStackTrace();
				}

				new Thread(new Runnable() {
					public void run() {
						mRecordTime = 0;
						while (mRecordState == RECORD_ING) {
							if (mRecordTime >= MAX_TIME) {
								mRecordHandler.sendEmptyMessage(0);
							} else {
								try {
									Thread.sleep(200);
									mRecordTime += 0.2;
									if (mRecordState == RECORD_ING) {
										mRecordHandler.sendEmptyMessage(1);
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}).start();
			}
			break;
			
		case R.id.recording_stop_btn:
			startButton.setVisibility(View.VISIBLE);
			stopButton.setVisibility(View.GONE);
			
			if (mRecordState == RECORD_ING) {
				mRecordState = RECORD_END;
				if (existSDCard()) {
					try {
						mRecordUtil.stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (mRecordTime <= MIN_TIME) {
						Toast.makeText(ALVoice_DActivity.this,R.string.app_record_tooshort,Toast.LENGTH_SHORT).show();
						mRecordTime = 0;
					}else{
						
						saveVoice(mRecordPath);
						Intent intent = new Intent();
						intent.putExtra("matterId", matterID);
						intent.putExtra("mRecordPath", mRecordPath);
						setResult(RESULT_OK, intent);
						
						finish();
					}
				} else {
					Toast.makeText(ALVoice_DActivity.this,R.string.app_record_sdcarderror, Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}
	
	
	@Override
	public void finish() {
		mainLayout.setBackgroundColor(Color.TRANSPARENT);
		super.finish();
	}
	
	private boolean existSDCard() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED) ? true : false ;
	}
	
	public void saveVoice(String vPath){
		MatterService matterDB = new MatterService();
		matter = matterDB.queryMatterById(ALVoice_DActivity.this, matterID);
		if(matter!=null){
			matter.setVideoName(vPath);
			matterDB.insertOrUpdateNewMatter(ALVoice_DActivity.this, matter);
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
