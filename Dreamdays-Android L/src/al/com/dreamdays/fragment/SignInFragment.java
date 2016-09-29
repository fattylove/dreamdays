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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.ProgressBar;
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
public class SignInFragment extends Fragment implements OnClickListener{
	
	private Activity mActivity;
	
	private int GENERAL_USER = 0;

	private SharedPreferences preferences ;
	private SharedPreferences.Editor editor;
	
	private ProgressBar progressBar;
	private EditText passportEditText;
	private TextView passportLineTextView ,passportPortTextView;
	
	private EditText passwordEditText;
	private TextView passwordLineTextView,passwordPortTextView;
	
	private Button signInButton;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
		preferences = activity.getSharedPreferences("Login", Context.MODE_PRIVATE);
		editor = preferences.edit();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.al_dreamdays_sign_in_layout, container, false);
		progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
		passportEditText = (EditText)rootView.findViewById(R.id.passportEditText);
		passportLineTextView = (TextView)rootView.findViewById(R.id.passportLineTextView);
		passportPortTextView = (TextView)rootView.findViewById(R.id.passportPortTextView);
		passportLineTextView.setBackgroundColor(Color.parseColor("#cad5db"));
		
		passportEditText.setTypeface(BaseApplication.typeface_heavy);
		passportPortTextView.setTypeface(BaseApplication.typeface_heavy);
		
		passportEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
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
		
		passwordEditText = (EditText)rootView.findViewById(R.id.passwordEditText);
		passwordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		passwordLineTextView = (TextView)rootView.findViewById(R.id.passwordLineTextView);
		passwordPortTextView = (TextView)rootView.findViewById(R.id.passwordPortTextView);
		
		passwordEditText.setTypeface(BaseApplication.typeface_heavy);
		passwordPortTextView.setTypeface(BaseApplication.typeface_heavy);
		
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
		
		signInButton = (Button)rootView.findViewById(R.id.signInButton);
		signInButton.setTypeface(BaseApplication.typeface_heavy);
		signInButton.setOnClickListener(this);
		
		passportLineTextView.setBackgroundColor(Color.parseColor("#cad5db"));
		passwordLineTextView.setBackgroundColor(Color.parseColor("#cad5db"));
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signInButton:
			String email = passportEditText.getText().toString().trim();
			String password = passwordEditText.getText().toString().trim();
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
			login(email, password, "", GENERAL_USER);
			UmengClick.OnClick(mActivity, KEY.LOGIN_WITH_YOUR_MAIL);
			break;
		case R.id.facebookLayout:
			break;
		}
	}
	
	
	/**
	 * 登录接口
	 * 
	 * @param passport
	 * @param password
	 * @param nickname
	 * @param userType
	 */
	public void login(final String passport, String password, String nickname, int userType) {
		progressBar.setVisibility(View.VISIBLE);
		Parameters parameters = new Parameters();
		parameters.put("passport", passport);
		parameters.put("password", MD5Util.makeMD5(password));
		parameters.put("nickname", nickname);
		parameters.put("userType", userType + "");//
		parameters.put("mobileType", ""+Constant.ANDROID);
		new HttpService().post(LINKS.LOGIN, parameters, new CallBack<String>() {
			public void onSuccess(String t, int code ) {
				super.onSuccess(t ,code);
				try {
					JSONObject root = new JSONObject(t);
					int errorCode = root.getInt("errorCode");
					String errorMsg  = root.getString("errorMsg");
					switch (errorCode) {
					case 0:
						editor.putBoolean("isLogin", true).commit();
						editor.putString("passport", passport).commit();
						mActivity.finish();
						break;
					case 109:
						passwordEditText.setText("");
						DialogUtil.showCheckDialog(mActivity ,errorMsg);
						break;
					case 103:
						loginCount++;
						if (loginCount == 2) {
							showDialog(mActivity);
							return;
						}else{
							passwordEditText.setText("");
							DialogUtil.showCheckDialog(mActivity , errorMsg);
						}
						break;
					}
				} catch (Exception e) {
				}
				progressBar.setVisibility(View.GONE);
			}

			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				progressBar.setVisibility(View.GONE);
				DialogUtil.showCheckDialog(mActivity, R.string.login_text_6);
			}
		});
	}
	
	/**
	 * 显示忘记密码Dialog
	 */
	private int loginCount = -1;
	public void showDialog(Context context) {
		AlertDialog.Builder builder;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			builder= new Builder(context , android.R.style.Theme_Material_Light_Dialog);
		}else{
			builder= new Builder(context , android.R.style.Theme_Holo_Light_Dialog);
		}
		builder.setTitle(R.string.login_forget_title);
		builder.setMessage(R.string.login_forget_content);
		builder.setPositiveButton(context.getString(R.string.login_forget_ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				loginCount = -1;
				forget(passportEditText.getText().toString().trim());
			}
		});
		builder.setNegativeButton(R.string.login_forget_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				progressBar.setVisibility(View.GONE);
				loginCount = -1;
			}
		});
		builder.create().show();
	}
	
	/**
	 * @param passport
	 */
	public void forget(String passport) {
		progressBar.setVisibility(View.VISIBLE);
		Parameters parameters = new Parameters();
		parameters.put("passport", passport);
		new HttpService().post(LINKS.FORGET, parameters, new CallBack<String>() {
			public void onSuccess(String t , int code) {
				super.onSuccess(t ,code);
				loginCount = -1;
				DialogUtil.showCheckDialog(mActivity, R.string.login_wrong_password_send);
				progressBar.setVisibility(View.GONE);
			}
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				loginCount = -1;
				DialogUtil.showCheckDialog(mActivity, R.string.login_text_6);
				progressBar.setVisibility(View.GONE);
			}
		});
	}
}
