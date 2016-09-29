package al.com.dreamdays.base;

import al.com.dreamdays.widget.WidgetUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.guxiu.dreamdays.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 */
public class BaseActivity extends FragmentActivity {
	
	boolean isHome = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AManager.getAppManager().addActivity(this);
	}

	/**
	 * 返回键
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		MobclickAgent.onResume(this);
		
		//刷新widget
		WidgetUtil.widgetNotice(this);
		
		super.onResume();
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		WidgetUtil.widgetNotice(this);
		AManager.getAppManager().finishActivity(this);
		ImageLoader.getInstance().clearMemoryCache();
	}
	
	/**
	 * in animation
	 */
	public void showAnimationOut() {
		overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
	}

	/**
	 * back Animation
	 */
	public void showAnimationIn() {
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		
		ImageLoader.getInstance().clearMemoryCache();
		
		System.gc();
	}
	
	/**
	 * 设置主题   Theme5.0.1
	 */
	public void setTheme(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			setTheme(R.style.AndroidLAppBaseTheme);
		}else{
			setTheme(R.style.AppBaseTheme);
		}
	}
	
	/**
	 * 隐藏title bar
	 * 
	 * @param v
	 */
	public void hiddenView(View v){
		if(isAndroidL()){
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); 
		}else{
			v.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 是不是android l
	 * 
	 * @return
	 */
	public boolean isAndroidL(){
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? true : false ;
	}
	
	/**
	 * 是不是大于4.0
	 * 
	 * @return
	 */
	public boolean is4$0(){
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH ? true : false ;
	}
	
}
