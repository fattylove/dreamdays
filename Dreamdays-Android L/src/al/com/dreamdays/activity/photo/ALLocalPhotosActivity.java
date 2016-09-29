package al.com.dreamdays.activity.photo;

import java.util.ArrayList;

import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.utils.ImageUtil;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 
 * @author Fatty
 *
 */
public class ALLocalPhotosActivity extends BaseActivity {

	private int postition = 0 ;
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
		
		postition = getIntent().getIntExtra("position", 0);
		
		ArrayList<AlbumEntity> albums = ImageUtil.getAlbums(this);
		String album_dir = albums.get(postition).getmName();
		paperNames.addAll(ImageUtil.getPhotos(this,album_dir));
		
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
				LayoutInflater mLayoutInflater = LayoutInflater.from(ALLocalPhotosActivity.this);
				final ViewHolder viewHolder;
				if (convertView == null) {
					viewHolder = new ViewHolder();
					convertView = mLayoutInflater.inflate(R.layout.fatty_wall_paper_item_layout, parent ,false);
					viewHolder.paper = (ImageView)convertView.findViewById(R.id.itemImageView);
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
					viewHolder.paper.setImageBitmap(null);
				}
				//fit size
				ImageSize imageSize = new ImageSize(150 , 150);
				ImageLoader.getInstance().loadImage("file:///" + paperNames.get(position),imageSize, new ImageLoadingListener() {
					public void onLoadingStarted(String arg0, View arg1) {
					}
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					}
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						viewHolder.paper.setImageBitmap(arg2);
					}
					public void onLoadingCancelled(String arg0, View arg1) {
					}
				});
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
				intent.setClass(ALLocalPhotosActivity.this, ALLocalPhotosSettingActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	public class ViewHolder{
		public ImageView paper;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		ImageLoader.getInstance().clearMemoryCache();
	}
	

}
