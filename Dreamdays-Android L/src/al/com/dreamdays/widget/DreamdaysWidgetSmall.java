package al.com.dreamdays.widget;

import java.io.File;
import java.text.ParseException;

import al.com.dreamdays.activity.ALWelcomeActivity;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.sqlite.Matter;
import al.com.dreamdays.sqlite.MatterService;
import al.com.dreamdays.utils.DateUtil;
import al.com.dreamdays.utils.FileUtil;
import al.com.dreamdays.utils.ImageUtil;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.guxiu.dreamdays.R;

@SuppressLint("NewApi")
public class DreamdaysWidgetSmall extends AppWidgetProvider {

	@Override
	public IBinder peekService(Context myContext, Intent service) {
		return super.peekService(myContext, service);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		ComponentName cmpName = new ComponentName(context,DreamdaysWidgetSmall.class);
		setWidgetUIAndUpdate(context, AppWidgetManager.getInstance(context), cmpName);
	}
	
	public static void updateSmallUI(Context context) {
		ComponentName cmpName = new ComponentName(context, DreamdaysWidgetSmall.class);
		setWidgetUIAndUpdate(context, AppWidgetManager.getInstance(context), cmpName);
	}

	private static void setWidgetUIAndUpdate(Context context, AppWidgetManager appWidgetManager, ComponentName cmpName) {
		MatterService matterDB = new MatterService();
		Matter stickMatter = matterDB.queryStickMatter(context);
		
		if (stickMatter != null) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.al_dreamdays_widget_top_layout);
			remoteViews.setViewVisibility(R.id.nothingLayout, View.GONE);
			remoteViews.setViewVisibility(R.id.yuanbingImageView, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.eventTopLayout, View.VISIBLE);
			remoteViews.setTextViewText(R.id.topNameTextView, stickMatter.getMatterName());

			//set event icon
			int iconRes = Constant.event_white_icons[6];
			if (stickMatter.getClassifyType() < 7) {
				iconRes = Constant.event_white_icons[stickMatter.getClassifyType()-1];
			}
			remoteViews.setImageViewResource(R.id.centerImageView, iconRes);
			
			String bgImagePath = stickMatter.getPicName();
			if (!TextUtils.isEmpty(bgImagePath) && FileUtil.checkFileExists(bgImagePath)) {
				File file = new File(bgImagePath);
				remoteViews.setImageViewBitmap(R.id.topBgImageView, ImageUtil.loadSdcardDrawable(file , Constant.width , Constant.height/2));
			} else {
				int id = stickMatter.getClassifyType();
				int bgRes = Constant.top_bgs[6];
				if (id < 7) {
					bgRes = Constant.top_bgs[id-1];
				}
				remoteViews.setImageViewResource(R.id.topBgImageView, bgRes);
			}
			
			//set event date
			try {
				long MatterDay = DateUtil.getMatterDay(Constant.appDateFormat.parse(stickMatter.getMatterTime()));
				if (MatterDay > 0) {
					remoteViews.setTextViewText(R.id.topDaysTextView, " " + MatterDay + " ");
					remoteViews.setViewVisibility(R.id.bottomPointImageView, View.VISIBLE);
					remoteViews.setViewVisibility(R.id.topPointImageView, View.GONE);
				} else {
					MatterDay = Math.abs(MatterDay);
					remoteViews.setTextViewText(R.id.topDaysTextView, " " + MatterDay + " ");
					remoteViews.setViewVisibility(R.id.bottomPointImageView, View.GONE);
					remoteViews.setViewVisibility(R.id.topPointImageView, View.VISIBLE);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Intent intent = new Intent(context , ALWelcomeActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,	intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.yuanbingImageView,  pendingIntent);
			
			try {
				appWidgetManager.updateAppWidget(cmpName, remoteViews);
			} catch (Exception e) {
				e.toString();
				return;
			}
			
		} else {
			
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.al_dreamdays_widget_top_layout);
			remoteViews.setViewVisibility(R.id.nothingLayout, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.yuanbingImageView, View.GONE);
			remoteViews.setViewVisibility(R.id.eventTopLayout, View.GONE);
			Intent intent = new Intent(context, ALWelcomeActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.nothingLayout , pendingIntent);
			
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
}
