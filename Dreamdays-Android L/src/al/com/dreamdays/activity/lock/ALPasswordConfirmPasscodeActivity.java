package al.com.dreamdays.activity.lock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class ALPasswordConfirmPasscodeActivity extends ALPasscodeBaseActivity {

	private String onePassword = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onePassword = getIntent().getStringExtra("passcodeOneExtra");
		
		pinReportTextView.setText(R.string.al_lock_confirmnewpasscode);
		pinReportTextView.setTextColor(Color.parseColor("#607d8b"));
		pinReportTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
		goBackButton.setVisibility(View.VISIBLE);
		goBackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ALPasswordConfirmPasscodeActivity.this, ALPasswordNewPasscodeActivity.class);
				ALPasswordConfirmPasscodeActivity.this.startActivity(intent);
				finish();
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

		if(onePassword.equals(passLock)){
			passCodeInfo.edit().putString("passcode_two", passLock).commit();
			if(TextUtils.equals(passCodeInfo.getString("passcode_one", ""), passCodeInfo.getString("passcode_two", ""))){
				passCodeInfo.edit().putString("passcode", passLock).commit();
			}
			finish();
		}else{
			
			setError(R.string.passcode_wrong_passcode);
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
	
	protected void showPasswordError() {
		setError(R.string.passcode_wrong_passcode);
	}
	
	public void setError(String msg) {
		pinReportTextView.setText(msg);
		pinReportTextView.setTextColor(Color.parseColor("#ea3232"));
		pinReportTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0f);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				pinReportTextView.setText("Confirm new passcode");
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
				pinReportTextView.setText("Confirm new passcode");
				pinReportTextView.setTextColor(Color.parseColor("#607d8b"));
				pinReportTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
			}
		}, 1500);
	}
	
}