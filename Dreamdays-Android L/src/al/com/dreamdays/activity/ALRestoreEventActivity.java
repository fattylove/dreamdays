package al.com.dreamdays.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import al.com.dreamdays.adapter.BackupEventAdapter;
import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.base.LINKS;
import al.com.dreamdays.sqlite.Category;
import al.com.dreamdays.sqlite.CategoryService;
import al.com.dreamdays.sqlite.Matter;
import al.com.dreamdays.sqlite.MatterService;
import al.com.dreamdays.utils.Base64Util;
import al.com.dreamdays.utils.DialogUtil;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guxiu.dreamdays.R;

import fatty.library.http.core.CallBack;
import fatty.library.http.core.HttpService;
import fatty.library.http.core.Parameters;

/**
 * 
 * @author Fatty
 *
 */
public class ALRestoreEventActivity extends BaseActivity implements OnClickListener{

	private final static int GET_MATTER_SUCCESS =3;
	private final static int GET_MATTER_FAILED = 4;
	
	private final static int RESTORE_SUCCESS = 5;
	private final static int RESTORE_FAILED = 6;
	
	private SimpleDateFormat dateFMT_1 = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
	private SimpleDateFormat dateFMT_2 = new SimpleDateFormat("MMMM dd,yyyy", Locale.ENGLISH);
	
	private ArrayList<Matter> matters = new ArrayList<Matter>();
	private ArrayList<Category> categories = new ArrayList<Category>();
	
	private ProgressBar progressBar;
	private ImageButton exitImageButton;
	private TextView titleTextView;
	
	//bottom
	private RelativeLayout bottomLayout;
	private TextView replaceLineTextView,replaceTextView ,replace2TextView;
	
	private ListView restoreListView;
	private BackupEventAdapter backupBaseadapter;
	private String time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme();
		setContentView(R.layout.al_dreamdays_restore_2_layout);
		hiddenView(findViewById(R.id.notifyBarTextView));
		
		time = getIntent().getStringExtra("time");
		
		progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
		exitImageButton = (ImageButton)this.findViewById(R.id.exitImageButton);
		titleTextView = (TextView)this.findViewById(R.id.titleTextView);
		titleTextView.setTypeface(BaseApplication.typeface_heavy);
		titleTextView.setText( Constant.appDateFormat.format(new Date(Long.valueOf(time)*1000)));
		exitImageButton.setOnClickListener(this);
		
		restoreListView = (ListView)this.findViewById(R.id.restoreListView);
		backupBaseadapter = new BackupEventAdapter(this);
		restoreListView.setAdapter(backupBaseadapter);
		
		bottomLayout = (RelativeLayout)this.findViewById(R.id.bottomLayout);
		replaceLineTextView = (TextView)this.findViewById(R.id.replaceLineTextView);
		replaceTextView = (TextView)this.findViewById(R.id.replaceTextView);
		replace2TextView = (TextView)this.findViewById(R.id.replace2TextView);
		bottomLayout.setVisibility(View.GONE);
		bottomLayout.setOnClickListener(this);
		
		replaceLineTextView.setTypeface(BaseApplication.typeface_medium);
		replaceTextView.setTypeface(BaseApplication.typeface_medium);
		replace2TextView.setTypeface(BaseApplication.typeface_medium);
		
		getMatters(getSharedPreferences("Login", Context.MODE_PRIVATE).getString("passport", ""), time);
	}
	

	/**
	 * 获取Matter数据
	 * 
	 * @param passport
	 * @param last_update
	 */
	public void getMatters(String passport, String last_update) {
		progressBar.setVisibility(View.VISIBLE);
		Parameters parameters = new Parameters();
		parameters.put("passport", passport);
		parameters.put("last_update", last_update);
		new HttpService().post(LINKS.GETMATTER, parameters, new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				if(!TextUtils.isEmpty(t)){
					Message message = new Message();
					message.obj = t;
					message.what = GET_MATTER_SUCCESS;
					handler.sendMessage(message);
				}
			}
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Message message = new Message();
				message.what = GET_MATTER_FAILED;
				handler.sendMessage(message);
			}
		});
	}
	
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			
			case GET_MATTER_SUCCESS:
				try {
					bottomLayout.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
					JSONObject root = new JSONObject((String) msg.obj);
					JSONArray array = root.getJSONArray("result");
					
					ArrayList<Matter> tempArrayList = new ArrayList<Matter>();
					ArrayList<Matter> sortArrayList = new ArrayList<Matter>();
					for(int i = 0; i < array.length() ; i++){
						Matter matter =new Matter();
						Category category = new Category();
						
						JSONObject matterJson = array.getJSONObject(i);
						matter.set_id(matterJson.getInt("id"));
						matter.setMatterName(Base64Util.BASE64_decode(matterJson.getString("matter_name")));
						Date date1 =  dateFMT_2.parse(matterJson.getString("matter_time"));
						Date date2 =  dateFMT_2.parse(matterJson.getString("create_time"));
						matter.setMatterTime(dateFMT_1.format(date1));
						matter.setCreateTime(dateFMT_1.format(date2));
						matter.setIfStick(matterJson.getInt("if_stict"));
						matter.setVideoName(matterJson.getString("acc_video_name"));
						matter.setPicName(matterJson.getString("pic_name"));
						matter.setIf_notify(0);
						matter.setClassifyType(matterJson.getInt("classify_type"));
						matter.setSort_id(matterJson.getInt("sort_id"));
						matter.setRepeat_type(matterJson.getInt("repeat_type"));
						
						category.set_id(matterJson.getInt("classify_type"));
						category.setCategoryName(Base64Util.BASE64_decode(matterJson.getString("classify_name")));
						categories.add(category);
						if(matterJson.getInt("if_stict")==1){
							tempArrayList.add(matter);
						}else{
							sortArrayList.add(matter);
						}
					}
					matters.addAll(tempArrayList);
					matters.addAll(sortArrayList);
					tempArrayList.clear();
					sortArrayList.clear();
					
					backupBaseadapter.setMatters(matters);
				} catch (Exception e) {
				}
				break;
			case GET_MATTER_FAILED:
				progressBar.setVisibility(View.GONE);
				DialogUtil.showCheckDialog(ALRestoreEventActivity.this, R.string.login_text_6);
				break;
				
			case RESTORE_SUCCESS:
				new Handler().postDelayed(new Runnable() {
					public void run() {
						bottomLayout.setClickable(false);
						bottomLayout.setOnClickListener(null);
						progressBar.setVisibility(View.GONE);
						replaceTextView.setText(getString(R.string.al_restored_on)+" "+Constant.appDateFormat.format(new Date(Long.valueOf(time)*1000)));
						replaceTextView.setTextColor(Color.parseColor("#93c756"));
						replace2TextView.setVisibility(View.GONE);
					}
				}, 500);
				break;
			case RESTORE_FAILED:
				progressBar.setVisibility(View.GONE);
				DialogUtil.showCheckDialog(ALRestoreEventActivity.this, R.string.login_text_5);
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exitImageButton:
			finish();
			break;

		case R.id.bottomLayout:
			progressBar.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if(matters.size() > 0 && categories.size() > 0 ){
						MatterService service = new MatterService();
						service.deleteMatters(ALRestoreEventActivity.this);
						service.insertMatters(ALRestoreEventActivity.this, matters);
						
						CategoryService categoryService = new CategoryService();
						categoryService.deleteCategory(ALRestoreEventActivity.this);
						categoryService.insertDefaultCategory(ALRestoreEventActivity.this);
						categoryService.replaceCategory(ALRestoreEventActivity.this, categories);
						Message message = new Message();
						message.what = RESTORE_SUCCESS;
						handler.sendMessage(message);
					}else{
						Message message = new Message();
						message.what = RESTORE_FAILED;
						handler.sendMessage(message);
					}
				}
			}, 1000);
			break;
		default:
			break;
		}
	}

}
