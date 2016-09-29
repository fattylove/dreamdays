package al.com.dreamdays.widget;

import al.com.dreamdays.activity.ALWelcomeActivity;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.sqlite.Matter;
import al.com.dreamdays.sqlite.MatterService;
import al.com.dreamdays.utils.DateUtil;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 * 
 * 4*1 widget
 *
 */
public class DreamdaysWidgetCover extends AppWidgetProvider {

	@Override
	public IBinder peekService(Context myContext, Intent service) {
		return super.peekService(myContext, service);
	}

	// 更新Next　Ｗidget
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		ComponentName cmpName = new ComponentName(context, DreamdaysWidgetCover.class);
		setWidgetUIAndUpdate(context, AppWidgetManager.getInstance(context), cmpName);
	}

	/**
	 * 更新UpdateUI
	 */
	private static void setWidgetUIAndUpdate(Context context, AppWidgetManager appWidgetManager, ComponentName cmpName) {
		Matter matter = new MatterService().queryStickMatter(context);
		if (matter != null ) {
			// set matter name
			final RemoteViews rv = new RemoteViews(context.getPackageName(),R.layout.al_dreamdays_appwidget_next);
			rv.setTextViewText(R.id.homeEventDateTextView, null == matter.getMatterTime() ? "" : matter.getMatterTime());
			rv.setTextViewText(R.id.homeEventNameTextView, null == matter.getMatterName() ? "" : matter.getMatterName());
			
			try {
				//set date number
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

			//set icon resources
			int iconRes = Constant.event_black_red_icons[6];
			if (matter.getClassifyType() < 7) {
				iconRes = Constant.event_black_red_icons[matter.getClassifyType()-1];
			}
			rv.setImageViewResource(R.id.homeItemImageView, iconRes);
			
			// set pending intent 
			Intent intent = new Intent(context, ALWelcomeActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setOnClickPendingIntent(R.id.widgetnext_cellbtn, pendingIntent);
			try {
				appWidgetManager.updateAppWidget(cmpName, rv);
			} catch (Exception e) {
				e.toString();
				return;
			}
		} else {
			// set null layout ,  because event is null.
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.al_dreamdays_appwidget_next_null);
			
			Intent intent = new Intent(context, ALWelcomeActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.widgetnext_cellbtn_null, pendingIntent);
			
			try {
				appWidgetManager.updateAppWidget(cmpName, remoteViews);
			} catch (Exception e) {
				e.toString();
				return;
			}
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	/**
	 * update Next Widget data
	 * 
	 * @param context
	 */
	public static void updateNextUI(Context context) {
		ComponentName cmpName = new ComponentName(context, DreamdaysWidgetCover.class);
		setWidgetUIAndUpdate(context, AppWidgetManager.getInstance(context),cmpName);
	}
}
