package al.com.dreamdays.activity.lock;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class ALPasscodeUnlockActivity extends ALPasscodeBaseActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		pinReportTextView.setText(R.string.al_lock_enteryourpasscode);
		pinReportTextView.setTextColor(Color.parseColor("#607d8b"));
		pinReportTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
		goBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	protected void onPinLockInserted() {
		String passLock = pinCodeField1.getText().toString()+ pinCodeField2.getText().toString()+ pinCodeField3.getText().toString() + pinCodeField4.getText();
		SharedPreferences passCodeInfo = getSharedPreferences("PASSCODE_INFO", 0);
		
		if (!TextUtils.isEmpty(passCodeInfo.getString("passcode", ""))) {
			if (passLock.equals(passCodeInfo.getString("passcode", ""))) {
				passCodeInfo.edit().putString("passcode_one", "").commit();
				passCodeInfo.edit().putString("passcode", "").commit();
				passCodeInfo.edit().putString("passcode_two", "").commit();
				finish();
			} else {
				showPasswordError();
				pinCodeField1.setText("");
				pinCodeField2.setText("");
				pinCodeField3.setText("");
				pinCodeField4.setText("");
				pinCodeField1.setBackgroundResource(R.drawable.al_pin_dot_default);
				pinCodeField2.setBackgroundResource(R.drawable.al_pin_dot_default);
				pinCodeField3.setBackgroundResource(R.drawable.al_pin_dot_default);
				pinCodeField4.setBackgroundResource(R.drawable.al_pin_dot_default);
				pinCodeField1.requestFocus();
			}
		}
	}
	
	protected void showPasswordError() {
		setError(R.string.passcode_wrong_passcode);
	}
	
	public void setError(String msg) {
		pinReportTextView.setText(msg);
		pinReportTextView.setTextColor(Color.parseColor("#ea3232"));
		pinReportTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0f);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				pinReportTextView.setText("Enter your passcode");
				pinReportTextView.setTextColor(Color.parseColor("#607d8b"));
				pinReportTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
			}
		}, 1500);
	}

	public void setError(int r) {
		pinReportTextView.setText(r);
		pinReportTextView.setTextColor(Color.parseColor("#ea3232"));
		pinReportTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0f);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				pinReportTextView.setText("Enter your passcode");
				pinReportTextView.setTextColor(Color.parseColor("#607d8b"));
				pinReportTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
			}
		}, 1500);
	}
}