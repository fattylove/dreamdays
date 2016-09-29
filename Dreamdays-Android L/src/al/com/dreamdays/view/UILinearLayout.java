package al.com.dreamdays.view;

import java.util.NoSuchElementException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * 
 * @author Fatty
 * 
 *         上拉滑ViewGroup
 * 
 */
@SuppressLint("NewApi")
public class UILinearLayout extends LinearLayout {

	
	public interface OnUILayoutScrollListener{
		public void scroll(int y);
	}
	
	public OnUILayoutScrollListener onUILayoutScrollListener;

	public void setOnUILayoutScrollListener( OnUILayoutScrollListener onUILayoutScrollListener) {
		this.onUILayoutScrollListener = onUILayoutScrollListener;
	}

	public UILinearLayout(Context context) {
		super(context);
	}
	
	public UILinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UILinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	public UILinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}
	
	private OnGiveUpTouchEventListener mGiveUpTouchEventListener;

	public interface OnGiveUpTouchEventListener {
		public boolean giveUpTouchEvent(MotionEvent event);
	}

	public void setOnGiveUpTouchEventListener(OnGiveUpTouchEventListener l) {
		mGiveUpTouchEventListener = l;
	}

	private View mHeader;
	private View mContent;

	private int mOriginalHeaderHeight;
	private int mHeaderHeight;

	private int mStatus = STATUS_EXPANDED;
	public static final int STATUS_EXPANDED = 1;
	public static final int STATUS_COLLAPSED = 2;
	private int mTouchSlop;
	private int mLastY = 0;
	private int mLastYIntercept = 0;

	private boolean mIsSticky = true;

	
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		if (hasWindowFocus && (mHeader == null || mContent == null)) {
			int headerId = getResources().getIdentifier("header", "id", getContext().getPackageName());
			int contentId = getResources().getIdentifier("content", "id", getContext().getPackageName());
			if (headerId != 0 && contentId != 0) {
				mHeader = findViewById(headerId);
				mContent = findViewById(contentId);
				mOriginalHeaderHeight = mHeader.getMeasuredHeight();
				mHeaderHeight = mOriginalHeaderHeight;
				mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
			} else {
				throw new NoSuchElementException("Did your view with id \"header\" or \"content\" exists?");
			}
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		int intercepted = 0;
		int y = (int) event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			mLastYIntercept = y;
			mLastY = y;
			intercepted = 0;
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			int deltaY = y - mLastYIntercept;
			if (mStatus == STATUS_EXPANDED && deltaY <= -mTouchSlop) {
				intercepted = 1;
			} else if (mGiveUpTouchEventListener != null) {
				if (mGiveUpTouchEventListener.giveUpTouchEvent(event) && deltaY >= mTouchSlop) {
					intercepted = 1;
				}
			}
			break;
		}
		case MotionEvent.ACTION_UP: {
			intercepted = 0;
			break;
		}
		}
		return intercepted != 0 && mIsSticky;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!mIsSticky) {
			return true;
		}
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if(onUILayoutScrollListener != null){
				onUILayoutScrollListener.scroll(mHeaderHeight);
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			int deltaY = y - mLastY;
			mHeaderHeight += deltaY;
			setHeaderHeight(mHeaderHeight);
			if(onUILayoutScrollListener != null){
				onUILayoutScrollListener.scroll(mHeaderHeight);
			}
			break;
		}
		case MotionEvent.ACTION_UP: {
			int destHeight = 0;
			if (mHeaderHeight <= mOriginalHeaderHeight * 0.5) {
				destHeight = 0;
				mStatus = STATUS_COLLAPSED;
			} else {
				destHeight = mOriginalHeaderHeight;
				mStatus = STATUS_EXPANDED;
			}
			this.smoothSetHeaderHeight(mHeaderHeight, destHeight);
			if(onUILayoutScrollListener != null){
				onUILayoutScrollListener.scroll(mHeaderHeight);
			}
			break;
		}
		default:
			break;
		}
		mLastY = y;
		return true;
	}

	public void smoothSetHeaderHeight(final int from, final int to) {
		smoothSetHeaderHeight(from, to, false);
	}

	/**
	 * 
	 * 设置view的弹回动画
	 * 
	 * @param from
	 * @param to
	 * @param modifyOriginalHeaderHeight
	 * 
	 */
	public void smoothSetHeaderHeight(final int from, final int to, final boolean modifyOriginalHeaderHeight) {
		final int frameCount = (int) (600 / 1000f * 30) + 1;
		final float partation = (to - from) / (float) frameCount;
		new Thread("Thread#smoothSetHeaderHeight") {
			@Override
			public void run() {
				for (int i = 0; i < frameCount; i++) {
					final int height;
					if (i == frameCount - 1) {
						height = to;
					} else {
						height = (int) (from + partation * i);
					}
					post(new Runnable() {
						@Override
						public void run() {
							setHeaderHeight(height);
						}
					});
					try {
						sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (modifyOriginalHeaderHeight) {
					setOriginalHeaderHeight(to);
				}
			};
		}.start();
	}

	/**
	 * 
	 * @param originalHeaderHeight
	 */
	public void setOriginalHeaderHeight(int originalHeaderHeight) {
		mOriginalHeaderHeight = originalHeaderHeight;
	}

	/**
	 * 
	 * @param height
	 * @param modifyOriginalHeaderHeight
	 */
	public void setHeaderHeight(int height, boolean modifyOriginalHeaderHeight) {
		if (modifyOriginalHeaderHeight) {
			setOriginalHeaderHeight(height);
		}
		setHeaderHeight(height);
	}

	/**
	 * 
	 * @param height
	 */
	public void setHeaderHeight(int height) {
		if (height < 0) {
			height = 0;
		} else if (height > mOriginalHeaderHeight) {
			height = mOriginalHeaderHeight;
		}

		if (mHeader != null && mHeader.getLayoutParams() != null) {
			mHeader.getLayoutParams().height = height;
			mHeader.requestLayout();
			mHeaderHeight = height;
		} else {
		}
	}

	/**
	 * 
	 * @param isSticky
	 */
	public void setSticky(boolean isSticky) {
		mIsSticky = isSticky;
	}

}
