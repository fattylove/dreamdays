package al.com.dreamdays.base;

import java.util.Stack;

import android.app.Activity;

/**
 * 
 * @author Fatty
 * 
 */
public class AManager {

	private static Stack<Activity> activityStack;
	private static AManager instance;

	public static Stack<Activity> getActivityStack() {
		return activityStack;
	}

	private AManager() {
	}

	/**
	 * 单一实例
	 */
	public static AManager getAppManager() {
		if (instance == null) {
			instance = new AManager();
			if (activityStack == null) {
				activityStack = new Stack<Activity>();
			}
		}
		return instance;
	}

	public boolean isHasActivity(Activity activity) {
		boolean result = false;
		if (activityStack.size() > 0) {
			result = activityStack.contains(activity);
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity;
		try {
			activity = activityStack.lastElement();
		} catch (Exception e) {
			return null;
		}
		return activity;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public Activity getActivity(Class<?> clazz) {
		Activity result = null;
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(clazz)) {
				result = activity;
			}
		}
		return result;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		if (activityStack.size() > 0) {
			Activity activity = activityStack.lastElement();
			if (null != activity) {
				activity.finish();
				activity = null;
			}
		}
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}

	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 
	 * @author Fatty
	 *
	 */
	public class ActivityManagerBuilder {

		private String activityName;
		private String activityVersion;
		private String activityCode;
		private String activityExecption;

		private Object activityCreate;
		private Object activityDestory;
		private Object activityStart;
		private Object activityResume;
		private Object activityPause;
		private Object activityRestart;
		private Object activityFinish;
		private Object activityStop;

		public String getActivityName() {
			return activityName;
		}

		public void setActivityName(String activityName) {
			this.activityName = activityName;
		}

		public String getActivityVersion() {
			return activityVersion;
		}

		public void setActivityVersion(String activityVersion) {
			this.activityVersion = activityVersion;
		}

		public String getActivityCode() {
			return activityCode;
		}

		public void setActivityCode(String activityCode) {
			this.activityCode = activityCode;
		}

		public String getActivityExecption() {
			return activityExecption;
		}

		public void setActivityExecption(String activityExecption) {
			this.activityExecption = activityExecption;
		}

		public Object getActivityCreate() {
			return activityCreate;
		}

		public void setActivityCreate(Object activityCreate) {
			this.activityCreate = activityCreate;
		}

		public Object getActivityDestory() {
			return activityDestory;
		}

		public void setActivityDestory(Object activityDestory) {
			this.activityDestory = activityDestory;
		}

		public Object getActivityStart() {
			return activityStart;
		}

		public void setActivityStart(Object activityStart) {
			this.activityStart = activityStart;
		}

		public Object getActivityResume() {
			return activityResume;
		}

		public void setActivityResume(Object activityResume) {
			this.activityResume = activityResume;
		}

		public Object getActivityPause() {
			return activityPause;
		}

		public void setActivityPause(Object activityPause) {
			this.activityPause = activityPause;
		}

		public Object getActivityRestart() {
			return activityRestart;
		}

		public void setActivityRestart(Object activityRestart) {
			this.activityRestart = activityRestart;
		}

		public Object getActivityFinish() {
			return activityFinish;
		}

		public void setActivityFinish(Object activityFinish) {
			this.activityFinish = activityFinish;
		}

		public Object getActivityStop() {
			return activityStop;
		}

		public void setActivityStop(Object activityStop) {
			this.activityStop = activityStop;
		}

	}
}