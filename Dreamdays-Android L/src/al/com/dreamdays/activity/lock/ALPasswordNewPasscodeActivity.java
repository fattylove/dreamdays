package al.com.dreamdays.activity.lock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class ALPasswordNewPasscodeActivity extends ALPasscodeBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		pinReportTextView.setText(R.string.al_lock_enternewpasscode);
		pinReportTextView.setTextColor(Color.parseColor("#607d8b"));
		pinReportTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
		goBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ALPasswordNewPasscodeActivity.this.finish();
			}
		});
	}

	@Override
	protected void onPinLockInserted() {
		String passLock = pinCodeField1.getText().toString()+ pinCodeField2.getText().toString() + pinCodeField3.getText().toString() + pinCodeField4.getText();
		pinCodeField1.setText("");
		pinCodeField2.setText("");
		pinCodeField3.setText("");
		pinCodeField4.setText("");
		pinCodeField1.requestFocus();
		
		SharedPreferences passCodeInfo = getSharedPreferences("PASSCODE_INFO", 0);
		passCodeInfo.edit().putString("passcode_one", passLock).commit();
		
		Intent intent = new Intent();
		intent.setClass(ALPasswordNewPasscodeActivity.this, ALPasswordConfirmPasscodeActivity.class);
		intent.putExtra("passcodeOneExtra", passLock);
		startActivity(intent);
		finish();
	}
}