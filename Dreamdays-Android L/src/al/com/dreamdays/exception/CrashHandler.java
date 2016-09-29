package al.com.dreamdays.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";

	private static CrashHandler INSTANCE = new CrashHandler();
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private Context mContext;

	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当 UncaughtException 发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		}
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}