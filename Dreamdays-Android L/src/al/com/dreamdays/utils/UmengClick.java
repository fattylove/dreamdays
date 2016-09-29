package al.com.dreamdays.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

public class UmengClick {

	public static void OnClick(Context context , String eventId){
		MobclickAgent.onEvent(context, eventId);
		System.err.println("The event name :"+eventId+" is clicked...");
	}
	
}
