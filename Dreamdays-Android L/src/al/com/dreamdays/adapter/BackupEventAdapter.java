package al.com.dreamdays.adapter;

import java.util.ArrayList;

import al.com.dreamdays.activity.ALHomeActivity;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.sqlite.Matter;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class BackupEventAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Matter> matters = new ArrayList<Matter>();

	public BackupEventAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return matters.size();
	}

	public void setMatters(ArrayList<Matter> matters) {
		this.matters.addAll(matters);
		this.notifyDataSetChanged();
	}
	
	public void clear(){
		this.matters.clear();
		this.notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return matters.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(mContext).inflate(R.layout.fatty_backup_matter_item_layout, null);
		TextView content = (TextView) convertView.findViewById(R.id.contentTextView);
		TextView time = (TextView)convertView.findViewById(R.id.timeTextView);
		ImageView itemImageViewBg = (ImageView)convertView.findViewById(R.id.itemImageViewBg);
		
		Matter matter = (Matter) getItem(position);
		
		if(matter.getIfStick() ==1){
			itemImageViewBg.setVisibility(View.VISIBLE);

			ALHomeActivity.setDetailCenterBg(itemImageViewBg, mContext, matter.getClassifyType());
			
			content.setTextColor(Color.parseColor("#ffffff"));
			time.setTextColor(Color.parseColor("#ffffff"));
			content.setShadowLayer(1, 0, -2, Color.parseColor("#000000"));
			time.setShadowLayer(1, 0, -2, Color.parseColor("#000000"));//字体阴影
		}else{
			itemImageViewBg.setVisibility(View.GONE);
		}
		content.setText(matter.getMatterName());
		time.setText(matter.getMatterTime());
		
		content.setTypeface(BaseApplication.typeface_medium);
		time.setTypeface(BaseApplication.typeface_medium);
		
		return convertView;
	}

}
