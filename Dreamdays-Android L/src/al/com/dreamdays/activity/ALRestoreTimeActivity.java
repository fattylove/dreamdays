package al.com.dreamdays.activity;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.base.LINKS;
import al.com.dreamdays.utils.DialogUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
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
public class ALRestoreTimeActivity extends BaseActivity implements OnClickListener{

	private final static int GET_TIMELIST_SUCCESS  = 1;
	private final static int GET_TIMELIST_FAILED = 2;
	
	private ProgressBar progressBar;
	private ImageButton exitImageButton;
	private ListView restoreListView;
	private TimeAdapter adapter;
	private TextView titleTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme();
		setContentView(R.layout.al_dreamdays_restore_layout);
		hiddenView(findViewById(R.id.notifyBarTextView));
		
		titleTextView = (TextView)this.findViewById(R.id.titleTextView);
		titleTextView.setTypeface(BaseApplication.typeface_heavy);
		
		progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
		exitImageButton = (ImageButton)this.findViewById(R.id.exitImageButton);
		exitImageButton.setOnClickListener(this);
		
		restoreListView = (ListView)this.findViewById(R.id.restoreListView);
		adapter = new TimeAdapter(this);
		restoreListView.setAdapter(adapter);
		
		progressBar.setVisibility(View.VISIBLE);
		getTimeList(getSharedPreferences("Login", Context.MODE_PRIVATE).getString("passport", ""));
		
		restoreListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String time = (String) restoreListView.getItemAtPosition(position);
				Intent intent = new Intent();
				intent.putExtra("time", time);
				intent.setClass(ALRestoreTimeActivity.this, ALRestoreEventActivity.class);
				ALRestoreTimeActivity.this.startActivity(intent);
			}
		});
	}
	
	// listView.addFooterView  listView.addHeaderView  要写在listView.setAdapter 之前
 	
	/**
	 * 获取最近备份的三次时间列表
	 * 
	 * @param passport
	 */
	public void getTimeList(String passport) {
		Parameters parameters = new Parameters();
		parameters.put("passport", passport);
		new HttpService().post(LINKS.GETTIMELIST, parameters, new CallBack<String>() {
			public void onSuccess(String t ,int code) {
				super.onSuccess(t ,code);
				Message message = new Message();
				message.what = GET_TIMELIST_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}

			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Message message = new Message();
				message.what = GET_TIMELIST_FAILED;
				handler.sendMessage(message);
			}
		});
	}
	
	/**
	 * 
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_TIMELIST_SUCCESS:
				progressBar.setVisibility(View.GONE);
				try {
					JSONObject root = new JSONObject(msg.obj.toString());
					int errorCode = root.getInt("errorCode");
					switch (errorCode) {
					case 0:
						JSONArray array = root.getJSONArray("result");
						if(array.length() > 0){
							ArrayList<String> times = new ArrayList<String>();
							for (int i = 0; i < array.length(); i++) {
								times.add(array.getLong(i)+"");
							}
							adapter.setTimes(times);
						}else{
							DialogUtil.showCheckDialog(ALRestoreTimeActivity.this, R.string.login_text_5);
						}
						break;
					}
				} catch (Exception e) {
				}
				break;
				
			case GET_TIMELIST_FAILED:
				progressBar.setVisibility(View.GONE);
				break;
			}
		}
		
	};
	
	/**
	 * 
	 * @author Fatty
	 *
	 */
	class TimeAdapter extends BaseAdapter{
		
		private Context mContext;
		public TimeAdapter(Context context){
			this.mContext = context;
		}
		
		public ArrayList<String> times = new ArrayList<String>();

		public void setTimes(ArrayList<String> times) {
			this.times = times;
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return times.size();
		}

		@Override
		public Object getItem(int position) {
			return times.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.al_dreamdays_restore_item_layout, null);
			TextView timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
			String time = (String) getItem(position);
			timeTextView.setText( Constant.appDateFormat.format(new Date(Long.valueOf(time)*1000)));
			timeTextView.setTypeface(BaseApplication.typeface_medium);
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exitImageButton:
			finish();
			break;

		default:
			break;
		}
	}

}
