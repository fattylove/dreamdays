package al.com.dreamdays.adapter;

import java.util.ArrayList;

import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.sqlite.Category;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class HomePopoAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Category> groups;
	private LayoutInflater mLayoutInflater;
	private int mIndex;

	public int getmIndex() {
		return mIndex;
	}

	public void setmIndex(int mIndex) {
		this.mIndex = mIndex;
	}

	public HomePopoAdapter(Context context, ArrayList<Category> groups,
			int choicePosition) {
		this.mContext = context;
		this.groups = groups;
		this.mIndex = choicePosition;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return groups.size();
	}

	@Override
	public Object getItem(int position) {
		return groups.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.al_dreamdays_home_popo_item, null);
			viewHolder.itemBgLayout = (LinearLayout)convertView.findViewById(R.id.itemBgLayout);
			viewHolder.groupItemTextView = (TextView) convertView.findViewById(R.id.tv_group_item);
			viewHolder.groupItemTextView.setTypeface(BaseApplication.typeface_medium);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (mIndex == position) {
			viewHolder.groupItemTextView.setTextColor(Color.parseColor("#00bcd4"));
			viewHolder.itemBgLayout.setBackgroundColor(Color.parseColor("#eceff1"));
		} else {
			viewHolder.groupItemTextView.setTextColor(Color.parseColor("#607d8b"));
			viewHolder.itemBgLayout.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		viewHolder.groupItemTextView.setText(groups.get(position).getCategoryName());
		return convertView;
	}

	static class ViewHolder {
		LinearLayout itemBgLayout;
		TextView groupItemTextView;
	}

}