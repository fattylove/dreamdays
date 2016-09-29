package al.com.dreamdays.receiver;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import al.com.dreamdays.activity.ALAddEditEventActivity;
import al.com.dreamdays.activity.ALWelcomeActivity;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.sqlite.Matter;
import al.com.dreamdays.sqlite.MatterService;
import al.com.dreamdays.widget.WidgetUtil;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 * 
 * 
 * notifications and repeat notifications
 * 
 *
 */
@SuppressWarnings("deprecation")
public class AppAlarmReceiver extends BroadcastReceiver {

	private MatterService matterService;
	private Date nofityDate ;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		WidgetUtil.widgetNotice(context);
		
		matterService = new MatterService();
		
		//notification
		ArrayList<Matter> notifyMatters = (ArrayList<Matter>) matterService.queryAllMatter(context , 1);
		for (Matter matter : notifyMatters) {
			String dateStr = matter.getMatterTime();
			try {
				nofityDate = Constant.appDateFormat.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			nofityDate.setHours(9);
			nofityDate.setMinutes(30);
			String equal_matterDate = Constant.equalDateFormat.format(nofityDate);
			String equal_currentDate = Constant.equalDateFormat.format(new Date(System.currentTimeMillis()));
			
			System.err.println("notify:matterName->"+matter.getMatterName()+", if_notify->"+matter.getIf_notify() +", matter_time->" + matter.getMatterTime() + ", equal_matterDate->" +equal_matterDate +", equal_currentDate->"+ equal_currentDate);
			if (TextUtils.equals(equal_matterDate, equal_currentDate) && matter.getIf_notify() == 1) {
				showWearNotification(context,matter);
				matterService.updateNotify(context, matter);
			}
		}
		
		//repeat
		ArrayList<Matter> repeatMatters = (ArrayList<Matter>) matterService.queryAllRepeatMatter(context);
		Calendar calender = Calendar.getInstance(Locale.ENGLISH);
		for(Matter matter : repeatMatters ){
			System.err.println("repeat:matterName->"+matter.getMatterName() +", repeat_type->" + matter.getRepeat_type() + ", create_time->" + matter.getMatterTime());
			String currentDate = Constant.appDateFormat.format(new Date());
			String repeatDate = matter.getMatterTime();
			if(currentDate.equals(repeatDate)){
				//发通知
				String eDate = Constant.equalDateFormat_3.format(new Date());
				if(eDate.equals("09:30")){
					showWearNotification(context,matter);
					try {
						calender.setTime(Constant.appDateFormat.parse(repeatDate));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					switch (matter.getRepeat_type()) {
					case 1://Weekly  - 		7天
						calender.add(Calendar.DATE, 7);
						matter.setMatterTime(Constant.appDateFormat.format(calender.getTime()));
						break;
					case 2://Bi-Weekly  	14天
						calender.add(Calendar.DATE, 14);
						matter.setMatterTime(Constant.appDateFormat.format(calender.getTime()));
						break;
					case 3://Monthly   		1个月
						calender.add(Calendar.MONTH, 1);
						matter.setMatterTime(Constant.appDateFormat.format(calender.getTime()));
						break;
					case 4://Quarterly   	4个月
						calender.add(Calendar.MONTH, 4);
						matter.setMatterTime(Constant.appDateFormat.format(calender.getTime()));
						break;
					case 5://Semi-annually  6个月
						calender.add(Calendar.MONTH, 6);
						matter.setMatterTime(Constant.appDateFormat.format(calender.getTime()));
						break;
					case 6://Annually       12个月
						calender.add(Calendar.YEAR, 1);
						matter.setMatterTime(Constant.appDateFormat.format(calender.getTime()));
						break;
					}
					matterService.insertOrUpdateNewMatter(context, matter);
				}
			}
		}
		System.err.println("----------App Alarm Receiver---------");
	}
	
	/**
	 * 
	 * show Notification bar
	 * 
	 * set information by events
	 * 
	 * Use NotificationCompat and NotificationCompat.Builder from support.v4
	 * 
	 * 
	 * @param context
	 * @param matter
	 */
	void showWearNotification(Context context , Matter matter){
		SharedPreferences preferences = context.getSharedPreferences("NOTIFICATION", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		// 存储notification ids
		String id = matter.get_id()+"@";
		if(preferences.contains("notificationIds")){
			String ids = preferences.getString("notificationIds", "");
			if(!TextUtils.isEmpty(ids)){
				editor.putString("notificationIds", ids+id);
				editor.commit();
			}else{
				editor.putString("notificationIds", id);
				editor.commit();
			}
		}else{
			editor.putString("notificationIds", id);
			editor.commit();
		}
		
		Intent homeIntent = new Intent();
		homeIntent.setClass(context, ALWelcomeActivity.class);
		homeIntent.setAction("Notification");
		PendingIntent homePendingIntent = PendingIntent.getActivity(context, 0, homeIntent, Intent.FLAG_ACTIVITY_NEW_TASK );

		Intent addIntent = new Intent();
		addIntent.putExtra("isCreate" , true);
		addIntent.setClass(context, ALAddEditEventActivity.class);
		String expandedTitle = context.getString(R.string.app_name);
		String expandedText = context.getString(R.string.alarm_title)+" "+ matter.getMatterName();
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(expandedTitle)
		        .setContentText(expandedText)
		        .setWhen(System.currentTimeMillis())
		        .setTicker(context.getString(R.string.app_name))
		        .setDefaults(Notification.DEFAULT_SOUND)
		        .setContentIntent(homePendingIntent) ;
		
		NotificationManagerCompat notificationManager =  NotificationManagerCompat.from(context);
		notificationManager.notify(matter.get_id(), notificationBuilder.build());
	}
	
}
