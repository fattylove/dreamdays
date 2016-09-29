package al.com.dreamdays.widget;

import java.util.ArrayList;

import al.com.dreamdays.activity.ALDetailActivity;
import al.com.dreamdays.activity.ALWelcomeActivity;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.sqlite.Matter;
import al.com.dreamdays.sqlite.MatterService;
import al.com.dreamdays.utils.DateUtil;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.guxiu.dreamdays.R;


/**
 * 
 * @author Fatty
 *
 */
public class WidgetUpdateService extends RemoteViewsService {
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return super.onBind(intent);
	}

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
	}

	/**
	 * 
	 * @author Fatty
	 * 
	 * RemoteViewsFactory
	 *
	 */
	class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

		private Context mContext;
		private ArrayList<Matter> matterList = new ArrayList<Matter>();

		public ListRemoteViewsFactory(Context context, Intent intent) {
			mContext = context;
		}

		@Override
		public void onCreate() {
		}

		@Override
		public int getCount() {
			if (matterList.size() >= 4) {
				return 4;
			} else {
				return matterList.size();
			}
		}

		@Override
		public RemoteViews getLoadingView() {
			return null;
		}

		@Override
		public void onDataSetChanged() {
			synchronized (matterList) {
				matterList.clear();
				MatterService matterDB = new MatterService();
				matterList = (ArrayList<Matter>) matterDB.queryAllMatter(mContext);
				matterList = ALDetailActivity.sortMatters(matterList);
			}
		}

		@Override
		public void onDestroy() {
			matterList.clear();
		}

		@Override
		public RemoteViews getViewAt(int position) {
			if (position < 0 || position >= matterList.size())
				return null;
			
			Matter matter = matterList.get(position);
			final RemoteViews rv = new RemoteViews(mContext.getPackageName(),R.layout.al_dreamdays_home_widget_listview_item_layout);
			rv.setTextViewText(R.id.homeEventDateTextView, null == matter.getMatterTime() ? "" : matter.getMatterTime());
			rv.setTextViewText(R.id.homeEventNameTextView, null == matter.getMatterName() ? "" : matter.getMatterName());
			
			if(position == 0){
				rv.setViewVisibility(R.id.line, View.GONE);
			}else{
				rv.setViewVisibility(R.id.line, View.VISIBLE);
			}
			
			try {
				long MatterDay = DateUtil.getMatterDay(Constant.appDateFormat.parse(matter.getMatterTime()));
				if (MatterDay >= 0) {
					if(matter.getClassifyType() ==1){
						rv.setImageViewResource(R.id.homeEventPointImageView, R.drawable.al_item_down_red);
						rv.setTextColor(R.id.homeEventDaysTextView, Color.parseColor("#ff1744"));
					}else{
						rv.setImageViewResource(R.id.homeEventPointImageView, R.drawable.al_item_down_blue);
						rv.setTextColor(R.id.homeEventDaysTextView, Color.parseColor("#2196f3"));
					}
				} else {
					MatterDay = Math.abs(MatterDay);
					if(matter.getClassifyType() ==1){
						rv.setImageViewResource(R.id.homeEventPointImageView, R.drawable.al_item_up_red);
						rv.setTextColor(R.id.homeEventDaysTextView, Color.parseColor("#ff1744"));
					}else{
						rv.setImageViewResource(R.id.homeEventPointImageView, R.drawable.al_item_up_green);
						rv.setTextColor(R.id.homeEventDaysTextView, Color.parseColor("#a2cf6e"));
					}
				}
				rv.setTextViewText(R.id.homeEventDaysTextView, MatterDay+"");
			} catch (Exception e) {
			}

			int iconRes = Constant.event_black_red_icons[6];
			if (matter.getClassifyType() < 7) {
				iconRes = Constant.event_black_red_icons[matter.getClassifyType()-1];
			}
			rv.setImageViewResource(R.id.homeItemImageView, iconRes);
			
			Intent detailIntent = new Intent(mContext, ALWelcomeActivity.class);
			rv.setOnClickFillInIntent(R.id.main_item_layout, detailIntent);
			return rv;
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}
	}
}