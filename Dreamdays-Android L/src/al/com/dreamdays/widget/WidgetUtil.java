package al.com.dreamdays.widget;

import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author Fatty
 *
 */
public class WidgetUtil {
	
	/**
	 * 刷新 widget
	 * 
	 * @param context
	 */
	public static void widgetNotice(Context context) {
		Intent refreshIntent = new Intent(context, DreamdaysWidget.class);
		refreshIntent.setAction(DreamdaysWidget.REFRESH);
		context.sendBroadcast(refreshIntent);
		
		DreamdaysWidgetNext.updateNextUI(context);
		DreamdaysWidgetCover.updateNextUI(context);
		DreamdaysWidgetSmall.updateSmallUI(context);
		
		System.err.println("-------------DreamdaysWidget REFRESH---------------");
	}
}
