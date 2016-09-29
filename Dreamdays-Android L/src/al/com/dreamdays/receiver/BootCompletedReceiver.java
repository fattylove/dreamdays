package al.com.dreamdays.receiver;

import al.com.dreamdays.activity.ALWelcomeActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ALWelcomeActivity.updateWidget(context);
	}
}
