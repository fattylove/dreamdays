package al.com.dreamdays.widget;

import al.com.dreamdays.activity.ALWelcomeActivity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class DreamdaysWidget extends AppWidgetProvider {
	// log tag
	public static final String REFRESH = "com.roc.dreamdays.refresh";
 
	@Override
	public IBinder peekService(Context myContext, Intent service) {
		return super.peekService(myContext, service);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		for (int i = 0; i < appWidgetIds.length; i++) {
			Intent intentService = new Intent(context, WidgetUpdateService.class);
			intentService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			intentService.setData(Uri.parse(intentService.toUri(Intent.URI_INTENT_SCHEME)));
			
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.al_dreamdays_app_widget);
			remoteViews.setRemoteAdapter(R.id.dreamdaywidget_list, intentService);

			/**
			 * 
			 */
			Intent mainIntent = new Intent(context, ALWelcomeActivity.class);
			PendingIntent mainPendingIntent = PendingIntent.getActivity(context,0, mainIntent, 0);
			remoteViews.setOnClickPendingIntent(R.id.show,	mainPendingIntent);
			
			/**
			 * 
			 */
			Intent createIntent = new Intent(context, ALWelcomeActivity.class);
			PendingIntent createPendingIntent = PendingIntent.getActivity(context,0, createIntent, 0);
			remoteViews.setOnClickPendingIntent(R.id.create,	createPendingIntent);

			/**
			 * 
			 */
			Intent clickIntent = new Intent(context, ALWelcomeActivity.class);
			PendingIntent clickPI = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setPendingIntentTemplate(R.id.dreamdaywidget_list, clickPI);

			try {
				appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
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

	@Override
	public void onReceive(Context context, Intent intent) {
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);
		ComponentName cmpName = new ComponentName(context,DreamdaysWidget.class);
		if (intent.getAction().equals(REFRESH)) {
			int[] appWidgetIds = mgr.getAppWidgetIds(cmpName);
			mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.dreamdaywidget_list);
		}
		super.onReceive(context, intent);
	}
}
