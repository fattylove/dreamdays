package al.com.dreamdays.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

import al.com.dreamdays.adapter.DetailPagerAdapter;
import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.fragment.DetailFragment;
import al.com.dreamdays.sqlite.Matter;
import al.com.dreamdays.sqlite.MatterService;
import al.com.dreamdays.utils.DateUtil;
import al.com.dreamdays.utils.event.LeftEventCompare;
import al.com.dreamdays.utils.event.SinceEventCompare;
import al.com.dreamdays.view.AlphaImageView;
import al.com.dreamdays.view.AlphaImageView.LoadBitmapOOMListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class ALDetailActivity extends BaseActivity implements OnClickListener {
	
	// Android L 
	private ImageButton exitImageButton ,editImageButton ,audioImageButton ,shareImageButton;
	
	public static final int VOICE = 106;
	
	private ViewPager viewPager;
	private DetailPagerAdapter adapter;
	private DetailFragment currentFragment;
	
	private RelativeLayout globalLayout;
	
	private int index = 0;
	private int detailMatterId;
	private Matter detailMatter;
	private AlphaImageView detailBgImageView;
	private MatterService matterService;
	private ArrayList<Matter> matters = new ArrayList<Matter>();
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	
	public static final String ACTION = "FINISH_DETAIL_ACTIVITY";
	public class FinishDetailActivityReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			ALDetailActivity.this.finish();
		}
	}
	
	private FinishDetailActivityReceiver receiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme();
		setContentView(R.layout.al_dreamdays_detail_layout);
		hiddenView(findViewById(R.id.notifyBarTextView));
		
		receiver = new FinishDetailActivityReceiver();
		registerReceiver(receiver, new IntentFilter(ACTION));
		
		matterService = new MatterService();
		
		globalLayout = (RelativeLayout)this.findViewById(R.id.globalLayout);
		detailBgImageView = (AlphaImageView)this.findViewById(R.id.detailBgImageView);
		detailBgImageView.setOnLoadBitmapOOMListener(new LoadBitmapOOMListener() {
			@Override
			public void success(int state , int matterId) {
				switch (state) {
				case AlphaImageView.LOCAL_BITMAP:
					System.err.println("LOCAL_BITMAP ：MatterId:"+matterId +" , load success!");
					break;
				case AlphaImageView.RES_BITMAP:
					System.err.println("RES_BITMAP ： MatterId:"+matterId +" , load success!");
					break;
				}
			}
			
			@Override
			public void failed(int state , int matterId) {
				switch (state) {
				case AlphaImageView.LOCAL_BITMAP:
					System.err.println("LOCAL_BITMAP ： MatterId:"+matterId +" , load failed!");
					break;
				case AlphaImageView.RES_BITMAP:
					System.err.println("RES_BITMAP ： MatterId:"+matterId +" , load failed!");
					break;
				}
			}
		});
		
		exitImageButton = (ImageButton)this.findViewById(R.id.exitImageButton);
		editImageButton = (ImageButton)this.findViewById(R.id.editImageButton);
		audioImageButton = (ImageButton)this.findViewById(R.id.audioImageButton);
		shareImageButton = (ImageButton)this.findViewById(R.id.shareImageButton);
		editImageButton.setOnClickListener(this);
		audioImageButton.setOnClickListener(this);
		exitImageButton.setOnClickListener(this);
		shareImageButton.setOnClickListener(this);
		
		detailMatterId = getIntent().getIntExtra("matter_id", 0);
		detailMatter = matterService.queryMatterById(this, detailMatterId);

		//ViewPager
		viewPager = (ViewPager)this.findViewById(R.id.viewPager);
		
		//set Data
		ArrayList<Matter> sortMatters = (ArrayList<Matter>) matterService.queryAllMatterExitStickMatter(this);
		Matter coverMatter =  matterService.queryStickMatter(this);
		
		//set sort Data
		SharedPreferences ifSortByTime = getSharedPreferences("IF_SORTBYTIME",	0);
		if (ifSortByTime.getBoolean("ifSortByTime", true)) {
			matters.clear();
			if(coverMatter!=null){
				matters.add(coverMatter);
			}
			matters.addAll(ALDetailActivity.sortMatters(sortMatters));
		}else{
			matters.clear();
			if(coverMatter!=null){
				matters.add(coverMatter);
			}
			matters.addAll(sortMatters);
		}
		for(int i = 0; i < matters.size() ;i++){
			DetailFragment detailFragment = new DetailFragment();
			detailFragment.setMatter(matters.get(i));
			if(detailMatter.get_id() == matters.get(i).get_id()){
				index = i;
			}
			fragments.add(detailFragment);
		}
		
		detailBgImageView.setmPosition(index);
		detailBgImageView.setMatters(matters);
		
		currentFragment = (DetailFragment) fragments.get(index);
		adapter = new DetailPagerAdapter(getSupportFragmentManager(), fragments);
		viewPager.setAdapter(adapter);  
		viewPager.setCurrentItem(index);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				detailMatter = matters.get(arg0);
				if(detailMatter != null){
					detailMatterId = detailMatter.get_id();
					currentFragment = (DetailFragment) fragments.get(arg0);
					currentFragment.setMatter(detailMatter);
					currentFragment.refreshData(detailMatter);
				}
			}
			
			public void onPageScrolled(int index, float degree, int arg2) {
				detailBgImageView.setmDegree(degree);
				detailBgImageView.setmPosition(index);
			}
			
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		boolean isLite = getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isLite", true);
		boolean isOpen = getSharedPreferences("fatty", Context.MODE_PRIVATE).getBoolean("isOpen", true);
		if(isLite)
			if(isOpen)
				addAdmod();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
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
		
		if(adView!=null){
			adView.destroy();
		}
		
		if(detailBgImageView!=null){
			detailBgImageView.clear();
		}
		
		if(receiver!=null){
			unregisterReceiver(receiver);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case VOICE:
			if(data==null){
				return;
			}
			
			int id = data.getIntExtra("matterId", 0);
			String mRecordPath = data.getStringExtra("mRecordPath");
			Matter changedMatter = matterService.queryMatterById(ALDetailActivity.this	, id);
			
			if(changedMatter != null){
				if(!TextUtils.isEmpty(mRecordPath)){
					changedMatter.setVideoName(mRecordPath);
				}
				changedVoice(changedMatter);
			}
			break;
		}
	}
	
	/*********************************************************************************************************************/
	@Override
	public void onClick(View v) {
		
		currentFragment.stopVoice();
		switch (v.getId()) {
		case R.id.exitImageButton:
			finish();
			break;
		case R.id.editImageButton:
			Intent editIntent = new Intent();
			editIntent.setClass(ALDetailActivity.this, ALAddEditEventActivity.class);
			editIntent.putExtra("matter_id", detailMatterId);
			startActivity(editIntent);
			break;
		case R.id.audioImageButton:
			Intent recordVoiceIntent = new Intent();
			recordVoiceIntent.setClass(ALDetailActivity.this, ALVoice_DActivity.class);
			recordVoiceIntent.putExtra("matterID", detailMatterId);
			startActivityForResult(recordVoiceIntent, VOICE);
			break;
		case R.id.shareImageButton:
			share();
			break;
		}
	}

	/**
	 * 更改声音配置
	 * 
	 * @param voiceMatter
	 */
	private void changedVoice(Matter voiceMatter){
		Matter changedMatter = null;
		for(int i=0; i<matters.size() ; i++){
			if(matters.get(i).get_id() == voiceMatter.get_id()){
				changedMatter = matters.get(i);
			}
		}
		int index = matters.indexOf(changedMatter);
		matters.remove(index);
		matters.add(index, voiceMatter);
		
		currentFragment.setMatter(voiceMatter);
		currentFragment.refreshData(voiceMatter);
	}
	
	/**
	 * 屏幕截屏分享
	 * 
	 * Share
	 */
	private void share(){
		exitImageButton.setVisibility(View.INVISIBLE);
		editImageButton.setVisibility(View.INVISIBLE);
		audioImageButton.setVisibility(View.INVISIBLE);
		shareImageButton.setVisibility(View.INVISIBLE);
		if(adView!=null){
			adView.setVisibility(View.INVISIBLE);
		}
		try {
//			View view = ALDetailActivity.this.getWindow().getDecorView();
			globalLayout.setDrawingCacheEnabled(true);
			globalLayout.buildDrawingCache();
			Bitmap bitmap = globalLayout.getDrawingCache();
			try {
				boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
				if (sdCardExist) {
					FileOutputStream fos = new FileOutputStream(Constant.SHAREDETAILIMAGE);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 65, fos);
				}
			} catch (Exception e) {
			}
			Intent intent2 = new Intent(Intent.ACTION_SEND);
			File imageFile = new File(Constant.SHAREDETAILIMAGE);
			if (imageFile.exists() && imageFile.isFile()) {
				intent2.setType("image/*");
				Uri u = Uri.fromFile(imageFile);
				intent2.putExtra(Intent.EXTRA_STREAM, u);
			} else {
				intent2.setType("text/plain");
			}
			
			// get text
			long MatterDay = DateUtil.getMatterDay(Constant.appDateFormat.parse(detailMatter.getMatterTime()));
			String ifDayLeft = "";
			if (MatterDay > 0) {
				ifDayLeft = getString(R.string.app_days_left);
			} else {
				MatterDay = Math.abs(MatterDay);
				ifDayLeft = getString(R.string.app_days_since);
			}
			
			intent2.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) +"");
			intent2.putExtra(Intent.EXTRA_TEXT,    getString(R.string.app_name) + ": "+ detailMatter.getMatterName() + ", "+ MatterDay + " " + ifDayLeft+ "    http://bit.ly/1kDHyce    ");
			
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(Intent.createChooser(intent2, "Share Dreamdays"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(adView!=null){
			adView.setVisibility(View.VISIBLE);
		}
		exitImageButton.setVisibility(View.VISIBLE);
		editImageButton.setVisibility(View.VISIBLE);
		audioImageButton.setVisibility(View.VISIBLE);
		shareImageButton.setVisibility(View.VISIBLE);
	}
	
	/**
	 * admod
	 */
	private AdView adView;
	private void addAdmod(){
		adView = new AdView(this);
		adView.setAdUnitId(Constant.ADMOD_KEY);
		adView.setAdSize(AdSize.BANNER);
		((LinearLayout)this.findViewById(R.id.googleAdsLayout)).addView(adView);
	    adView.loadAd(new AdRequest.Builder().build());
	}
	
	/**
	 * events 排序
	 * 
	 * @param matters
	 * @return
	 */
	public static ArrayList<Matter> sortMatters(ArrayList<Matter> matters){
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
		
		return matters;
	}
}
