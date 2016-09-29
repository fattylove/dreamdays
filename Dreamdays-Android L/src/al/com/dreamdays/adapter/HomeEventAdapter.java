package al.com.dreamdays.adapter;

import java.util.ArrayList;

import al.com.dreamdays.activity.ALHomeActivity;
import al.com.dreamdays.base.AManager;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.sqlite.Matter;
import al.com.dreamdays.utils.DateUtil;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
public class HomeEventAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Matter> matters = new ArrayList<Matter>();

	public HomeEventAdapter(Context context) {
		this.mContext = context;
	}

	public void addMatters(ArrayList<Matter> matters) {
		this.matters.addAll(matters);
	}

	public void addMatter(Matter matter) {
		this.matters.add(matter);
	}

	public void clear() {
		matters.clear();
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return matters.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.al_dreamdays_home_listview_item_layout, parent , false );
			viewHolder.homeItemImageView = (ImageView)convertView.findViewById(R.id.homeItemImageView);
			viewHolder.homeEventNameTextView = (TextView)convertView.findViewById(R.id.homeEventNameTextView);
			viewHolder.homeEventDateTextView = (TextView)convertView.findViewById(R.id.homeEventDateTextView);
			viewHolder.homeEventDaysTextView = (TextView)convertView.findViewById(R.id.homeEventDaysTextView);
			viewHolder.deleteTextView = (TextView)convertView.findViewById(R.id.deleteTextView);
			viewHolder.homeEventPointImageView = (ImageView)convertView.findViewById(R.id.homeEventPointImageView);
			viewHolder.line = (TextView)convertView.findViewById(R.id.line);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(position == 0){
			viewHolder.line.setVisibility(View.INVISIBLE);
		}else{
			viewHolder.line.setVisibility(View.VISIBLE);
		}
		viewHolder.deleteTextView.setTypeface(BaseApplication.typeface_medium);
		viewHolder.homeEventNameTextView.setTypeface(BaseApplication.typeface_medium);
		viewHolder.homeEventDateTextView.setTypeface(BaseApplication.typeface_medium);
		viewHolder.homeEventDaysTextView.setTypeface(BaseApplication.typeface_medium);
		
		final Matter matter = (Matter) getItem(position);
		
		viewHolder.homeEventNameTextView.setText(null == matter.getMatterName() ? "": matter.getMatterName());
		viewHolder.homeEventDateTextView.setText(null == matter.getMatterTime() ? "": matter.getMatterTime());
		try {
			long MatterDay = DateUtil.getMatterDay(Constant.appDateFormat.parse(matter.getMatterTime()));
			if (MatterDay > 0) {
				if(matter.getClassifyType() ==1){
					viewHolder.homeEventPointImageView.setImageResource(R.drawable.al_item_down_red);
					viewHolder.homeEventDaysTextView.setTextColor(Color.parseColor("#ff1744"));
				}else{
					viewHolder.homeEventPointImageView.setImageResource(R.drawable.al_item_down_blue);
					viewHolder.homeEventDaysTextView.setTextColor(Color.parseColor("#2196f3"));
				}
			} else {
				MatterDay = Math.abs(MatterDay);
				viewHolder.homeEventDaysTextView.setText(MatterDay + " ");
				if(matter.getClassifyType() ==1){
					viewHolder.homeEventPointImageView.setImageResource(R.drawable.al_item_up_red);
					viewHolder.homeEventDaysTextView.setTextColor(Color.parseColor("#ff1744"));
				}else{
					viewHolder.homeEventPointImageView.setImageResource(R.drawable.al_item_up_green);
					viewHolder.homeEventDaysTextView.setTextColor(Color.parseColor("#a2cf6e"));
				}
			}
			viewHolder.homeEventDaysTextView.setText(MatterDay + " ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ALHomeActivity.setBlackRedIcon(viewHolder.homeItemImageView, mContext, matter.getClassifyType());
		
		if(matter.isDeleteType()){
			viewHolder.deleteTextView.setVisibility(View.VISIBLE);
			viewHolder.homeEventDaysTextView.setVisibility(View.GONE);
			viewHolder.homeEventPointImageView.setVisibility(View.GONE);
			viewHolder.deleteTextView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					AManager actManager = AManager.getAppManager();
					if(actManager.currentActivity() instanceof ALHomeActivity){
						ALHomeActivity currentActivity = (ALHomeActivity) actManager.currentActivity();
						currentActivity.showRemoveEventDialog(mContext , matter.get_id());
					}
				}
			});
		}else{
			viewHolder.deleteTextView.setVisibility(View.GONE);
			viewHolder.homeEventDaysTextView.setVisibility(View.VISIBLE);
			viewHolder.homeEventPointImageView.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}

	public class ViewHolder {
		public ImageView homeItemImageView;
		public TextView homeEventNameTextView;
		public TextView homeEventDateTextView;
		public TextView homeEventDaysTextView;
		public TextView deleteTextView;
		public ImageView homeEventPointImageView;
		public TextView line;
	}

}
