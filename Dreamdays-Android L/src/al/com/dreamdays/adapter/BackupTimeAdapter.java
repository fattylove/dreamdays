package al.com.dreamdays.adapter;

import java.util.ArrayList;
import java.util.Date;

import al.com.dreamdays.base.Constant;
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
public class BackupTimeAdapter extends BaseAdapter {

	private int p;
	private Context mContext;
	private ArrayList<String> contents = new ArrayList<String>();

	public BackupTimeAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return contents.size();
	}

	public ArrayList<String> getContents() {
		return contents;
	}
	
	public void setPoint(int p){
		this.p= p;
		this.notifyDataSetChanged();
	}

	public void setContents(ArrayList<String> contents) {
		this.contents.addAll(contents);
	}
	
	public void clear(){
		this.contents.clear();
		this.notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return contents.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.fatty_backup_time_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.content = (TextView) convertView.findViewById(R.id.timeTextView);
			viewHolder.hockImageView = (ImageView)convertView.findViewById(R.id.hockImageView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}	
		String date = Constant.appDateFormat.format(new Date(Long.valueOf(contents.get(position))*1000));
		viewHolder.content.setText(date);
		if(position == p){
			viewHolder.content.setTextColor(Color.parseColor("#1a8bb2"));
			viewHolder.hockImageView.setVisibility(View.VISIBLE);
		}else{
			viewHolder.hockImageView.setVisibility(View.GONE);
			viewHolder.content.setTextColor(Color.parseColor("#000000"));
		}
		return convertView;
	}

	public class ViewHolder {
		TextView content;
		ImageView hockImageView;
	}

}
