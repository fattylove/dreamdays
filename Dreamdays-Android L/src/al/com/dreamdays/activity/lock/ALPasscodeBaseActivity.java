package al.com.dreamdays.activity.lock;

import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.BaseApplication;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guxiu.dreamdays.R;

/**
 * 
 * 
 * @author Fatty
 *
 */
public abstract class ALPasscodeBaseActivity extends BaseActivity {

	protected TextView pinReportTextView;

	protected EditText pinCodeField1 = null;
	protected EditText pinCodeField2 = null;
	protected EditText pinCodeField3 = null;
	protected EditText pinCodeField4 = null;
	protected InputFilter[] filters = null;
	protected TextView topMessage = null;
	
	protected RelativeLayout homeTitleLayout ;
	protected Button goBackButton;
	protected TextView  titleMsgTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme();
		setContentView(R.layout.app_passcode_keyboard);
		hiddenView(findViewById(R.id.notifyBarTextView));
		
		homeTitleLayout = (RelativeLayout)this.findViewById(R.id.homeTitleLayout);
		goBackButton = (Button)this.findViewById(R.id.goBackButton);
		titleMsgTextView = (TextView)this.findViewById(R.id.titleMsgTextView);
		pinReportTextView = (TextView) findViewById(R.id.pinReportTextView);
		
		titleMsgTextView.setTypeface(BaseApplication.typeface_heavy);
		pinReportTextView.setTypeface(BaseApplication.typeface_medium);
		
		filters = new InputFilter[2];
		filters[0] = new InputFilter.LengthFilter(1);
		filters[1] = onlyNumber;
		
		pinCodeField1 = (EditText) findViewById(R.id.pincode_1);
		setupPinItem(pinCodeField1);
		pinCodeField2 = (EditText) findViewById(R.id.pincode_2);
		setupPinItem(pinCodeField2);
		pinCodeField3 = (EditText) findViewById(R.id.pincode_3);
		setupPinItem(pinCodeField3);
		pinCodeField4 = (EditText) findViewById(R.id.pincode_4);
		setupPinItem(pinCodeField4);
		
		// setup the keyboard
		((Button) findViewById(R.id.button0)).setOnClickListener(defaultButtonListener);
		((Button) findViewById(R.id.button1)).setOnClickListener(defaultButtonListener);
		((Button) findViewById(R.id.button2)).setOnClickListener(defaultButtonListener);
		((Button) findViewById(R.id.button3)).setOnClickListener(defaultButtonListener);
		((Button) findViewById(R.id.button4)).setOnClickListener(defaultButtonListener);
		((Button) findViewById(R.id.button5)).setOnClickListener(defaultButtonListener);
		((Button) findViewById(R.id.button6)).setOnClickListener(defaultButtonListener);
		((Button) findViewById(R.id.button7)).setOnClickListener(defaultButtonListener);
		((Button) findViewById(R.id.button8)).setOnClickListener(defaultButtonListener);
		((Button) findViewById(R.id.button9)).setOnClickListener(defaultButtonListener);
		((Button) findViewById(R.id.button_erase)).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (pinCodeField1.isFocused()) {
					pinCodeField1.setBackgroundResource(R.drawable.al_pin_dot_default);
				} else if (pinCodeField2.isFocused()) {
					pinCodeField2.setBackgroundResource(R.drawable.al_pin_dot_default);
					pinCodeField1.requestFocus();
					pinCodeField1.setText("");
				} else if (pinCodeField3.isFocused()) {
					pinCodeField3.setBackgroundResource(R.drawable.al_pin_dot_default);
					pinCodeField2.requestFocus();
					pinCodeField2.setText("");
				} else if (pinCodeField4.isFocused()) {
					pinCodeField4.setBackgroundResource(R.drawable.al_pin_dot_default);
					pinCodeField3.requestFocus();
					pinCodeField3.setText("");
				}
			}
		});
	}

	protected void setupPinItem(EditText item) {
		item.setInputType(InputType.TYPE_NULL);
		item.setFilters(filters);
		item.setTransformationMethod(PasswordTransformationMethod.getInstance());
	}

	private OnClickListener defaultButtonListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int currentValue = -1;
			int id = arg0.getId();
			if (id == R.id.button0) {
				currentValue = 0;
			} else if (id == R.id.button1) {
				currentValue = 1;
			} else if (id == R.id.button2) {
				currentValue = 2;
			} else if (id == R.id.button3) {
				currentValue = 3;
			} else if (id == R.id.button4) {
				currentValue = 4;
			} else if (id == R.id.button5) {
				currentValue = 5;
			} else if (id == R.id.button6) {
				currentValue = 6;
			} else if (id == R.id.button7) {
				currentValue = 7;
			} else if (id == R.id.button8) {
				currentValue = 8;
			} else if (id == R.id.button9) {
				currentValue = 9;
			} else {
			}

			String currentValueString = String.valueOf(currentValue);
			// SET current value String
			if (pinCodeField1.isFocused()) {
				pinCodeField1.setText(currentValueString);
				pinCodeField1.setBackgroundResource(R.drawable.al_pin_dot_selector);
				pinCodeField2.requestFocus();
				pinCodeField2.setText("");
			} else if (pinCodeField2.isFocused()) {
				pinCodeField2.setText(currentValueString);
				pinCodeField2.setBackgroundResource(R.drawable.al_pin_dot_selector);
				pinCodeField3.requestFocus();
				pinCodeField3.setText("");
			} else if (pinCodeField3.isFocused()) {
				pinCodeField3.setText(currentValueString);
				pinCodeField3.setBackgroundResource(R.drawable.al_pin_dot_selector);
				pinCodeField4.requestFocus();
				pinCodeField4.setText("");
			} else if (pinCodeField4.isFocused()) {
				pinCodeField4.setBackgroundResource(R.drawable.al_pin_dot_selector);
				pinCodeField4.setText(currentValueString);
			}

			if (pinCodeField4.getText().toString().length() > 0 && pinCodeField3.getText().toString().length() > 0 && pinCodeField2.getText().toString().length() > 0 && pinCodeField1.getText().toString().length() > 0) {
				onPinLockInserted();
			}
		}
	};

	protected abstract void onPinLockInserted();

	private InputFilter onlyNumber = new InputFilter() {
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			if (source.length() > 1)
				return "";
			if (source.length() == 0)
				return null;
			try {
				int number = Integer.parseInt(source.toString());
				if ((number >= 0) && (number <= 9))
					return String.valueOf(number);
				else
					return "";
			} catch (NumberFormatException e) {
				return "";
			}
		}
	};

}