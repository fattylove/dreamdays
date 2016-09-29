package al.com.dreamdays.activity;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;

import al.com.dreamdays.adapter.SignInUpPagerAdapter;
import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.base.KEY;
import al.com.dreamdays.base.LINKS;
import al.com.dreamdays.fragment.SignInFragment;
import al.com.dreamdays.fragment.SignUpFragment;
import al.com.dreamdays.utils.DialogUtil;
import al.com.dreamdays.utils.MD5Util;
import al.com.dreamdays.utils.UmengClick;
import al.com.dreamdays.view.WrapViewPager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.api.twitter.TwitterAndroid;
import com.guxiu.dreamdays.R;

import fatty.library.http.core.CallBack;
import fatty.library.http.core.HttpService;
import fatty.library.http.core.Parameters;

/**
 * 
 * @author Fatty
 *
 */
public class ALSignInUpActivity extends BaseActivity implements OnClickListener{
	
	private int FACEBOOK_USER = 1;
	private int TWITTER_USER = 2;
	private SharedPreferences preferences ;
	private SharedPreferences.Editor editor;
	private LinearLayout twitterLayout;
	
	private ImageButton exitImageButton;
	
	private RelativeLayout signInLayout;
	private TextView signInTextView ,signInLine;
	
	private RelativeLayout signUpLayout;
	private TextView signUpTextView,signUpLine;
	
	private WrapViewPager viewPager;
	
	private SignInFragment signInFragment;
	private SignUpFragment signUpFragment;
	
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	
	private TextView  facebookTextView;
	private TextView twitterTextView;
	private TextView signReportTextView;
	private TextView titleTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme();
		setContentView(R.layout.al_dreamdays_sign_in_up_layout);
		hiddenView(findViewById(R.id.notifyBarTextView));
		
		preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
		editor = preferences.edit();
		
		signReportTextView = (TextView)this.findViewById(R.id.signReportTextView);
		signReportTextView.setTypeface(BaseApplication.typeface_heavy);
		
		facebookAuthButton = (com.facebook.widget.LoginButton) findViewById(R.id.facebookAuthButton);
		twitterLayout = (LinearLayout)findViewById(R.id.twitterLayout);
		twitterLayout.setOnClickListener(this);
		
		exitImageButton = (ImageButton)this.findViewById(R.id.exitImageButton);
		exitImageButton.setOnClickListener(this);
		signInLayout = (RelativeLayout)this.findViewById(R.id.signInLayout);
		signInLayout.setOnClickListener(this);
		signUpLayout = (RelativeLayout)this.findViewById(R.id.signUpLayout);
		signUpLayout.setOnClickListener(this);
		
		signInTextView = (TextView)this.findViewById(R.id.signInTextView);
		signUpTextView = (TextView)this.findViewById(R.id.signUpTextView);
		titleTextView = (TextView)this.findViewById(R.id.titleTextView);
		signInTextView.setTypeface(BaseApplication.typeface_heavy);
		signUpTextView.setTypeface(BaseApplication.typeface_heavy);
		titleTextView.setTypeface(BaseApplication.typeface_heavy);
		
		signInLine = (TextView)this.findViewById(R.id.signInLine);
		signUpLine = (TextView)this.findViewById(R.id.signUpLine);
		
		viewPager = (WrapViewPager)this.findViewById(R.id.viewPager);
		signInFragment = new SignInFragment();
		signUpFragment = new SignUpFragment();
		fragments.add(signInFragment);
		fragments.add(signUpFragment);
		
		viewPager.setAdapter(new SignInUpPagerAdapter(getSupportFragmentManager(), fragments));  
		viewPager.setCurrentItem(0);
		selectTabStyle(0);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				selectTabStyle(arg0);
			}
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		facebookTextView = (TextView)this.findViewById(R.id.facebookTextView);
		twitterTextView = (TextView)this.findViewById(R.id.twitterTextView);
		facebookTextView.setTypeface(BaseApplication.typeface_heavy);
		twitterTextView.setTypeface(BaseApplication.typeface_heavy);
		
		initTwitter();
		initFacebook(savedInstanceState);
		
	}
	
	public void selectTabStyle(int p){
		switch (p) {
		case 0:
			signInTextView.setTextColor(Color.WHITE);
			signUpTextView.setTextColor(Color.parseColor("#bfcbd1"));
			signInLine.setVisibility(View.VISIBLE);
			signUpLine.setVisibility(View.INVISIBLE);
			break;
		case 1:
			signInTextView.setTextColor(Color.parseColor("#bfcbd1"));
			signUpTextView.setTextColor(Color.WHITE);
			signInLine.setVisibility(View.INVISIBLE);
			signUpLine.setVisibility(View.VISIBLE);
			break;
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exitImageButton:
			finish();
			break;
		case R.id.signInLayout:
			viewPager.setCurrentItem(0);
			break;
			
		case R.id.signUpLayout:
			viewPager.setCurrentItem(1);
			break;
		case R.id.twitterLayout:
			twitter.login(this);
			break;
		default:
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
		Parameters parameters = new Parameters();
		parameters.put("passport", passport);
		parameters.put("password", MD5Util.makeMD5(password));
		parameters.put("nickname", nickname);
		parameters.put("userType", userType + "");//
		parameters.put("mobileType", ""+Constant.ANDROID);
		System.err.println(LINKS.LOGIN + "?" + parameters.toString());
		new HttpService().post(LINKS.LOGIN, parameters, new CallBack<String>() {
			public void onSuccess(String t, int code ) {
				super.onSuccess(t ,code);
				System.err.println(t);
				try {
					JSONObject root = new JSONObject(t);
					int errorCode = root.getInt("errorCode");
					String errorMsg  = root.getString("errorMsg");
					switch (errorCode) {
					case 0:
						editor.putBoolean("isLogin", true).commit();
						editor.putString("passport", passport).commit();
						finish();
						break;
					case 109:
						DialogUtil.showCheckDialog(ALSignInUpActivity.this ,errorMsg);
						break;
					case 103:
						break;
					}
				} catch (Exception e) {
				}
			}

			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				DialogUtil.showCheckDialog(ALSignInUpActivity.this, R.string.login_text_6);
			}
		});
	}

	/****************************          twitter        ************************************/
	private com.api.twitter.TwitterAndroid twitter;
	
	public void initTwitter(){
		twitter4j.conf.ConfigurationBuilder builder = new twitter4j.conf.ConfigurationBuilder();
		builder.setOAuthConsumerKey(Constant.TWITTER_OAuthConsumerKey);
		builder.setOAuthConsumerSecret(Constant.TWITTER_OAuthConsumerSecret );
		builder.setOAuthAccessTokenSecret( Constant.TWITTER_OAuthAccessTokenSecret );
		
		twitter4j.conf.Configuration configuration = builder.build();
		twitter = new TwitterAndroid( this, statusCallback, configuration);
		twitter.logout();
	}
	
	private com.api.twitter.TwitterAndroid.StatusCallback statusCallback = new com.api.twitter.TwitterAndroid.StatusCallback() {
		public void call( com.api.twitter.TwitterAndroid.SessionState newState ) {
			twitter4j.TwitterException exception = newState.getException();
			int state = newState.getState();
			if( null != exception ) {
				return;
			}
			switch( state ) {
				case com.api.twitter.TwitterAndroid.SessionState.LOGIN_FAILED:
					break;
				case com.api.twitter.TwitterAndroid.SessionState.CONNECTED:
					login(newState.getActiveSession().getScreenName()+"@guxiu.ca", MD5Util.makeMD5(""),newState.getActiveSession().getScreenName(),TWITTER_USER);
					break;
				case com.api.twitter.TwitterAndroid.SessionState.DISCONNECTED:
					break;
				case com.api.twitter.TwitterAndroid.SessionState.CONNECTING:
					break;
			}
		}
	};
	
	/****************************          facebook        ************************************/
	private com.facebook.UiLifecycleHelper uiHelper;
	private com.facebook.Session.StatusCallback callback = new com.facebook.Session.StatusCallback() {
		@Override
		public void call(com.facebook.Session session, com.facebook.SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(com.facebook.Session session, com.facebook.SessionState state, Exception exception) {
		System.err.println(state.toString());
		if (state.isOpened()) {
			System.err.println("Logged in ....");
		} else if (state.isClosed()) {
			System.err.println("Logged out...");
		}
	}
	
	private com.facebook.widget.LoginButton facebookAuthButton;
	public void initFacebook(Bundle savedInstanceState){
        uiHelper = new com.facebook.UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
		facebookAuthButton.setReadPermissions(Arrays.asList("email", "user_likes", "user_status"));
		facebookAuthButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UmengClick.OnClick(ALSignInUpActivity.this, KEY.LOGIN_WITH_FACEBOOK);
			}
		});
		
		facebookAuthButton.setUserInfoChangedCallback(new com.facebook.widget.LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(com.facebook.model.GraphUser user) {
            	if(user!=null){
            		try {
            			JSONObject root = new JSONObject(user.getInnerJSONObject().toString());
            			String email = root.getString("email");
            			String name = root.getString("name");
            			if(!TextUtils.isEmpty(email)){
            				login(email, MD5Util.makeMD5("") , name, FACEBOOK_USER);
            			}else{
            				login(name+"@guxiu.ca", MD5Util.makeMD5(""), name, FACEBOOK_USER);
            			}
					} catch (Exception e) {
						System.err.println(e.toString());
					}
            	}
            }
        });
		
		com.facebook.Session session = com.facebook.Session.getActiveSession();
		if(session!=null){
			if(session.isOpened()){
				session.close();
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		com.facebook.Session session = com.facebook.Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	@Override  
    public void onSaveInstanceState(Bundle outState) {  
        super.onSaveInstanceState(outState);  
        uiHelper.onSaveInstanceState(outState);  
    } 
	
	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
}
