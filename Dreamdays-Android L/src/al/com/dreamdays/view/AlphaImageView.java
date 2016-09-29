package al.com.dreamdays.view;

import java.io.File;
import java.util.ArrayList;

import al.com.dreamdays.activity.ALHomeActivity;
import al.com.dreamdays.base.Constant;
import al.com.dreamdays.sqlite.Matter;
import al.com.dreamdays.utils.FileUtil;
import al.com.dreamdays.utils.ImageUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 
 * @author Fatty
 *
 */
public class AlphaImageView extends ImageView {
	public static final int LOCAL_BITMAP = 90001;
	public static final int RES_BITMAP = 90002;
	
	/**
	 * 
	 * @author Fatty
	 *
	 * OOM监听器
	 *
	 */
	public interface LoadBitmapOOMListener{
		public void success(int state ,int matterId);
		public void failed(int state ,int matterId);
	}
	
	public interface loadBitmapErrorListner{
		public void error();
	}
	
	private LoadBitmapOOMListener onLoadBitmapOOMListener;
	
	public LoadBitmapOOMListener getOnLoadBitmapOOMListener() {
		return onLoadBitmapOOMListener;
	}

	public void setOnLoadBitmapOOMListener(
			LoadBitmapOOMListener onLoadBitmapOOMListener) {
		this.onLoadBitmapOOMListener = onLoadBitmapOOMListener;
	}

	private int mPosition;
	private int mPrePosition = 0;
	
	private float mDegree;
	private ArrayList<Matter> matters = new ArrayList<Matter>();
	private ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	private Context mContext;
	
	private Drawable mNext;
	
	public AlphaImageView(Context context) {
		super(context);
		mContext = context;
	}

	public AlphaImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public AlphaImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
	}
	
	public void setMatters( ArrayList<Matter> matters) {
		this.matters.addAll(matters);
		for(Matter matter : matters){
			
			if (!TextUtils.isEmpty(matter.getPicName()) && FileUtil.checkFileExists(matter.getPicName())) {
				Bitmap bgBitmap = ImageUtil.loadSdcardDrawable(new File(matter.getPicName()), Constant.width, Constant.height) ;
				drawables.add(ImageUtil.bitmap2Drawable(bgBitmap));
				
				//onOOMListener
				if(bgBitmap!=null){
					if(onLoadBitmapOOMListener!=null){
						onLoadBitmapOOMListener.success(LOCAL_BITMAP ,matter.get_id());
					}
				}else{
					if(onLoadBitmapOOMListener!=null){
						onLoadBitmapOOMListener.failed(LOCAL_BITMAP,matter.get_id());
					}
				}
			} else {
				Bitmap bgBitmap = ImageUtil.loadResourceDrawable(mContext, ALHomeActivity.setDetailBg_int(mContext, matter.getClassifyType()));
				drawables.add(ImageUtil.bitmap2Drawable(bgBitmap));
				
				//onOOMListener
				if(bgBitmap!=null){
					if(onLoadBitmapOOMListener!=null){
						onLoadBitmapOOMListener.success(RES_BITMAP ,matter.get_id());
					}
				}else{
					if(onLoadBitmapOOMListener!=null){
						onLoadBitmapOOMListener.failed(RES_BITMAP ,matter.get_id());
					}
				}
			}
		}
		
		if(matters.size()>1){
			mNext = this.drawables.get(1);
		}else{
			mNext = null;
		}
	}

	public void clear(){
		this.matters.clear();
		this.drawables.clear();
	}
	
	public void setmPosition(int mPosition) {
		this.mPosition = mPosition;
	}

	public void setmDegree(float mDegree) {
		this.mDegree = mDegree;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (null == drawables) {
			return;
		}
		int alpha1 = (int) (255 - (mDegree * 255));
		int alpha2 = (int) ((mDegree * 255));
		
		// fore
		Drawable fore = drawables.get(mPosition);
		if(fore != null){
			fore.setBounds(0 ,0 , Constant.width , Constant.height);
			fore.setAlpha(alpha1);
			fore.draw(canvas);
		}
		
		// next
		if(mNext!=null){
			mNext.setBounds(0, 0, Constant.width, Constant.height);
			if (mPrePosition != mPosition) {
				if (mPosition != drawables.size() - 1) {
					mNext = drawables.get(mPosition + 1);
				} else {
					mNext = drawables.get(mPosition);
				}
			}
			if (mPosition == drawables.size() - 1) {
				mNext.setAlpha(255);
			}else{
				mNext.setAlpha(alpha2);
			}
			mNext.draw(canvas);
		}
		
		mPrePosition = mPosition;
		invalidate();
	}
	
}