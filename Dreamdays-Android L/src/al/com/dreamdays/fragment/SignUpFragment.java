package al.com.dreamdays.fragment;

import org.json.JSONObject;

import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.base.KEY;
import al.com.dreamdays.base.LINKS;
import al.com.dreamdays.utils.DialogUtil;
import al.com.dreamdays.utils.EmailUtil;
import al.com.dreamdays.utils.MD5Util;
import al.com.dreamdays.utils.UmengClick;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.guxiu.dreamdays.R;

import fatty.library.http.core.CallBack;
import fatty.library.http.core.HttpService;
import fatty.library.http.core.Parameters;

/**
 * 
 * @author Fatty
 *
 */
public class SignUpFragment extends Fragment implements OnClickListener{
	
	private Activity mActivity;
	private SharedPreferences preferences ;
	private SharedPreferences.Editor editor;

	private EditText passportEditText;
	private TextView passportLineTextView ,passportPortTextView;
	
	private EditText passwordEditText;
	private TextView passwordLineTextView ,passwordPortTextView;
	
	private EditText rePasswordEditText;
	private TextView rePasswordLineTextView ,rePasswordPortTextView;
	
	private Button signUpButton;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
		preferences = activity.getSharedPreferences("Login", Context.MODE_PRIVATE);
		editor = preferences.edit();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.al_dreamdays_sign_up_layout, container, false);
		
		passportEditText = (EditText)rootView.findViewById(R.id.passportEditText);
		passportLineTextView = (TextView)rootView.findViewById(R.id.passportLineTextView);
		passportPortTextView = (TextView)rootView.findViewById(R.id.passportPortTextView);
		
		passportEditText.setTypeface(BaseApplication.typeface_heavy);
		passportPortTextView.setTypeface(BaseApplication.typeface_heavy);
		
		passwordEditText = (EditText)rootView.findViewById(R.id.passwordEditText);
		passwordLineTextView = (TextView)rootView.findViewById(R.id.passwordLineTextView);
		passwordPortTextView = (TextView)rootView.findViewById(R.id.passwordPortTextView);
		
		passwordEditText.setTypeface(BaseApplication.typeface_heavy);
		passwordPortTextView.setTypeface(BaseApplication.typeface_heavy);
		
		rePasswordEditText = (EditText)rootView.findViewById(R.id.rePasswordEditText);
		rePasswordLineTextView = (TextView)rootView.findViewById(R.id.rePasswordLineTextView);
		rePasswordPortTextView = (TextView)rootView.findViewById(R.id.rePasswordPortTextView);
		
		rePasswordEditText.setTypeface(BaseApplication.typeface_heavy);
		rePasswordPortTextView.setTypeface(BaseApplication.typeface_heavy);
		
		passportEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		passwordEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		rePasswordPortTextView.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		signUpButton = (Button)rootView.findViewById(R.id.signUpButton);
		signUpButton.setTypeface(BaseApplication.typeface_heavy);
		signUpButton.setOnClickListener(this);
		
		//Passport
		passportEditText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passportPortTextView.setVisibility(View.INVISIBLE);
				passportLineTextView.setBackgroundColor(Color.parseColor("#cad5db"));
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {				
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		passportEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(!EmailUtil.isEmail(passportEditText.getText().toString().trim())){
						passportPortTextView.setVisibility(View.VISIBLE);
						passportPortTextView.setText(R.string.login_wrong_p_e_c_e_a);
						passportLineTextView.setBackgroundColor(Color.parseColor("#ff5959"));
						return;
					}else{
						passportLineTextView.setBackgroundColor(Color.parseColor("#2196f3"));
					}
				}
			}
		});
		
		passwordEditText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordPortTextView.setVisibility(View.INVISIBLE);
				passwordLineTextView.setBackgroundColor(Color.parseColor("#cad5db"));
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {				
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		//Password
		passwordEditText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordPortTextView.setVisibility(View.INVISIBLE);
				passwordLineTextView.setBackgroundColor(Color.parseColor("#cad5db"));
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {				
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		passwordEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(TextUtils.isEmpty(passwordEditText.getText().toString())){
						passwordPortTextView.setVisibility(View.VISIBLE);
						passwordPortTextView.setText(R.string.login_wrong_y_n_a_s_p);
						passwordLineTextView.setBackgroundColor(Color.parseColor("#ff5959"));
						return;
					}else{
						passwordLineTextView.setBackgroundColor(Color.parseColor("#2196f3"));
					}
				}
			}
		});
		
		passwordEditText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordPortTextView.setVisibility(View.INVISIBLE);
				passwordLineTextView.setBackgroundColor(Color.parseColor("#cad5db"));
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {				
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		passportLineTextView.setBackgroundColor(Color.parseColor("#cad5db"));
		passwordLineTextView.setBackgroundColor(Color.parseColor("#cad5db"));
		rePasswordLineTextView.setBackgroundColor(Color.parseColor("#cad5db"));
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signUpButton:
			
			String email = passportEditText.getText().toString().trim();
			String password = passwordEditText.getText().toString().trim();
			String repassword = rePasswordEditText.getText().toString().trim();
			
			if(TextUtils.isEmpty(email)){
				passportPortTextView.setVisibility(View.VISIBLE);
				passportPortTextView.setText(R.string.login_wrong_e_e);
				passportLineTextView.setBackgroundColor(Color.parseColor("#ff5959"));
				return;
			}
			
			if(!EmailUtil.isEmail(email)){
				passportPortTextView.setVisibility(View.VISIBLE);
				passportPortTextView.setText(R.string.login_wrong_p_e_c_e_a);
				passportLineTextView.setBackgroundColor(Color.parseColor("#ff5959"));
				return;
			}else{
				passportLineTextView.setBackgroundColor(Color.parseColor("#2196f3"));
			}
			
			if(TextUtils.isEmpty(password)){
				passwordPortTextView.setVisibility(View.VISIBLE);
				passwordPortTextView.setText(R.string.login_wrong_p);
				passwordLineTextView.setBackgroundColor(Color.parseColor("#ff5959"));
				return;
			}
			
			if(TextUtils.isEmpty(repassword)){
				rePasswordPortTextView.setVisibility(View.VISIBLE);
				rePasswordPortTextView.setText(R.string.login_wrong_p_d_n_m);
				rePasswordLineTextView.setBackgroundColor(Color.parseColor("#ff5959"));
				return;
			}
			
			if(!TextUtils.equals(password, repassword)){
				rePasswordPortTextView.setVisibility(View.VISIBLE);
				rePasswordPortTextView.setText(R.string.login_wrong_p_d_n_m);
				rePasswordLineTextView.setBackgroundColor(Color.parseColor("#ff5959"));
				return;
			}
			
			register(email, password);
			UmengClick.OnClick(mActivity, KEY.REGISTER);
			
			break;
		case R.id.facebookLayout:
			
			break;
		}
	}
	
	
	/**
	 * 注册接口
	 * 
	 * @param passport
	 * @param password
	 */
	public void register(final String passport, String password) {
		HttpService httpService = new HttpService();
		Parameters parameters = new Parameters();
		String p = MD5Util.makeMD5(password);
		parameters.put("passport", passport);
		parameters.put("password", p);
		parameters.put("mobileType", ""+Constant.ANDROID);
		httpService.post(LINKS.REGISTER, parameters, new CallBack<String>() {
			public void onSuccess(String t, int code) {
				super.onSuccess(t ,code);
				try {
					JSONObject root = new JSONObject(t);
					int errorCode = root.getInt("errorCode");
					String errorMsg = root.getString("errorMsg");
					switch (errorCode) {
					case 0:
						editor.putBoolean("isLogin", true).commit();
						editor.putString("passport", passport).commit();
						mActivity.finish();
						break;
					case 101:
						DialogUtil.showCheckDialog(mActivity, errorMsg);
						break;
					}
				} catch (Exception e) {
				}
			}

			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				DialogUtil.showCheckDialog(mActivity, R.string.login_text_6);
			}
		});
	}
}
