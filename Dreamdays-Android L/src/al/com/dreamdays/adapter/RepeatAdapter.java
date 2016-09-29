package al.com.dreamdays.adapter;

import java.util.ArrayList;

import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.sqlite.Repeat;
import android.content.Context;
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
public class RepeatAdapter extends BaseAdapter {

	private Context mContext;
	private int mCurrentPosition;
	private ArrayList<Repeat> repeatArrayList = new ArrayList<Repeat>();


	public RepeatAdapter(Context context) {
		this.mContext = context;
	}

	public void addRepeats(ArrayList<Repeat> repeatArrayList){
		this.repeatArrayList.addAll(repeatArrayList);
		this.notifyDataSetChanged();
	}
	
	public void setPosition(int position){
		this.mCurrentPosition = position;
	}
	
	public void clear(){
		this.repeatArrayList.clear();
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return repeatArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return repeatArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.al_dreamdays_repeat_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.itemText = (TextView) convertView.findViewById(R.id.category_item_title);
			viewHolder.itemCheck = (ImageView) convertView.findViewById(R.id.category_item_check);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Repeat repeat = repeatArrayList.get(position);
		viewHolder.itemText.setText(repeat.getName());
		viewHolder.itemText.setTypeface(BaseApplication.typeface_medium);
		if (mCurrentPosition == position ) {
			viewHolder.itemCheck.setVisibility(View.VISIBLE);
		} else {
			viewHolder.itemCheck.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	public class ViewHolder {
		TextView itemText;
		ImageView itemCheck;
	}

}
