package al.com.dreamdays.activity.photo;

import java.util.ArrayList;

import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.utils.ImageUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class ALWallPaperActivity extends BaseActivity {

	private GridView gridView;
	private ImageButton exitImageButton;
	private ArrayList<String> paperNames = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme();
		setContentView(R.layout.fatty_wall_paper_layout);
		hiddenView(findViewById(R.id.notifyBarTextView));
		
		
		((TextView)findViewById(R.id.titleTextView)).setTypeface(BaseApplication.typeface_heavy);
		
		initDate();
		exitImageButton = (ImageButton)this.findViewById(R.id.exitImageButton);
		exitImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		gridView = (GridView) this.findViewById(R.id.gridView);
		gridView.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater mLayoutInflater = LayoutInflater.from(ALWallPaperActivity.this);
				ViewHolder viewHolder;
				if (convertView == null) {
					viewHolder = new ViewHolder();
					convertView = mLayoutInflater.inflate(R.layout.fatty_wall_paper_item_layout, parent ,false);
					viewHolder.paper = (ImageView)convertView.findViewById(R.id.itemImageView);
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
					viewHolder.paper.setImageBitmap(null);
				}
				viewHolder.paper.setImageBitmap(ImageUtil.loadAssetsDrawable(ALWallPaperActivity.this ,paperNames.get(position)));
				return convertView;
			}

			public long getItemId(int position) {
				return position;
			}

			public Object getItem(int position) {
				return paperNames.get(position);
			}

			public int getCount() {
				return paperNames.size();
			}
		});
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String imgName = (String)gridView.getItemAtPosition(position);
				Intent intent = new Intent();
				intent.putExtra("imgName", imgName);
				intent.setClass(ALWallPaperActivity.this, ALWallPaperSettingActivity.class);
				ALWallPaperActivity.this.startActivity(intent);
				finish();
			}
		});
	}
	
	public class ViewHolder{
		public ImageView paper;
	}
	
	/**
	 * 初始化本地数据
	 */
	public void initDate(){
		for(int i = 1; i<=40 ; i++){
			paperNames.add("img/wall_paper_img_s_"+i+".jpg");
		}
	}
}
